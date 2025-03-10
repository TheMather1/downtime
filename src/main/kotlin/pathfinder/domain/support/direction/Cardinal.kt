package pathfinder.domain.support.direction

enum class Cardinal(override val orientation: Orientation): Direction<Cardinal> {
    NORTH(Orientation.CardinalOrientation.VERTICAL) {
        override val opposite = SOUTH
    },
    EAST(Orientation.CardinalOrientation.HORIZONTAL) {
        override val opposite = WEST
    },
    WEST(Orientation.CardinalOrientation.HORIZONTAL) {
        override val opposite = EAST
    },
    SOUTH(Orientation.CardinalOrientation.VERTICAL) {
        override val opposite = NORTH
    };

    override fun towards(cardinal: Cardinal) = this == cardinal
}