package pathfinder.domain.support.coordinate

data class HexCoordinate(val q: Int, val r: Int, val z: Int) {
    private constructor(coords: List<String>): this(coords[0].toInt(), coords[1].toInt(), coords[2].toInt())
    constructor(axialKey: String) : this(axialKey.split(':'))
    val axialKey: String = "$q:$r:$z"
    val s = -q-r
    val oddQY = r + (q - (q and 1)) / 2
    val evenQY = r + (q + (q and 2)) / 2

    fun effectiveY(offsetX: Int) = if (offsetX and 1 == 0) oddQY else evenQY

    fun toCoordinate() = Coordinate(q /*- offsetX*/, /*effectiveY(offsetX) - offsetY*/ oddQY)


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
}