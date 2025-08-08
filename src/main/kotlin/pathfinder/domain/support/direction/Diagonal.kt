package pathfinder.domain.support.direction

enum class Diagonal: Direction<Diagonal> {
    NORTH_WEST {
        override val opposite
            get() = SOUTH_EAST
        override fun towards(cardinal: Cardinal) = cardinal == Cardinal.NORTH || cardinal == Cardinal.WEST
    },
    NORTH_EAST {
        override val opposite
            get() = SOUTH_WEST
        override fun towards(cardinal: Cardinal) = cardinal == Cardinal.NORTH || cardinal == Cardinal.EAST
    },
    SOUTH_WEST {
        override val opposite
            get() = NORTH_EAST
        override fun towards(cardinal: Cardinal) = cardinal == Cardinal.SOUTH || cardinal == Cardinal.WEST
    },
    SOUTH_EAST {
        override val opposite
            get() = NORTH_WEST
        override fun towards(cardinal: Cardinal) = cardinal == Cardinal.SOUTH || cardinal == Cardinal.EAST
    };

    override val displayName = name.lowercase().replaceFirstChar { it.uppercase() }

    override val orientation = Orientation.DiagonalOrientation.DIAGONAL
}
