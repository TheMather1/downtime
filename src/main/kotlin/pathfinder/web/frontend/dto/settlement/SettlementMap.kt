package pathfinder.web.frontend.dto.settlement

import DistrictNode
import pathfinder.domain.kingdom.settlement.District
import pathfinder.domain.kingdom.settlement.DistrictBorder
import pathfinder.domain.support.coordinate.Coordinate
import pathfinder.domain.support.direction.Orientation.CardinalOrientation.HORIZONTAL
import pathfinder.domain.support.direction.Orientation.CardinalOrientation.VERTICAL

class SettlementMap(
    val districts: Map<Coordinate, DistrictNode>,
    val borders: Map<BorderLocation, BorderInfo>
) {

    constructor(districts: List<District>): this(
        districts.associate { district ->
            district.coordinate to DistrictNode(district)
        },
        buildMap {
            districts.forEach { district ->
                BorderLocation(district.coordinate, HORIZONTAL).takeUnless { containsKey(it) }?.let {
                    put(it, district.northBorder.toBorderInfo())
                }
                BorderLocation(district.coordinate.south, HORIZONTAL).takeUnless { containsKey(it) }?.let {
                    put(it, district.southBorder.toBorderInfo())
                }
                BorderLocation(district.coordinate, VERTICAL).takeUnless { containsKey(it) }?.let {
                    put(it, district.eastBorder.toBorderInfo())
                }
                BorderLocation(district.coordinate.west, VERTICAL).takeUnless { containsKey(it) }?.let {
                    put(it, district.westBorder.toBorderInfo())
                }
            }
        }
    )

    companion object {
        private fun DistrictBorder.toBorderInfo(): BorderInfo = BorderInfo(
            id = id,
            type = type,
            buildings = getBuildings().map { it.type }.toSet()
        )
    }
}
