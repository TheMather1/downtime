package pathfinder.domain.kingdom.settlement

import jakarta.persistence.*
import pathfinder.domain.kingdom.settlement.buildings.Building
import pathfinder.domain.kingdom.settlement.buildings.Infrastructure
import pathfinder.domain.kingdom.settlement.buildings.Lot
import pathfinder.domain.kingdom.settlement.buildings.LotBuilding
import pathfinder.domain.kingdom.settlement.buildings.LotBuildingType
import pathfinder.domain.kingdom.settlement.buildings.LotBuildingType.BuildingSize.*
import pathfinder.domain.support.direction.Cardinal
import pathfinder.domain.support.direction.Cardinal.*
import pathfinder.domain.support.coordinate.Coordinate
import pathfinder.domain.support.coordinate.CoordinateConverter

@Entity
class District(
    @Convert(converter = CoordinateConverter::class)
    val coordinate: Coordinate,
    @ManyToOne
    val settlement: Settlement,
    northBorder: HorizontalBorder? = null,
    eastBorder: VerticalBorder? = null,
    southBorder: HorizontalBorder? = null,
    westBorder: VerticalBorder? = null
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "south")
    val northBorder: HorizontalBorder = northBorder?.also {
        if (it.facing != SOUTH) throw IllegalArgumentException("Existing border to the north must face south.")
        it.south = this
    } ?: HorizontalBorder(facing = NORTH, south = this)

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "west")
    val eastBorder: VerticalBorder = eastBorder?.also {
        if (it.facing != WEST) throw IllegalArgumentException("Existing border to the east must face west.")
        it.west = this
    } ?: VerticalBorder(facing = EAST, west = this)

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "north")
    val southBorder: HorizontalBorder = southBorder?.also {
        if (it.facing != NORTH) throw IllegalArgumentException("Existing border to the south must face north.")
        it.north = this
    } ?: HorizontalBorder(facing = SOUTH, north = this)

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "east")
    val westBorder: VerticalBorder = westBorder?.also {
        if (it.facing != EAST) throw IllegalArgumentException("Existing border to the west must face east.")
        it.east = this
    } ?: VerticalBorder(facing = WEST, east = this)

    @Convert(converter = CoordinateConverter::class)
    @MapKey(name = "coordinate")
    @OneToMany(mappedBy = "district", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val buildingMap = (0..5).flatMap { x ->
        (0..5).map { y -> Coordinate(x, y) }
    }.associateWith { Lot(it, this) }

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    private val infrastructure = mutableSetOf<Infrastructure>()
    fun getInfrastructure() = infrastructure.toSet()

    val buildings: Set<Building<*>>
        get() = (buildingMap.values.flatMap { it.upgrades + it.building }.filterNotNull() + infrastructure +
                northBorder.getBuildings() + eastBorder.getBuildings() + southBorder.getBuildings() + westBorder.getBuildings()).toSet()

    fun waterway(direction: Cardinal) = buildingMap.any {
        when (direction) {
            NORTH -> it.key.y == 0
            WEST -> it.key.x == 0
            SOUTH -> it.key.y == 5
            EAST -> it.key.x == 5
        } && it.value.building?.type == LotBuildingType.WATERWAY
    }

    fun get(coordinate: Coordinate): LotBuilding? {
        coordinate.assertBounds()
        return buildingMap[coordinate]!!.building
    }

    fun set(coordinates: List<Coordinate>, building: LotBuildingType) {
        val lots = coordinates.map {
            it.assertBounds()
            buildingMap[it]!!
        }
        if (coordinates.maxOf { it.x } - coordinates.minOf { it.x } > 1 || coordinates.maxOf { it.y } - coordinates.minOf { it.y } > 1)
            throw IllegalArgumentException("Defined space is not contiguous")
        val buildings = lots.mapNotNull { it.building }.distinct()
        when {
            buildings.size > 1 -> throw IllegalArgumentException("Multiple buildings in defined space.")
            buildings.firstOrNull()?.type?.upgradesTo(building) == false -> throw IllegalArgumentException("${buildings.first().type.name} building cannot be upgraded into ${building.name}.")
            building.size.isSize(when(coordinates.size) {
                1 -> NORMAL
                2 -> LARGE
                4 -> HUGE
                else -> throw IllegalArgumentException("Defined space is not a valid building size.")
            }) -> {
                val building = LotBuilding(building)
                coordinates.forEach { buildingMap[it]!!.building = building }
            }
            else -> throw IllegalArgumentException("Defined space is of invalid size for a building.")
        }
    }

    fun remove(coordinates: List<Coordinate>) {
        coordinates.forEach { it.assertBounds() }
        coordinates.groupBy { buildingMap[it]!!.building }.forEach { (building, coordinate) ->
            if(when (building?.type?.size) {
                null -> false
                NORMAL -> coordinate.size != 1
                NORMAL_OR_LARGE -> coordinate.size !in 1..2
                LARGE -> coordinate.size != 2
                HUGE -> coordinate.size != 4
            }) throw IllegalArgumentException("Defined space contains partial buildings.")
        }
        coordinates.forEach { buildingMap[it]!!.building = null }
    }

    fun Coordinate.assertBounds() = if(coordinate.x !in 0..5 && coordinate.y !in 0..5)
        throw IllegalArgumentException("Coordinate $coordinate out of bounds.")
        else true
}