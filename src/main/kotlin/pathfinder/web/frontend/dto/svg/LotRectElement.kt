package pathfinder.web.frontend.dto.svg

class LotRectElement(
    x: Double,
    y: Double,
    width: Double,
    height: Double,
    districtId: Long,
    val lotX: Int,
    val lotY: Int
): DistrictRectElement(x, y, width, height, districtId)
