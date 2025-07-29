package pathfinder.web.frontend.dto.svg

open class DistrictRectElement(
    x: Double,
    y: Double,
    width: Double,
    height: Double,
    val districtId: Long,
): SvgRectElement(x, y, width, height)
