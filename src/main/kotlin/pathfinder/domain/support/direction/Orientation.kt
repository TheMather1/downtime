package pathfinder.domain.support.direction

sealed interface Orientation{
    enum class CardinalOrientation: Orientation {
        VERTICAL {
            override val perpendicular = HORIZONTAL
        },
        HORIZONTAL {
            override val perpendicular = VERTICAL
        };

        abstract val perpendicular: CardinalOrientation
    }
    enum class DiagonalOrientation: Orientation{
        DIAGONAL
    }
}