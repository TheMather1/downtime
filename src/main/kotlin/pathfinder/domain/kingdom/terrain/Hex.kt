package pathfinder.domain.kingdom.terrain

import jakarta.persistence.*
import pathfinder.domain.kingdom.Kingdom
import pathfinder.domain.kingdom.settlement.Settlement
import pathfinder.domain.kingdom.terrain.features.TerrainFeature
import pathfinder.domain.kingdom.terrain.features.TerrainFeature.*
import pathfinder.domain.kingdom.terrain.improvements.Improvement
import pathfinder.domain.kingdom.terrain.improvements.Improvement.*
import pathfinder.domain.support.coordinate.HexCoordinate
import pathfinder.domain.support.coordinate.HexCoordinateConverter
import pathfinder.web.frontend.dto.HexData

@Entity
data class Hex(
    @Enumerated(EnumType.STRING)
    var rawTerrain: TerrainType,
    @ManyToOne
    val map: KingdomMap,
    @Convert(converter = HexCoordinateConverter::class)
    val coordinate: HexCoordinate
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
    @ElementCollection
    @Enumerated(EnumType.STRING)
    val terrainFeatures: MutableSet<TerrainFeature> = mutableSetOf()
    @ElementCollection
    @Enumerated(EnumType.STRING)
    val improvements: MutableSet<Improvement> = mutableSetOf()
    @ManyToOne
    @JoinColumn(
        foreignKey = ForeignKey(
            foreignKeyDefinition = "FOREIGN KEY (owner_id) REFERENCES kingdom(id) ON DELETE SET NULL"
        )
    )
    var owner: Kingdom? = null
    @OneToOne(mappedBy = "hex", cascade = [CascadeType.ALL], orphanRemoval = true)
    var settlement: Settlement? = null
    var freetextVisible = ""
    var freetextHidden = ""


    val north: Hex?
        get() = map[coordinate.north]
    val northWest: Hex?
        get() = map[coordinate.northWest]
    val northEast: Hex?
        get() = map[coordinate.northEast]
    val south: Hex?
        get() = map[coordinate.south]
    val southWest: Hex?
        get() = map[coordinate.southWest]
    val southEast: Hex?
        get() = map[coordinate.southEast]
    val above: Hex?
        get() = map[coordinate.above]
    val below: Hex?
        get() = map[coordinate.below]

    val isOpaque: Boolean
        get() = TODO("Not yet implemented")
    val isPassable: Boolean
        get() = TODO("Not yet implemented")
    val movementCost: Double
        get() = (if(terrain == TerrainType.PLAIN) 5 else 8) / when {
            HIGHWAY in improvements -> terrain.highwayMovement
            ROAD in improvements -> terrain.roadMovement
            else -> terrain.movementMultiplier
        }

    val terrain: TerrainType
        get() = if (coastal) depthSearch()
            else rawTerrain

    private fun depthSearch(): TerrainType {
        var candidates = neighbors.filterNot { it.rawTerrain == TerrainType.WATER }.onEach {
            if (!it.coastal) return it.rawTerrain
        }
        val filter = mutableSetOf(this)
        while(candidates.isNotEmpty()) {
            filter += candidates
            candidates = candidates.map { it.depthSearch(filter) }.flatMap {
                if(it.second != null) return it.second!!
                it.first
            }
        }
        return rawTerrain
    }

    val coastal
        get() = rawTerrain == TerrainType.COASTLINE

    val hasRiver
        get() = RIVER in terrainFeatures || CANAL in improvements

    val hasWater
        get() = hasRiver || LAKE in terrainFeatures || SWAMP in terrainFeatures

    val hasRoad
        get() = ROAD in improvements || HIGHWAY in improvements

    val easyTransport
        get() = hasRiver || hasRoad

    private fun depthSearch(filter: Set<Hex>): Pair<Set<Hex>, TerrainType?> = neighbors.firstOrNull {
        !it.coastal && it.rawTerrain != TerrainType.WATER
    }?.let { emptySet<Hex>() to it.rawTerrain }
        ?: (neighbors.filterNot { it.rawTerrain == TerrainType.WATER }.toSet() - filter to null)

    val neighbors
        get() = setOfNotNull(north, northWest, northEast, south, southWest, southEast, above, below)

    val neighborCoordinates
        get() = mapOf(
            coordinate.north to north,
            coordinate.northWest to northWest,
            coordinate.northEast to northEast,
            coordinate.south to south,
            coordinate.southWest to southWest,
            coordinate.southEast to southEast
        )

    fun neighbors(filter: (hex: Hex) -> Boolean): Set<Hex> = neighbors.filter(filter).toSet()


    val earningsBonus
        get() = improvements.sumOf { improvement -> improvement.earningsBonus(this) }

    val consumptionBonus
        get() = improvements.sumOf { improvement -> improvement.consumptionBonus(this) }

    val economyBonus
        get() = improvements.sumOf { improvement -> improvement.economyBonus(this) } + if (RESOURCE in terrainFeatures) 1 else 0

    val loyaltyBonus
        get() = improvements.sumOf { improvement -> improvement.loyaltyBonus(this) } + when {
            LANDMARK !in terrainFeatures -> 0
            hasRoad -> 2
            else -> 1
        }

    val stabilityBonus
        get() = improvements.sumOf { improvement -> improvement.stabilityBonus(this) }

    val eligibleImprovements
        get() = Improvement.entries.filter { it.eligible(this) }.toSet()

    fun getEligibleImprovements(terrainType: TerrainType) = copy(rawTerrain = terrainType).eligibleImprovements

    val hexData
        get() = HexData(
            coordinate.q,
            coordinate.r,
            id,
            rawTerrain,
            eligibleImprovements,
            improvements,
            if(rawTerrain == TerrainType.WATER) setOf(RESOURCE) else TerrainFeature.entries.toSet(),
            terrainFeatures,
            if(hasRoad) setOfNotNull(
                if (north?.hasRoad == true) 1 else null,
                if (northEast?.hasRoad == true) 2 else null,
                if (southEast?.hasRoad == true) 4 else null,
                if (south?.hasRoad == true) 8 else null,
                if (southWest?.hasRoad == true) 16 else null,
                if (northWest?.hasRoad == true) 32 else null
            ).sum() else 0,
            if(hasRiver) setOfNotNull(
                if (north?.hasRiver == true || north?.rawTerrain == TerrainType.WATER) 1 else null,
                if (northEast?.hasRiver == true || north?.rawTerrain == TerrainType.WATER) 2 else null,
                if (southEast?.hasRiver == true || north?.rawTerrain == TerrainType.WATER) 4 else null,
                if (south?.hasRiver == true || north?.rawTerrain == TerrainType.WATER) 8 else null,
                if (southWest?.hasRiver == true || north?.rawTerrain == TerrainType.WATER) 16 else null,
                if (northWest?.hasRiver == true || north?.rawTerrain == TerrainType.WATER) 32 else null
            ).sum() else 0,
            freetextVisible,
            freetextHidden,
            settlement?.settlementData
        )

    val tooltip: String
        get() {
            var string = "[${coordinate.axialKey}] ${rawTerrain.displayName}"
            owner?.let { string += "\nOwned by: ${it.name}" }
            if (settlement != null) string += "\nSettlement: ${settlement!!.name} - ${settlement!!.type.displayName}"
            if (terrainFeatures.isNotEmpty()) string += "\nTerrain features: ${terrainFeatures.joinToString(", ") { it.displayName}}"
            if (improvements.isNotEmpty()) string += "\nImprovements: ${improvements.joinToString(", ") { it.displayName }}"
            if (freetextVisible.isNotBlank()) string += "\n\nNotes:\n$freetextVisible"
            return string
        }
}
