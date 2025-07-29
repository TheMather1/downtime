package pathfinder.web.frontend.dto.svg

import pathfinder.domain.kingdom.settlement.DistrictBorder
import pathfinder.domain.kingdom.settlement.buildings.BorderBuildingType
import pathfinder.domain.support.direction.Cardinal

open class BorderPolygonElement(
    side: Cardinal,
    coordinates: Set<Pair<Double, Double>>,
    districtId: Long,
    val type: DistrictBorder.BorderType,
    val building: BorderBuildingType?
) : DistrictPolygonElement(coordinates, districtId) {
    val borderType = when {
        building == BorderBuildingType.CITY_WALL -> "wall"
        building == BorderBuildingType.MOAT -> "water"
        type == DistrictBorder.BorderType.WATER -> "water"
        else -> "land"
    }
}
