package pathfinder.web.frontend.dto.svg

class StreetRectElement(
    x: Double,
    y: Double,
    width: Double,
    height: Double,
    districtId: Long,
    val paved: Boolean,
): DistrictRectElement(x, y, width, height, districtId)
