import pathfinder.domain.kingdom.settlement.District
import pathfinder.domain.kingdom.settlement.buildings.CustomBuildingType
import pathfinder.domain.kingdom.settlement.buildings.InfrastructureType
import pathfinder.domain.kingdom.settlement.buildings.LotBuildingType
import pathfinder.domain.kingdom.settlement.buildings.UpgradeType
import pathfinder.domain.support.coordinate.Coordinate

data class DistrictNode(
    val id: Long,
    val coordinate: Coordinate,
    val settlementId: Long,
    val lots: Map<Coordinate, LotInfo>,
    val infrastructure: Set<InfrastructureInfo>
) {
    constructor(district: District) : this(
        id = district.id,
        coordinate = district.coordinate,
        settlementId = district.settlement.id,
        lots = district.getBuildingMap().mapValues { (coord, lot) ->
            LotInfo(
                coordinate = coord,
                building = lot.building?.let {
                    LotBuildingInfo(it.type, it.customization)
                },
                upgrades = lot.upgrades.mapTo(mutableSetOf()) {
                    UpgradeInfo(it.type, it.customization)
                }
            )
        },
        infrastructure = district.getInfrastructure().mapTo(mutableSetOf()) { infra ->
            InfrastructureInfo(
                type = infra.type,
                properties = infra.customization
            )
        }
    )
    data class LotInfo(
        val coordinate: Coordinate,
        val building: LotBuildingInfo?,
        val upgrades: Set<UpgradeInfo>
    )

    data class LotBuildingInfo(
        val type: LotBuildingType,
        val custom: CustomBuildingType?
    )

    data class UpgradeInfo(
        val type: UpgradeType,
        val custom: CustomBuildingType?
    )

    data class InfrastructureInfo(
        val type: InfrastructureType,
        val properties: CustomBuildingType?
    )
}
