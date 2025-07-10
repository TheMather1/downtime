package pathfinder.domain.support.direction

sealed interface Orientation{
    enum class CardinalOrientation: Orientation {
        VERTICAL {
            override val perpendicular
                get() = HORIZONTAL
        },
        HORIZONTAL {
            override val perpendicular
                get() = VERTICAL
        };

        abstract val perpendicular: CardinalOrientation
    }
    enum class DiagonalOrientation: Orientation{
        DIAGONAL
    }
}
