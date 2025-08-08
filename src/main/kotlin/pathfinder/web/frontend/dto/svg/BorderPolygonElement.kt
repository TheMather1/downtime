package pathfinder.web.frontend.dto.svg

import pathfinder.domain.kingdom.settlement.DistrictBorder

open class BorderPolygonElement(
    val innerCoordinates: Set<Pair<Double, Double>>,
    val outerCoordinates: Set<Pair<Double, Double>>,
    val borderId: Long,
    val type: DistrictBorder.BorderType,
    val wall: Boolean,
    val moat: Boolean
) {
    val innerBorderType = when {
        wall -> "wall"
        type == DistrictBorder.BorderType.WATER -> "water"
        else -> "land"
    }
    val outerBorderType = when {
        moat -> "water"
        type == DistrictBorder.BorderType.WATER -> "water"
        else -> "land"
    }

    val innerPointString
        get() = innerCoordinates.joinToString(" ") { "${it.first},${it.second}" }
    val outerPointString
        get() = outerCoordinates.joinToString(" ") { "${it.first},${it.second}" }
}
