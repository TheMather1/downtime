package pathfinder.web.frontend.dto.svg

import pathfinder.domain.kingdom.settlement.buildings.LotBuildingType
import pathfinder.domain.support.direction.Orientation

class BuildingRectElement(
    x: Double,
    y: Double,
    width: Double,
    height: Double,
    districtId: Long,
    val building: Long,
    val buildingType: LotBuildingType,
    val lots: Set<LotRectElement>,
    orientation: Orientation.CardinalOrientation?
): DistrictRectElement(x, y, width, height, districtId) {
    val rotation = if (orientation == Orientation.CardinalOrientation.VERTICAL) 90 else 0
    override fun toString() = "{buildingId: $building, districtId: $districtId, lots: [${lots.joinToString()}]}"
}
