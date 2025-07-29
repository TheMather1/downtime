package pathfinder.web.frontend.dto.settlement

import pathfinder.domain.kingdom.settlement.District
import pathfinder.domain.kingdom.settlement.buildings.BorderBuildingType
import pathfinder.domain.kingdom.settlement.buildings.InfrastructureType
import pathfinder.domain.kingdom.settlement.buildings.Lot
import pathfinder.domain.support.coordinate.Coordinate
import pathfinder.domain.support.direction.Cardinal
import pathfinder.domain.support.direction.Orientation
import pathfinder.web.frontend.dto.svg.BorderPolygonElement
import pathfinder.web.frontend.dto.svg.BuildingRectElement
import pathfinder.web.frontend.dto.svg.DistrictRectElement
import pathfinder.web.frontend.dto.svg.LotRectElement
import pathfinder.web.frontend.dto.svg.StreetRectElement

class SettlementMapConfig(
    val lotWidth: Int = 100,
    val roadWidth: Int = 25,
    val borderWidth: Int = 25,
    val districts: Map<Coordinate, District>
) {
    val districtWidth = lotWidth * 6 + roadWidth * 4 + borderWidth * 2
    val mapOffsetX = (districts.maxOf { it.key.x } - districts.minOf { it.key.x } - 0.5) * districtWidth
    val mapOffsetY = (districts.maxOf { it.key.y } - districts.minOf { it.key.y } - 0.5) * districtWidth

    private val outerUpperLeft = 0.0 to 0.0
    private val innerUpperLeft = borderWidth.toDouble() to borderWidth.toDouble()
    private val outerUpperRight = districtWidth.toDouble() to 0.0
    private val innerUpperRight = districtWidth - borderWidth.toDouble() to borderWidth.toDouble()
    private val outerLowerLeft = 0.0 to districtWidth.toDouble()
    private val innerLowerLeft = borderWidth.toDouble() to districtWidth - borderWidth.toDouble()
    private val outerLowerRight = districtWidth.toDouble() to districtWidth.toDouble()
    private val innerLowerRight = districtWidth - borderWidth.toDouble() to districtWidth - borderWidth.toDouble()

    private val northBorder = setOf(outerUpperLeft, outerUpperRight, innerUpperRight, innerUpperLeft)
    private val eastBorder = setOf(innerUpperRight, outerUpperRight, outerLowerRight, innerLowerRight)
    private val westBorder = setOf(outerUpperLeft, innerUpperLeft, innerLowerLeft, outerLowerLeft)
    private val southBorder = setOf(innerLowerLeft, innerLowerRight, outerLowerRight, outerLowerLeft)

    val buildings = districts.flatMap { (dCoord, district) ->
        district.buildingLots.map { (building, lots) ->
            val height = lots.rangeX.width
            val width = lots.rangeY.width
            val orientation = when {
                lots.size != 2 -> null
                height > width -> Orientation.CardinalOrientation.HORIZONTAL
                else -> Orientation.CardinalOrientation.VERTICAL
            }
            val offsetX = district.lotOffsetX + lots.minOf { it.coordinate.x }.let { it * lotWidth + it / 2 * roadWidth }
            val offsetY = district.lotOffsetY + lots.minOf { it.coordinate.y }.let { it * lotWidth + it / 2 * roadWidth }
            BuildingRectElement(
                offsetX,
                offsetY,
                width.toDouble(),
                height.toDouble(),
                district.id,
                building.id,
                building.type,
                orientation
            )
        }
    }
    val emptyLots = districts.flatMap { (dCoord, district) ->
        district.emptyLots.map {
            val offsetX = district.lotOffsetX + it.coordinate.x * lotWidth + it.coordinate.x / 2 * roadWidth
            val offsetY = district.lotOffsetY + it.coordinate.y * lotWidth + it.coordinate.y / 2 * roadWidth
            LotRectElement(
                offsetX,
                offsetY,
                lotWidth.toDouble(),
                lotWidth.toDouble(),
                district.id,
                it.coordinate.x,
                it.coordinate.y
            )
        }
    }

    val streets = districts.map { (dCoord, district) ->
        StreetRectElement(
            district.innerOffsetX,
            district.innerOffsetY,
            (districtWidth - borderWidth * 2).toDouble(),
            (districtWidth - borderWidth * 2).toDouble(),
            district.id,
            district.getInfrastructure().any { it.type == InfrastructureType.PAVED_STREETS }
        )
    }

    val borders = districts.flatMap { (dCoord, district) ->
        listOf(
            BorderPolygonElement(
                Cardinal.NORTH,
                northBorder + (district.offsetX to district.offsetY),
                district.id,
                district.northBorder.type,
                district.northBorder.let {
                    if (it.facing == Cardinal.NORTH) it.getBuildings().firstNotNullOfOrNull { it.type.takeIf { it == BorderBuildingType.CITY_WALL } }
                    else it.getBuildings().firstNotNullOfOrNull { it.type.takeIf { it == BorderBuildingType.MOAT} }
                }
            ),
            BorderPolygonElement(
                Cardinal.EAST,
                eastBorder + (district.offsetX to district.offsetY),
                district.id,
                district.eastBorder.type,
                district.eastBorder.let {
                    if (it.facing == Cardinal.EAST) it.getBuildings().firstNotNullOfOrNull { it.type.takeIf { it == BorderBuildingType.CITY_WALL } }
                    else it.getBuildings().firstNotNullOfOrNull { it.type.takeIf { it == BorderBuildingType.MOAT} }
                }
            ),
            BorderPolygonElement(
                Cardinal.WEST,
                westBorder + (district.offsetX to district.offsetY),
                district.id,
                district.westBorder.type,
                district.westBorder.let {
                    if (it.facing == Cardinal.WEST) it.getBuildings().firstNotNullOfOrNull { it.type.takeIf { it == BorderBuildingType.CITY_WALL } }
                    else it.getBuildings().firstNotNullOfOrNull { it.type.takeIf { it == BorderBuildingType.MOAT} }
                }
            ),
            BorderPolygonElement(
                Cardinal.SOUTH,
                southBorder + (district.offsetX to district.offsetY),
                district.id,
                district.southBorder.type,
                district.southBorder.let {
                    if (it.facing == Cardinal.SOUTH) it.getBuildings().firstNotNullOfOrNull { it.type.takeIf { it == BorderBuildingType.CITY_WALL } }
                    else it.getBuildings().firstNotNullOfOrNull { it.type.takeIf { it == BorderBuildingType.MOAT} }
                }
            ),
        )
    }

    val Collection<Lot>.rangeX
        get() = minOf { it.coordinate.x }..maxOf { it.coordinate.x }
    val Collection<Lot>.rangeY
        get() = minOf { it.coordinate.y }..maxOf { it.coordinate.y }
    val IntRange.width
        get() = lotWidth * count() + roadWidth * matches(1..2, 3..4)
    fun IntRange.matches(vararg other: IntRange) = other.count { contains(it.first) && contains(it.last) }

    operator fun Pair<Double, Double>.plus(other: Pair<Double, Double>) = first + other.first to second + other.second
    operator fun Set<Pair<Double, Double>>.plus(other: Pair<Double, Double>) = map { it + other }.toSet()

    val District.offsetX
        get() = coordinate.x * districtWidth + borderWidth + roadWidth + mapOffsetX
    val District.offsetY
        get() = coordinate.y * districtWidth + borderWidth + roadWidth + mapOffsetY
    val District.innerOffsetX
        get() = offsetX + borderWidth
    val District.innerOffsetY
        get() = offsetY + borderWidth
    val District.lotOffsetX
        get() = innerOffsetX + roadWidth
    val District.lotOffsetY
        get() = innerOffsetY + roadWidth
}
