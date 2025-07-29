package pathfinder.web.frontend.dto.svg

open class SvgPolygonElement(
    val points: Set<Pair<Double, Double>>
) {
    val pointsString
        get() = points.joinToString(" ") { "${it.first},${it.second}" }
}
