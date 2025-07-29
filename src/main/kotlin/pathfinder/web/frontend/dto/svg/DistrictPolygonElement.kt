package pathfinder.web.frontend.dto.svg

open class DistrictPolygonElement(
    coordinates: Set<Pair<Double, Double>>,
    val districtId: Long
) : SvgPolygonElement(coordinates)
