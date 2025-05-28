package pathfinder.domain.support.coordinate

data class HexCoordinate(val q: Int, val r: Int, val z: Int) {
    private constructor(coords: List<String>): this(coords[0].toInt(), coords[1].toInt(), coords[2].toInt())
    constructor(axialKey: String) : this(axialKey.split(':'))
    val axialKey: String = "$q:$r:$z"
    val s = -q-r
    val oddQY = r + (q - (q and 1)) / 2
    val evenQY = r + (q + (q and 2)) / 2
    val oddRX = q + (r - (r and 1)) / 2
    val evenRX = q + (r + (r and 2)) / 2

    fun toCoordinate(flat: Boolean) = if(flat) Coordinate(q, oddQY) else Coordinate(oddRX, r)

    fun getNeighborInDirection(direction: Int): HexCoordinate = when (direction) {
        0 -> north
        1 -> northEast
        2 -> southEast
        3 -> south
        4 -> southWest
        else -> northWest
    }


    val north: HexCoordinate
        get() = HexCoordinate(q, r-1, z)
    val northWest: HexCoordinate
        get() = HexCoordinate(q-1, r, z)
    val northEast: HexCoordinate
        get() = HexCoordinate(q+1, r-1, z)
    val south: HexCoordinate
        get() = HexCoordinate(q, r+1, z)
    val southWest: HexCoordinate
        get() = HexCoordinate(q-1, r+1, z)
    val southEast: HexCoordinate
        get() = HexCoordinate(q+1, r, z)
    val above: HexCoordinate
        get() = HexCoordinate(q, r, z+1)
    val below: HexCoordinate
        get() = HexCoordinate(q, r, z-1)

    val neighbors2D: List<HexCoordinate>
        get() = listOf(
            north,
            northEast,
            southEast,
            south,
            southWest,
            northWest
        )

    companion object {
        fun fromXYZ(x: Int, y: Int, z: Int) = HexCoordinate(
            q = x,
            r = y - (x - (x and 1)) / 2,
            z = z
        )
    }
}
