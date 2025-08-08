package pathfinder.domain.support.coordinate

import pathfinder.domain.support.direction.Cardinal

data class Coordinate(val x: Int, val y: Int) {
    private constructor(coordinates: List<String>) : this(coordinates[0].toInt(), coordinates[1].toInt())
    constructor(coordinate: String) : this(coordinate.split(':'))

    fun plusX(offset: Int) = Coordinate(x + offset, y)
    fun plusY(offset: Int) = Coordinate(x, y + offset)
    fun minusX(offset: Int) = Coordinate(x - offset, y)
    fun minusY(offset: Int) = Coordinate(x, y - offset)

    val north
        get() = minusY(1)
    val south
        get() = plusY(1)
    val east
        get() = plusX(1)
    val west
        get() = minusX(1)
    val neighbors
        get() = setOfNotNull(north, south, east, west)
    fun get(cardinal: Cardinal) = when (cardinal) {
        Cardinal.NORTH -> north
        Cardinal.EAST -> east
        Cardinal.SOUTH -> south
        Cardinal.WEST -> west
    }
    fun neighbors(other: Coordinate) = this in other.neighbors
    override fun toString() = "$x:$y"
}
