package pathfinder.web.frontend.dto.settlement

import pathfinder.domain.support.coordinate.Coordinate
import pathfinder.domain.support.direction.Orientation.CardinalOrientation
import pathfinder.domain.support.direction.Orientation.CardinalOrientation.HORIZONTAL
import pathfinder.domain.support.direction.Orientation.CardinalOrientation.VERTICAL

data class BorderLocation(
    val coordinate: Coordinate,
    val orientation: CardinalOrientation
) {
    fun getConnectedCoordinates(): Pair<Coordinate, Coordinate> = when(orientation) {
        VERTICAL -> Pair(
            coordinate,
            coordinate.copy(x = coordinate.x + 1)
        )
        HORIZONTAL -> Pair(
            coordinate,
            coordinate.copy(y = coordinate.y + 1)
        )
    }
}
