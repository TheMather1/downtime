package pathfinder.domain.kingdom.terrain

import jakarta.persistence.*
import org.hibernate.annotations.Formula
import pathfinder.domain.Campaign
import pathfinder.domain.support.coordinate.Coordinate
import pathfinder.domain.support.coordinate.HexCoordinate
import pathfinder.domain.support.coordinate.HexCoordinateConverter
import pathfinder.web.FLAT_TOP
import pathfinder.web.frontend.dto.HexData
import pathfinder.web.frontend.support.RiverTracer
import pathfinder.web.frontend.support.RoadTracer

@Entity
class KingdomMap(
    @ManyToOne
    val campaign: Campaign,
    val name: String,
): Comparable<KingdomMap> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long = 0
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @MapKey(name = "coordinate")
    @Convert(converter = HexCoordinateConverter::class)
    val hexes: MutableMap<HexCoordinate, Hex> = mutableMapOf()

    @Formula("(SELECT COUNT(*) FROM hex WHERE hex.map_id = id)")
    val size = 0

    @Formula("(SELECT COUNT(*) FROM settlement s JOIN hex h ON s.hex_id = h.id WHERE h.map_id = id)")
    val settlementCount = 0

    @get:Transient
    val settlements
        get() = hexes.values.mapNotNull { it.settlement }.toSet()

    operator fun get(coordinate: HexCoordinate) =
        hexes[coordinate]

    fun insert(hex: Hex) {
        hexes[hex.coordinate] = hex
    }

    fun update(coordinate: HexCoordinate, transform: (hex: Hex) -> Unit) {
        transform(hexes.getOrPut(coordinate) { Hex(TerrainType.WATER, this, coordinate) })
    }

    val mapData
        get() = hexes.filter { it.key.z == 0 }.flatMap { (k, v) ->
            listOfNotNull(
                k.toCoordinate(FLAT_TOP) to v.hexData,
                if (v.north == null) k.north.toCoordinate(FLAT_TOP) to HexData(k.north.q, k.north.r) else null,
                if (v.northWest == null) k.northWest.toCoordinate(FLAT_TOP) to HexData(k.northWest.q, k.northWest.r) else null,
                if (v.northEast == null) k.northEast.toCoordinate(FLAT_TOP) to HexData(k.northEast.q, k.northEast.r) else null,
                if (v.south == null) k.south.toCoordinate(FLAT_TOP) to HexData(k.south.q, k.south.r) else null,
                if (v.southWest == null) k.southWest.toCoordinate(FLAT_TOP) to HexData(k.southWest.q, k.southWest.r) else null,
                if (v.southEast == null) k.southEast.toCoordinate(FLAT_TOP) to HexData(k.southEast.q, k.southEast.r) else null
            )
        }.toMap().toMutableMap().also {
            if (it.isEmpty()) it[Coordinate(0,0)] = HexData(0,0)
        }.toMap()

    val hexesAndNeighbors
        get() = hexes.takeUnless { it.isEmpty() }?.map { (k, v) ->
            v.neighborCoordinates + (k to v)
        }?.reduce { a, b -> a + b } ?: mapOf(HexCoordinate(0,0,0) to null)

    fun rivers(z: Int) = RiverTracer(this, z).findRivers()

    fun roads(z: Int) = RoadTracer(this, z).findRoads()

    val offsetX
        get() = mapData.maxOfOrNull { 1.5 - it.key.x } ?: 0.0
    val offsetY
        get() = mapData.maxOfOrNull { 1.5 - it.key.y } ?: 0.0

    override fun compareTo(other: KingdomMap): Int {
        return name.compareTo(other.name).takeUnless { it == 0 } ?: id.compareTo(other.id)
    }
}
