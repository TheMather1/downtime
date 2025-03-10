package pathfinder.domain.kingdom.terrain

import jakarta.persistence.*
import pathfinder.domain.Campaign
import pathfinder.domain.support.coordinate.Coordinate
import pathfinder.domain.support.coordinate.HexCoordinate
import pathfinder.domain.support.coordinate.HexCoordinateConverter
import pathfinder.web.frontend.dto.HexData

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

    val kingdoms
        get() = hexes.values.map { it.owner }.toSet()

    fun get(coordinate: HexCoordinate) =
        hexes[coordinate]

    fun insert(hex: Hex) {
        hexes[hex.coordinate] = hex
    }

    fun update(coordinate: HexCoordinate, transform: (hex: Hex) -> Unit) {
        transform(hexes.getOrPut(coordinate) { Hex(TerrainType.WATER, this, coordinate) })
    }

    val width
        get() = (hexes.maxOfOrNull { it.key.q + 1} ?: 0) - (hexes.minOfOrNull { it.key.q - 1 } ?: 0) + 1

    val oddQ
        get() = width.rem(2) == 1

    val height
        get() = (hexes.maxOfOrNull { it.key.oddQY + 1 } ?: 0) - (hexes.minOfOrNull { it.key.oddQY - 1} ?: 0) + 1

    val mapData
        get() = hexes.flatMap { (k, v) ->
            listOfNotNull(
                k.toCoordinate() to v.hexData,
                if (v.north == null) k.north.toCoordinate() to HexData(k.north.q, k.north.r) else null,
                if (v.northWest == null) k.northWest.toCoordinate() to HexData(k.northWest.q, k.northWest.r) else null,
                if (v.northEast == null) k.northEast.toCoordinate() to HexData(k.northEast.q, k.northEast.r) else null,
                if (v.south == null) k.south.toCoordinate() to HexData(k.south.q, k.south.r) else null,
                if (v.southWest == null) k.southWest.toCoordinate() to HexData(k.southWest.q, k.southWest.r) else null,
                if (v.southEast == null) k.southEast.toCoordinate() to HexData(k.southEast.q, k.southEast.r) else null
            )
        }.toMap().toMutableMap().also {
            if (it.isEmpty()) it[Coordinate(0,0)] = HexData(0,0)
        }.toMap()

    val offsetX
        get() = mapData.maxOfOrNull { 1.5 - it.key.x } ?: 0
    val offsetY
        get() = mapData.maxOfOrNull { 1.5 - it.key.y } ?: 0

    override fun compareTo(other: KingdomMap): Int {
        return name.compareTo(other.name).takeUnless { it == 0 } ?: id.compareTo(other.id)
    }
}
