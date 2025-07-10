package pathfinder.domain.support.direction

enum class Cardinal(override val orientation: Orientation): Direction<Cardinal> {
    NORTH(Orientation.CardinalOrientation.VERTICAL) {
        override val opposite
            get() = SOUTH
    },
    EAST(Orientation.CardinalOrientation.HORIZONTAL) {
        override val opposite
            get() = WEST
    },
    WEST(Orientation.CardinalOrientation.HORIZONTAL) {
        override val opposite
            get() = EAST
    },
    SOUTH(Orientation.CardinalOrientation.VERTICAL) {
        override val opposite
            get() = NORTH
    };

    override fun towards(cardinal: Cardinal) = this == cardinal
}
