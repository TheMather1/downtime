package pathfinder.web.frontend.dto.svg

class StreetRectElement(
    x: Double,
    y: Double,
    width: Double,
    height: Double,
    districtId: Long,
    val paved: Boolean,
    val streets: Set<SvgRectElement>,
    val emptyLots: Set<LotRectElement>,
    val buildings: Set<BuildingRectElement>
): DistrictRectElement(x, y, width, height, districtId)
