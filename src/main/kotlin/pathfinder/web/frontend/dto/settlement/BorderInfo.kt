package pathfinder.web.frontend.dto.settlement

import pathfinder.domain.kingdom.settlement.DistrictBorder
import pathfinder.domain.kingdom.settlement.buildings.BorderBuildingType

data class BorderInfo(
    val id: Long,
    val type: DistrictBorder.BorderType,
    val buildings: Set<BorderBuildingType>
)
