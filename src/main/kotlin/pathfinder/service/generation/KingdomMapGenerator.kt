package pathfinder.service.generation

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import pathfinder.domain.Campaign
import pathfinder.domain.kingdom.terrain.Hex
import pathfinder.domain.kingdom.terrain.KingdomMap
import pathfinder.domain.kingdom.terrain.TerrainType
import pathfinder.domain.kingdom.terrain.features.TerrainFeature
import pathfinder.domain.support.coordinate.HexCoordinate
import kotlin.math.sqrt
import kotlin.random.Random

@Service
class KingdomMapGenerator {
    private val log = LoggerFactory.getLogger(javaClass.simpleName)
    private val seed: Long = Random.nextLong()
    private val random = Random(seed)
    private val noiseGenerator = PerlinNoise(seed)

    private companion object {
        // Terrain generation scales
        const val NOISE_SCALE = 0.08  // Slightly reduced for more gradual changes

        // Mountain ranges (2-8 hexes = 24-96 miles)
        const val MIN_MOUNTAIN_RANGE = 2
        const val MAX_MOUNTAIN_RANGE = 8
        const val MOUNTAIN_RANGE_CHANCE = 0.25
        const val MOUNTAIN_BRANCH_CHANCE = 0.15

        // Forests (1-4 hexes = 12-48 miles)
        const val MIN_FOREST_SIZE = 1
        const val MAX_FOREST_SIZE = 4
        const val FOREST_CHANCE = 0.35

        // Lakes (1-3 hexes = 12-36 miles)
        const val MIN_LAKE_SIZE = 1
        const val MAX_LAKE_SIZE = 3
        val LAKE_SPREAD_CHANCE = { depth: Int -> 0.5 / (depth + 1) }

        // Rivers
        const val RIVER_START_CHANCE = 0.15
        const val MIN_RIVER_SPACING = 2  // 24 miles minimum between sources

        // Climate transitions
        const val CLIMATE_SCALE = 0.06  // More gradual climate changes

        // Terrain thresholds (currently used but not defined)
        const val DEEP_WATER_THRESHOLD = -0.3
        const val COASTAL_THRESHOLD = -0.1
        const val LOWLAND_THRESHOLD = 0.2
        const val HILL_THRESHOLD = 0.5

        // Climate thresholds (currently used but not defined)
        const val HIGH_TEMPERATURE = 0.6
        const val MEDIUM_TEMPERATURE = 0.3
        const val HIGH_MOISTURE = 0.6
        const val MEDIUM_MOISTURE = 0.3

        // Feature generation chances (currently used but not defined)
        const val LANDMARK_CHANCE = 0.05
        const val BRIDGE_CHANCE = 0.3
        const val JUNGLE_CONVERSION_CHANCE = 0.7

        // Terrain smoothing constants (used in addSurroundingHills)
        const val DIRECTION_CHANGE_CHANCE = 0.3

        // Cavern generation
        const val LAIR_TO_CAVERN_CHANCE = 0.4
        const val CAVERN_SPREAD_CHANCE = 0.3
        const val CAVERN_DESCENT_CHANCE = 0.2
        const val MAX_CAVERN_DEPTH = -3
    }

    fun generateMap(campaign: Campaign, name: String, height: Int, width: Int): KingdomMap {
        val map = KingdomMap(campaign, name)

        generateBaseTerrain(map, width, height)
        applyTerrainFeatures(map)

        return map
    }

    private fun generateBaseTerrain(map: KingdomMap, width: Int, height: Int) {
        val terrainParams = generateTerrainParameters(width, height)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val coordinate = HexCoordinate.fromXYZ(x, y, 0)
                terrainParams[coordinate]?.let { params ->
                    val terrain = determineTerrainType(params)
                    map.insert(Hex(terrain, map, coordinate))
                }
            }
        }
    }

    private fun applyTerrainFeatures(map: KingdomMap) {
        // 1. First, establish underground structures
        log.info("Generating underground features")
        generateBaseCaverns(map)     // Create base underground structure

        // 2. Generate major terrain features that affect elevation
        log.info("Generating mountain ranges and hill ranges")
        generateMountainRanges(map)  // Mountains should come first as they affect elevation
        log.info("Smoothing terrain transitions")
        smoothTerrain(map)           // Smooth terrain after mountains to create proper transitions

        // 3. Generate water features that affect terrain type
        log.info("Generating rivers")
        generateRivers(map)          // Rivers can modify terrain (create valleys, change desert to plain)
        log.info("Generating lakes")
        generateWaterBodies(map)     // Lakes should come after rivers

        // 4. Add terrain coverage based on climate and existing features
        log.info("Generating woodlands")
        generateForests(map)         // Forests/jungles should consider rivers and elevation

        // 5. Final smoothing pass to ensure consistency
        log.info("Finalizing terrain transitions")
        smoothTerrainBoundaries(map) // New function for final terrain transitions

        // 6. Add points of interest
        log.info("Adding points of interest")
        addTerrainFeatures(map)      // Resources, landmarks, lairs last

        // 7. Connect surface to underground
        log.info("Connecting surface to underground")
        connectCavernSystems(map)    // Connect lairs to caverns after all features are placed
    }

    private fun generateWaterBodies(map: KingdomMap) {
        // Generate lakes in appropriate locations
        map.hexes.filter { (_, hex) ->
            hex.rawTerrain == TerrainType.PLAIN ||
                    hex.rawTerrain == TerrainType.MARSH
        }.forEach { (coord, _) ->
            if (random.nextFloat() < 0.05) generateLake(map, coord)
        }
    }

    private fun addTerrainFeatures(map: KingdomMap) {
        generateLandmarks(map)
        generateResources(map)
        generateLairs(map)
        addBridges(map)
        addCavernFeatures(map)  // New function
    }

    private fun addCavernFeatures(map: KingdomMap) {
        map.hexes
            .filter { it.value.rawTerrain == TerrainType.CAVERN }
            .forEach { (_, hex) ->
                // Higher resource chance in caverns
                if (random.nextFloat() < 0.20) hex.terrainFeatures.add(TerrainFeature.RESOURCE)
                // Higher lair chance in caverns
                if (random.nextFloat() < 0.15) hex.terrainFeatures.add(TerrainFeature.LAIR)
            }
    }

    private fun generateTerrainParameters(width: Int, height: Int): Map<HexCoordinate, TerrainParams> =
        buildMap {
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val coordinate = HexCoordinate.fromXYZ(x, y, 0)
                    put(coordinate, generateParamsForCoordinate(x, y))
                }
            }
        }

    private fun generateParamsForCoordinate(x: Int, y: Int): TerrainParams {
        val coordinate = HexCoordinate.fromXYZ(x, y, 0)
        val elevation = noiseGenerator.noise(x * NOISE_SCALE, y * NOISE_SCALE)
        val moisture = noiseGenerator.noise(x * CLIMATE_SCALE + 1000, y * CLIMATE_SCALE + 1000)
        val temperature = noiseGenerator.noise(x * CLIMATE_SCALE + 2000, y * CLIMATE_SCALE + 2000)

        return TerrainParams(coordinate, elevation, moisture, temperature)
    }

    private fun determineTerrainType(params: TerrainParams): TerrainType =
        when {
            params.coordinate.z < 0 -> TerrainType.CAVERN  // Missing coordinate in TerrainParams
            params.elevation < DEEP_WATER_THRESHOLD -> TerrainType.WATER
            params.elevation < COASTAL_THRESHOLD -> determineCoastalTerrain(params)
            params.elevation < LOWLAND_THRESHOLD -> determineLowlandTerrain(params)
            params.elevation < HILL_THRESHOLD -> TerrainType.HILL
            else -> TerrainType.MOUNTAIN
        }

    private fun determineCoastalTerrain(params: TerrainParams): TerrainType =
        when {
            // Deep coastal waters
            params.elevation < (COASTAL_THRESHOLD - 0.05) -> TerrainType.WATER

            // Coastal zone
            params.moisture > HIGH_MOISTURE -> if (// Hot and wet coastal areas become mangrove-like marshes
                params.temperature > HIGH_TEMPERATURE) TerrainType.MARSH
                // Moderate to cool wet coasts become normal coastline
                else TerrainType.COASTLINE

            // Regular coastline for most cases
            params.moisture > MEDIUM_MOISTURE -> TerrainType.COASTLINE

            // Desert coastlines (less moisture)
            else -> if (// Hot dry coasts stay as coastline but might be modified later
                params.temperature > HIGH_TEMPERATURE) TerrainType.COASTLINE
                // Otherwise shallow water
                else TerrainType.WATER
        }

    private fun determineLowlandTerrain(params: TerrainParams): TerrainType =
        when {
            // Very wet environments
            params.moisture > HIGH_MOISTURE -> when {
                // Hot and wet = Jungle
                params.temperature > HIGH_TEMPERATURE -> TerrainType.JUNGLE
                // Warm and wet = Forest
                params.temperature > MEDIUM_TEMPERATURE -> TerrainType.FOREST
                // Cool and wet = Marsh
                else -> TerrainType.MARSH
            }

            // Moderately wet environments
            params.moisture > MEDIUM_MOISTURE -> when {
                // Hot = Forest (temperate/tropical)
                params.temperature > HIGH_TEMPERATURE -> TerrainType.FOREST
                // Moderate = Plain (grasslands)
                params.temperature > MEDIUM_TEMPERATURE -> TerrainType.PLAIN
                // Cool = Forest (taiga/boreal)
                else -> TerrainType.FOREST
            }

            // Dry environments
            else -> if (// Hot and dry = Desert
                params.temperature > MEDIUM_TEMPERATURE) TerrainType.DESERT
                // Cool and dry = Plain (steppes)
                else TerrainType.PLAIN
        }

    private fun smoothTerrain(map: KingdomMap) {
        // First validate underground hexes
        map.hexes.forEach { (coord, hex) ->
            if (coord.z < 0 && hex.rawTerrain != TerrainType.CAVERN) hex.rawTerrain = TerrainType.CAVERN
        }

        // First pass: Identify water-land boundaries and set coastlines
        map.hexes.forEach { (coord, hex) ->
            if (hex.rawTerrain == TerrainType.WATER) {
                val landNeighbors = getNeighborCoordinates(coord)
                    .mapNotNull { map.get(it) }
                    .filter { it.rawTerrain != TerrainType.WATER && it.rawTerrain != TerrainType.COASTLINE }

                if (landNeighbors.isNotEmpty()) hex.rawTerrain = TerrainType.COASTLINE
            }
        }

        // Second pass: General terrain smoothing
        map.hexes.forEach { (coord, hex) ->
            // Skip water and coastline in this pass
            if (hex.rawTerrain != TerrainType.WATER && hex.rawTerrain != TerrainType.COASTLINE) {
                val neighbors = getNeighborCoordinates(coord)
                    .mapNotNull { map.get(it) }
                    .map { it.terrain } // Use terrain instead of rawTerrain to get functional terrain type

                // If hex is completely isolated (no matching neighbors)
                if (neighbors.none { it == hex.terrain }) {

                    // Add forest/jungle smoothing in the plausibility check
                    val isPlausible = when (hex.terrain) {
                        TerrainType.FOREST -> neighbors.any { it == TerrainType.FOREST || it == TerrainType.PLAIN } ||
                            hex.hasRiver
                        TerrainType.JUNGLE -> (neighbors.any { it == TerrainType.JUNGLE || it == TerrainType.FOREST } &&
                            neighbors.any { it == TerrainType.MARSH || it == TerrainType.WATER }) ||
                            hex.hasRiver
                        TerrainType.HILL -> neighbors.any { it == TerrainType.PLAIN || it == TerrainType.MOUNTAIN }
                        TerrainType.MARSH -> neighbors.any { it == TerrainType.WATER || it == TerrainType.COASTLINE } ||
                            hex.hasRiver ||
                            neighbors.count { it == TerrainType.PLAIN } >= 2
                        TerrainType.MOUNTAIN -> neighbors.any { it == TerrainType.HILL }
                        TerrainType.DESERT -> neighbors.any { it == TerrainType.MOUNTAIN }
                        TerrainType.PLAIN -> true // Plains can exist anywhere
                        TerrainType.WATER, TerrainType.COASTLINE -> true // Handled in first pass
                        TerrainType.CAVERN -> true
                    }

                    if (!isPlausible) {

                        // Update terrain dominance weights
                        val dominantTerrain = neighbors
                            .filterNot { it == TerrainType.WATER || it == TerrainType.COASTLINE }
                            .groupBy { it }
                            .maxByOrNull { (terrain, group) ->
                                group.size * when (terrain) {
                                    TerrainType.PLAIN -> 1.2
                                    TerrainType.HILL -> 0.8
                                    TerrainType.MARSH -> 0.6
                                    TerrainType.MOUNTAIN -> 1.5
                                    TerrainType.DESERT -> 1.1
                                    TerrainType.FOREST -> 1.3
                                    TerrainType.JUNGLE -> 1.4
                                    else -> 0.0
                                }
                            }
                            ?.key

                        dominantTerrain?.let { newTerrain ->
                            hex.rawTerrain = when {
                                hex.rawTerrain == TerrainType.MOUNTAIN -> TerrainType.HILL
                                else -> newTerrain
                            }
                        }
                    }
                }
            }
        }

        // Third pass: Verify coastline consistency
        map.hexes.forEach { (coord, hex) ->
            if (hex.rawTerrain == TerrainType.COASTLINE) {
                val hasWaterNeighbor = getNeighborCoordinates(coord)
                    .mapNotNull { map.get(it) }
                    .any { it.rawTerrain == TerrainType.WATER }

                if (!hasWaterNeighbor) {
                    // Convert isolated coastline to most appropriate terrain
                    val neighborTerrains = getNeighborCoordinates(coord)
                        .mapNotNull { map.get(it) }
                        .map { it.terrain }
                        .filterNot { it == TerrainType.WATER || it == TerrainType.COASTLINE }

                    hex.rawTerrain = neighborTerrains
                        .groupBy { it }
                        .maxByOrNull { it.value.size }
                        ?.key ?: TerrainType.PLAIN
                }
            }
        }
    }

    private fun generateForests(map: KingdomMap) {
        // Find suitable starting points for forests
        val forestSeeds = map.hexes.filter { (_, hex) ->
            hex.rawTerrain == TerrainType.PLAIN &&
                    !hex.hasRiver &&
                    !hex.terrainFeatures.contains(TerrainFeature.LAKE)
        }

        forestSeeds.forEach { (coord, _) ->
            if (random.nextFloat() < FOREST_CHANCE) growForest(map, coord)
        }
    }

    private fun growForest(map: KingdomMap, start: HexCoordinate) {
        val forestHexes = mutableSetOf(start)
        val size = random.nextInt(MIN_FOREST_SIZE, MAX_FOREST_SIZE)
        val processed = mutableSetOf<HexCoordinate>()
        val toProcess = ArrayDeque<HexCoordinate>().apply { add(start) }

        while (forestHexes.size < size && toProcess.isNotEmpty()) {
            val current = toProcess.removeFirst()
            if (current in processed) continue
            processed.add(current)

            map.get(current)?.let { hex ->
                if (isValidForestHex(hex)) {
                    // Determine if this should be jungle based on neighbors and temperature
                    val shouldBeJungle = shouldBeJungle(map, current)
                    hex.rawTerrain = if (shouldBeJungle) TerrainType.JUNGLE else TerrainType.FOREST
                    forestHexes.add(current)

                    // Add neighbors to processing queue
                    getNeighborCoordinates(current)
                        .filterNot { it in processed }
                        .forEach { toProcess.add(it) }
                }
            }
        }
    }

    private fun isValidForestHex(hex: Hex): Boolean =
        hex.terrain == TerrainType.PLAIN && // Changed from rawTerrain
                !hex.hasRiver &&
                !hex.terrainFeatures.contains(TerrainFeature.LAKE)

    private fun shouldBeJungle(map: KingdomMap, coord: HexCoordinate): Boolean {
        // Check if near water or marsh (increases jungle likelihood)
        val nearWaterOrMarsh = getNeighborCoordinates(coord)
            .mapNotNull { map.get(it) }
            .any { it.rawTerrain in setOf(TerrainType.WATER, TerrainType.MARSH) }

        // Check if near existing jungle
        val nearJungle = getNeighborCoordinates(coord)
            .mapNotNull { map.get(it) }
            .any { it.rawTerrain == TerrainType.JUNGLE }

        return when {
            nearJungle -> random.nextFloat() < JUNGLE_CONVERSION_CHANCE
            nearWaterOrMarsh -> random.nextFloat() < JUNGLE_CONVERSION_CHANCE * 0.7
            else -> false
        }
    }

    private fun generateRivers(map: KingdomMap) {
        val riverSources = mutableSetOf<HexCoordinate>()
        val riverPaths = mutableSetOf<HexCoordinate>()

        map.hexes
            .filter { it.value.rawTerrain == TerrainType.MOUNTAIN }
            .forEach { (coord, _) ->
                if (random.nextFloat() < RIVER_START_CHANCE &&
                    !hasNearbyRiver(coord, riverSources, MIN_RIVER_SPACING)
                ) {
                    generateRiverPath(map, coord, riverPaths, riverSources)
                    riverSources.add(coord)
                }
            }
    }

    private fun generateRiverPath(
        map: KingdomMap,
        start: HexCoordinate,
        riverPaths: MutableSet<HexCoordinate>,
        riverSources: Set<HexCoordinate>
    ) {
        var current = start
        val visitedHexes = mutableSetOf<HexCoordinate>()

        while (true) {
            if (current in visitedHexes) break  // Prevent loops
            visitedHexes.add(current)

            val currentHex = map.get(current) ?: break
            if (currentHex.rawTerrain == TerrainType.WATER ||
                riverPaths.contains(current)
            ) break  // Stop at water or existing rivers

            val neighbors = getNeighborCoordinates(current)
                .mapNotNull { map.get(it) }
                .filter { neighbor ->
                    !riverSources.contains(neighbor.coordinate) &&  // Avoid other river sources
                            !riverPaths.contains(neighbor.coordinate) &&    // Avoid existing rivers
                            neighbor.rawTerrain != TerrainType.MOUNTAIN
                }

            if (neighbors.isEmpty()) break

            // Find lowest elevation neighbor
            val next = neighbors.minByOrNull {
                when (it.terrain) { // Changed from rawTerrain
                    TerrainType.WATER -> 0.0
                    TerrainType.MARSH -> 1.0
                    TerrainType.PLAIN -> 2.0
                    TerrainType.FOREST -> 2.5
                    TerrainType.JUNGLE -> 2.2
                    TerrainType.HILL -> 3.0
                    TerrainType.DESERT -> 2.8
                    else -> 4.0
                }
            } ?: break

            currentHex.terrainFeatures.add(TerrainFeature.RIVER)
            riverPaths.add(current)

            // Terrain modifications
            when (currentHex.rawTerrain) {
                TerrainType.PLAIN -> currentHex.rawTerrain = TerrainType.MARSH
                TerrainType.HILL -> currentHex.rawTerrain = TerrainType.PLAIN
                TerrainType.DESERT -> if (random.nextFloat() < 0.7) {
                    currentHex.rawTerrain = TerrainType.PLAIN
                }

                else -> {} // Keep existing terrain
            }

            current = next.coordinate
        }
    }

    private fun generateLake(map: KingdomMap, center: HexCoordinate) {
        val lakeHexes = mutableSetOf(center)
        val size = random.nextInt(MIN_LAKE_SIZE, MAX_LAKE_SIZE)

        repeat(size) { depth ->
            lakeHexes.toSet().forEach { coord ->
                getNeighborCoordinates(coord).forEach { neighbor ->
                    if (random.nextFloat() < LAKE_SPREAD_CHANCE(depth)) lakeHexes.add(neighbor)
                }
            }
        }

        lakeHexes.forEach { coord ->
            map.get(coord)?.terrainFeatures?.add(TerrainFeature.LAKE)
        }
    }

    private fun generateLandmarks(map: KingdomMap) {
        val eligibleHexes = map.hexes.filter {
            it.value.rawTerrain != TerrainType.WATER &&
                    it.value.terrainFeatures.none { f -> f == TerrainFeature.RIVER || f == TerrainFeature.LAKE }
        }

        eligibleHexes.forEach { (_, hex) ->
            if (random.nextFloat() < LANDMARK_CHANCE) hex.terrainFeatures.add(TerrainFeature.LANDMARK)
        }
    }

    private fun generateMountainRanges(map: KingdomMap) {
        val processedMountains = mutableSetOf<HexCoordinate>()
        val mountainSeeds = map.hexes
            .filter { it.value.rawTerrain == TerrainType.MOUNTAIN }
            .filter { it.key !in processedMountains }

        mountainSeeds.forEach { (coord, _) ->
            if (random.nextFloat() < MOUNTAIN_RANGE_CHANCE) generateMountainRange(map, coord, processedMountains)
        }
    }

    private fun generateMountainRange(
        map: KingdomMap,
        start: HexCoordinate,
        processedMountains: MutableSet<HexCoordinate>
    ) {
        val rangeLength = random.nextInt(MIN_MOUNTAIN_RANGE, MAX_MOUNTAIN_RANGE + 1)
        var current = start
        var direction = random.nextInt(6)

        repeat(rangeLength) { step ->
            processedMountains.add(current)

            // More natural direction changes
            if (random.nextFloat() < 0.3) direction = (direction + random.nextInt(-1, 2) + 6) % 6

            // Smaller chance for branches, but can create subsidiary ranges
            if (step > 1 && random.nextFloat() < MOUNTAIN_BRANCH_CHANCE) {
                val branchDirection = (direction + random.nextInt(2, 5)) % 6
                val branchLength = random.nextInt(2, 4) // Shorter branch ranges
                generateMountainBranch(map, current, branchDirection, branchLength, processedMountains)
            }

            current = current.getNeighborInDirection(direction)
            map.get(current)?.let { hex ->
                if (hex.rawTerrain != TerrainType.WATER) {
                    hex.rawTerrain = TerrainType.MOUNTAIN
                    addSurroundingHills(map, current)
                }
            }
        }
    }

    private fun generateMountainBranch(
        map: KingdomMap,
        start: HexCoordinate,
        direction: Int,
        length: Int,
        processedMountains: MutableSet<HexCoordinate>
    ) {
        var current = start
        var currentDirection = direction

        repeat(length) { step ->
            current = current.getNeighborInDirection(currentDirection)

            // Skip if this mountain was already processed
            if (current in processedMountains) return

            processedMountains.add(current)

            // Add mountain and surrounding hills if not water
            map.get(current)?.let { hex ->
                if (hex.rawTerrain != TerrainType.WATER) {
                    hex.rawTerrain = TerrainType.MOUNTAIN
                    addSurroundingHills(map, current)

                    // Slight direction changes for natural-looking branches
                    if (random.nextFloat() < DIRECTION_CHANGE_CHANCE) {
                        currentDirection = (currentDirection + random.nextInt(-1, 2) + 6) % 6
                    }
                } else
                    // Stop branch if we hit water
                    return
            }
        }
    }

    private fun addSurroundingHills(map: KingdomMap, center: HexCoordinate) {
        getNeighborCoordinates(center).forEach { neighbor ->
            map.get(neighbor)?.let { hex ->
                if (hex.rawTerrain != TerrainType.WATER &&
                    hex.rawTerrain != TerrainType.MOUNTAIN &&
                    random.nextFloat() < 0.6
                ) hex.rawTerrain = TerrainType.HILL
            }
        }
    }

    private fun addBridges(map: KingdomMap) {
        map.hexes.filter { it.value.terrainFeatures.contains(TerrainFeature.RIVER) }
            .forEach { (_, hex) ->
                val neighbors = getNeighborCoordinates(hex.coordinate)
                    .mapNotNull { map.get(it) }
                    .filter { neighbor ->
                        !neighbor.terrainFeatures.contains(TerrainFeature.RIVER) &&
                                (neighbor.terrainFeatures.contains(TerrainFeature.LANDMARK) ||
                                        neighbor.terrainFeatures.contains(TerrainFeature.RESOURCE) ||
                                        neighbor.rawTerrain == TerrainType.PLAIN)
                    }

                if (neighbors.isNotEmpty() && random.nextFloat() < BRIDGE_CHANCE) {
                    hex.terrainFeatures.add(TerrainFeature.BRIDGE)
                }
            }
    }

    private fun generateResources(map: KingdomMap) {
        map.hexes.forEach { (_, hex) ->
            if (random.nextFloat() < getResourceProbability(hex)) hex.terrainFeatures.add(TerrainFeature.RESOURCE)
        }
    }

    private fun getResourceProbability(hex: Hex): Double {
        return when (hex.rawTerrain) {
            TerrainType.CAVERN -> 0.20  // Add cavern resources
            TerrainType.MOUNTAIN -> 0.15
            TerrainType.HILL -> 0.1
            TerrainType.PLAIN -> 0.08
            TerrainType.MARSH -> 0.05
            TerrainType.FOREST -> 0.12  // Add forest resources
            TerrainType.JUNGLE -> 0.15  // Rich jungle resources
            TerrainType.DESERT -> 0.03  // Rare desert resources
            TerrainType.COASTLINE -> 0.08  // Coastal resources
            TerrainType.WATER -> 0.02   // Fishing grounds
        }
    }

    private fun generateLairs(map: KingdomMap) {
        val eligibleHexes = map.hexes.filter {
            it.value.rawTerrain != TerrainType.WATER &&
                    it.value.terrainFeatures.none { f -> f == TerrainFeature.LANDMARK || f == TerrainFeature.RIVER }
        }

        eligibleHexes.forEach { (coord, hex) ->
            if (random.nextFloat() < getLairProbability(hex)) {
                hex.terrainFeatures.add(TerrainFeature.LAIR)
                // Check if this lair connects to underground caverns
                if (random.nextFloat() < LAIR_TO_CAVERN_CHANCE) {
                    generateCavernSystem(map, coord.below)
                }
            }
        }
    }

    private fun getLairProbability(hex: Hex): Double {
        return when (hex.rawTerrain) {
            TerrainType.MOUNTAIN -> 0.08
            TerrainType.HILL -> 0.05
            TerrainType.MARSH -> 0.06
            else -> 0.02
        }
    }

    private fun getNeighborCoordinates(coord: HexCoordinate): List<HexCoordinate> {
        return listOf(
            coord.north, coord.northEast, coord.southEast,
            coord.south, coord.southWest, coord.northWest
        )
    }

    private fun hasNearbyRiver(coord: HexCoordinate, riverSources: Set<HexCoordinate>, range: Int): Boolean {
        for (r in -range..range) {
            for (q in -range..range) {
                val neighborCoord = HexCoordinate(coord.q + q, coord.r + r, 0)
                if (neighborCoord in riverSources) return true
            }
        }
        return false
    }

    private fun generateCavernSystem(map: KingdomMap, start: HexCoordinate) {
        if (start.z >= 0) return

        val processedCaverns = mutableSetOf<HexCoordinate>()
        val cavernQueue = ArrayDeque<HexCoordinate>().apply { add(start) }
        val connections = mutableSetOf<Pair<HexCoordinate, HexCoordinate>>()

        while (cavernQueue.isNotEmpty()) {
            val current = cavernQueue.removeFirst()
            if (current in processedCaverns || current.z < MAX_CAVERN_DEPTH) continue

            processedCaverns.add(current)
            map.update(current) { hex -> hex.rawTerrain = TerrainType.CAVERN }

            // Horizontal connections
            current.neighbors2D.forEach { neighbor ->
                if (random.nextFloat() < CAVERN_SPREAD_CHANCE) {
                    cavernQueue.add(neighbor.copy(z = current.z))
                    connections.add(current to neighbor.copy(z = current.z))
                }
            }

            // Vertical connections
            if (random.nextFloat() < CAVERN_DESCENT_CHANCE) {
                val below = current.below
                cavernQueue.add(below)
                connections.add(current to below)
            }
        }

        // Store connections for pathfinding/visualization
//        map.get(start)?.let { hex ->
//            hex.freetextHidden += connections.joinToString("|") {
//                "${it.first.q}:${it.first.r}:${it.first.z}-${it.second.q}:${it.second.r}:${it.second.z}"
//            }
//        }
    }

    private fun smoothTerrainBoundaries(map: KingdomMap) {
        // First pass: Identify problematic transitions
        val hexesToSmooth = map.hexes.filter { (coord, hex) ->
            hex.rawTerrain != TerrainType.WATER &&
                    hex.rawTerrain != TerrainType.COASTLINE &&
                    hex.rawTerrain != TerrainType.CAVERN &&
                    !isValidTerrainTransition(hex.terrain, getNeighborTerrains(map, coord))
        }

        // Second pass: Apply smoothing
        hexesToSmooth.forEach { (coord, hex) ->
            val neighbors = getNeighborTerrains(map, coord)
            smoothTerrainTransition(hex, neighbors)
        }
    }

    private fun isValidTerrainTransition(terrain: TerrainType, neighbors: List<TerrainType>): Boolean {
        return when (terrain) {
            TerrainType.MOUNTAIN -> neighbors.any { it == TerrainType.HILL }
            TerrainType.HILL -> neighbors.any { it == TerrainType.MOUNTAIN || it == TerrainType.PLAIN }
            TerrainType.DESERT -> neighbors.none { it == TerrainType.JUNGLE || it == TerrainType.MARSH }
            TerrainType.JUNGLE -> neighbors.any { it == TerrainType.FOREST || it == TerrainType.MARSH }
            TerrainType.MARSH -> neighbors.any { it == TerrainType.WATER || it == TerrainType.PLAIN }
            else -> true
        }
    }

    private fun smoothTerrainTransition(hex: Hex, neighbors: List<TerrainType>) {
        when (hex.terrain) {
            TerrainType.MOUNTAIN -> {
                if (!neighbors.any { it == TerrainType.HILL }) {
                    hex.rawTerrain = TerrainType.HILL
                }
            }

            TerrainType.HILL -> {
                if (!neighbors.any { it == TerrainType.MOUNTAIN || it == TerrainType.PLAIN }) {
                    hex.rawTerrain = TerrainType.PLAIN
                }
            }

            TerrainType.DESERT -> {
                if (neighbors.any { it == TerrainType.JUNGLE || it == TerrainType.MARSH }) {
                    hex.rawTerrain = TerrainType.PLAIN
                }
            }

            TerrainType.JUNGLE -> {
                if (!neighbors.any { it == TerrainType.FOREST || it == TerrainType.MARSH }) {
                    hex.rawTerrain = TerrainType.FOREST
                }
            }

            TerrainType.MARSH -> {
                if (!neighbors.any { it == TerrainType.WATER || it == TerrainType.PLAIN }) {
                    hex.rawTerrain = TerrainType.PLAIN
                }
            }

            else -> {} // Other terrain types don't need special smoothing
        }
    }

    private fun generateBaseCaverns(map: KingdomMap) {
        val cavernSeeds = mutableSetOf<HexCoordinate>()

        // Generate initial cavern seeds at each underground level
        for (z in -1 downTo MAX_CAVERN_DEPTH) {
            map.hexes
                .filter { it.key.z == z }
                .forEach { (coord, _) ->
                    if (random.nextFloat() < 0.3 && !cavernSeeds.any { seed -> getDistance(seed, coord) < 3 }
                    ) cavernSeeds.add(coord)
                }
        }

        // Grow cavern clusters from seeds
        cavernSeeds.forEach { seed ->
            val clusterSize = random.nextInt(3, 9)
            growCavernCluster(map, seed, clusterSize)
        }
    }

    private fun growCavernCluster(map: KingdomMap, start: HexCoordinate, size: Int) {
        if(start.z >= 0) return
        val cavernHexes = mutableSetOf<HexCoordinate>()
        val toProcess = ArrayDeque<HexCoordinate>().apply { add(start) }

        while (cavernHexes.size < size && toProcess.isNotEmpty()) {
            val current = toProcess.removeFirst()
            if (current in cavernHexes) continue

            map.update(current) { hex ->
                hex.rawTerrain = TerrainType.CAVERN
            }
            cavernHexes.add(current)

            // Add neighbors for processing
            current.neighbors2D
                .filter { it.z == current.z }
                .filterNot { it in cavernHexes }
                .forEach { toProcess.add(it) }
        }
    }

    private fun connectCavernSystems(map: KingdomMap) {
        // Find all cavern systems by level
        val cavernSystems = (-1 downTo MAX_CAVERN_DEPTH).associateWith { z -> findCavernSystems(map, z) }

        // Connect cavern systems vertically
        cavernSystems.entries.zipWithNext().forEach { (upper, lower) ->
            connectVerticalCavernSystems(map, upper.value, lower.value)
        }

        // Connect surface features to caverns
        map.hexes
            .filter { it.value.terrainFeatures.contains(TerrainFeature.LAIR) }
            .forEach { (coord, _) ->
                if (random.nextFloat() < LAIR_TO_CAVERN_CHANCE) {
                    ensureCavernConnection(map, coord)
                }
            }
    }

    private fun findCavernSystems(map: KingdomMap, z: Int): List<Set<HexCoordinate>> {
        val systems = mutableListOf<Set<HexCoordinate>>()
        val processed = mutableSetOf<HexCoordinate>()

        map.hexes
            .filter { it.key.z == z && it.value.rawTerrain == TerrainType.CAVERN }
            .forEach { (coord, _) ->
                if (coord !in processed) {
                    val system = findConnectedCaverns(map, coord)
                    systems.add(system)
                    processed.addAll(system)
                }
            }

        return systems
    }

    private fun ensureCavernConnection(map: KingdomMap, surfaceCoord: HexCoordinate) {
        var current = surfaceCoord
        while (current.z > MAX_CAVERN_DEPTH && current.z < 0) {
            current = current.below
            map.update(current) { hex ->
                hex.rawTerrain = TerrainType.CAVERN
            }

            // Create some horizontal connections at each level
            current.neighbors2D
                .filter { random.nextFloat() < CAVERN_SPREAD_CHANCE }
                .forEach { neighbor ->
                    map.update(neighbor.copy(z = current.z)) { hex ->
                        hex.rawTerrain = TerrainType.CAVERN
                    }
                }
        }
    }

    private fun getNeighborTerrains(map: KingdomMap, coord: HexCoordinate): List<TerrainType> =
        getNeighborCoordinates(coord)
            .mapNotNull { map.get(it) }
            .map { it.terrain }

    private fun findConnectedCaverns(map: KingdomMap, start: HexCoordinate): Set<HexCoordinate> {
        val connectedCaverns = mutableSetOf<HexCoordinate>()
        val toProcess = ArrayDeque<HexCoordinate>().apply { add(start) }

        while (toProcess.isNotEmpty()) {
            val current = toProcess.removeFirst()
            if (current in connectedCaverns) continue

            map.get(current)?.let { hex ->
                if (hex.rawTerrain == TerrainType.CAVERN) {
                    connectedCaverns.add(current)

                    // Add horizontal neighbors
                    current.neighbors2D
                        .filter { it.z == current.z }
                        .filterNot { it in connectedCaverns }
                        .forEach { toProcess.add(it) }

                    // Add vertical connections
                    listOf(current.above, current.below)
                        .filter { it.z in MAX_CAVERN_DEPTH..0 }
                        .filterNot { it in connectedCaverns }
                        .forEach { toProcess.add(it) }
                }
            }
        }

        return connectedCaverns
    }

    private fun connectVerticalCavernSystems(
        map: KingdomMap,
        upperSystems: List<Set<HexCoordinate>>,
        lowerSystems: List<Set<HexCoordinate>>
    ) {
        upperSystems.forEach { upperSystem ->
            // Find the closest lower system to connect to
            val nearestLower = lowerSystems.minByOrNull { lowerSystem ->
                upperSystem.minOf { upper ->
                    lowerSystem.minOf { lower ->
                        getDistance(upper, lower)
                    }
                }
            } ?: return@forEach

            // Find the closest points between systems
            val (upperPoint, lowerPoint) = findClosestPoints(upperSystem, nearestLower)

            // Create vertical connection
            createVerticalConnection(map, upperPoint, lowerPoint)
        }
    }

    private fun findClosestPoints(
        upperSystem: Set<HexCoordinate>,
        lowerSystem: Set<HexCoordinate>
    ): Pair<HexCoordinate, HexCoordinate> {
        var minDistance = Double.MAX_VALUE
        var closestUpper = upperSystem.first()
        var closestLower = lowerSystem.first()

        upperSystem.forEach { upper ->
            lowerSystem.forEach { lower ->
                val distance = getDistance(upper, lower)
                if (distance < minDistance) {
                    minDistance = distance
                    closestUpper = upper
                    closestLower = lower
                }
            }
        }

        return closestUpper to closestLower
    }

    private fun createVerticalConnection(
        map: KingdomMap,
        upper: HexCoordinate,
        lower: HexCoordinate
    ) {
        var current = upper
        while (current.z > lower.z) {
            current = current.below
            map.update(current) { hex -> hex.rawTerrain = TerrainType.CAVERN }

            // Add some horizontal variation
            if (random.nextFloat() < CAVERN_SPREAD_CHANCE) {
                val horizontalNeighbor = current.neighbors2D.random()
                map.update(horizontalNeighbor.copy(z = current.z)) { hex ->
                    hex.rawTerrain = TerrainType.CAVERN
                }
            }
        }
    }

    private fun getDistance(coord1: HexCoordinate, coord2: HexCoordinate): Double {
        val dx = (coord2.q - coord1.q).toDouble()
        val dy = (coord2.r - coord1.r).toDouble()
        val dz = (coord2.z - coord1.z).toDouble()

        // Use 3D distance formula with additional weight for vertical distance
        return sqrt(dx * dx + dy * dy + dz * dz * 4) // Vertical distance weighted more heavily
    }
}
