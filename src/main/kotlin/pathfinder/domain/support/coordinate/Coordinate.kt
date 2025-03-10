package pathfinder.domain.support.coordinate

data class Coordinate(val x: Int, val y: Int) {
    private constructor(coordinates: List<String>) : this(coordinates[0].toInt(), coordinates[1].toInt())
    constructor(coordinate: String) : this(coordinate.split(':'))

    val north
        get() = Coordinate(x, y-1)
    val south
        get() = Coordinate(x, y+1)
    val east
        get() = Coordinate(x+1, y)
    val west
        get() = Coordinate(x-1, y)
    override fun toString() = "$x:$y"
}