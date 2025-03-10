package pathfinder.domain.support.direction

enum class Diagonal: Direction<Diagonal> {
    NORTH_WEST {
        override val opposite = SOUTH_EAST
        override fun towards(cardinal: Cardinal) = cardinal == Cardinal.NORTH || cardinal == Cardinal.WEST
    },
    NORTH_EAST {
        override val opposite = SOUTH_WEST
        override fun towards(cardinal: Cardinal) = cardinal == Cardinal.NORTH || cardinal == Cardinal.EAST
    },
    SOUTH_WEST {
        override val opposite = NORTH_EAST
        override fun towards(cardinal: Cardinal) = cardinal == Cardinal.SOUTH || cardinal == Cardinal.WEST
    },
    SOUTH_EAST {
        override val opposite = NORTH_WEST
        override fun towards(cardinal: Cardinal) = cardinal == Cardinal.SOUTH || cardinal == Cardinal.EAST
    };

    override val orientation = Orientation.DiagonalOrientation.DIAGONAL
}