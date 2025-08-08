package pathfinder.web.frontend.dto.settlement

import pathfinder.domain.kingdom.settlement.District
import pathfinder.domain.kingdom.settlement.DistrictBorder
import pathfinder.domain.kingdom.settlement.buildings.InfrastructureType
import pathfinder.domain.kingdom.settlement.buildings.Lot
import pathfinder.domain.support.coordinate.Coordinate
import pathfinder.domain.support.direction.Cardinal
import pathfinder.domain.support.direction.Orientation
import pathfinder.web.frontend.dto.svg.*

class SettlementMapConfig(
    val lotWidth: Int = 100,
    val roadWidth: Int = 25,
    val borderWidth: Int = 25,
    val districts: Map<Coordinate, District>
) {
    val innerDistrictWidth = lotWidth * 6 + roadWidth * 4
    val districtWidth = innerDistrictWidth + borderWidth * 2
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

    val neighborDistricts = districts.entries.fold(mutableMapOf<Coordinate, DistrictBorder.BorderType>()) { neighbors, (districtCoord, district) ->
        if (districtCoord.north !in districts && districtCoord.north !in neighbors) neighbors[districtCoord.north] = district.northBorder.type
        if (districtCoord.east !in districts && districtCoord.east !in neighbors) neighbors[districtCoord.east] = district.eastBorder.type
        if (districtCoord.south !in districts && districtCoord.south !in neighbors) neighbors[districtCoord.south] = district.southBorder.type
        if (districtCoord.west !in districts && districtCoord.west !in neighbors) neighbors[districtCoord.west] = district.westBorder.type
        neighbors
    }.map { (coord, type) ->
        TerrainRectElement(
            coord.offsetX,
            coord.offsetY,
            districtWidth.toDouble(),
            districtWidth.toDouble(),
            coord.x,
            coord.y,
            type.name.lowercase()
        )
    }

    val buildings = districts.flatMap { (dCoord, district) ->
        district.buildingLots.map { (building, lots) ->
            val height = lots.rangeY.width
            val width = lots.rangeX.width
            val orientation = when {
                lots.size != 2 -> null
                height > width -> Orientation.CardinalOrientation.HORIZONTAL
                else -> Orientation.CardinalOrientation.VERTICAL
            }
            val offsetX = dCoord.lotOffsetX + lots.minOf { it.coordinate.x }.let { it * lotWidth + it / 2 * roadWidth }
            val offsetY = dCoord.lotOffsetY + lots.minOf { it.coordinate.y }.let { it * lotWidth + it / 2 * roadWidth }
            BuildingRectElement(
                offsetX,
                offsetY,
                width.toDouble(),
                height.toDouble(),
                district.id,
                building.id,
                building.type,
                lots.map {
                    val offsetX = dCoord.lotOffsetX + it.coordinate.x * lotWidth + it.coordinate.x / 2 * roadWidth
                    val offsetY = dCoord.lotOffsetY + it.coordinate.y * lotWidth + it.coordinate.y / 2 * roadWidth
                    LotRectElement(
                        offsetX,
                        offsetY,
                        lotWidth.toDouble(),
                        lotWidth.toDouble(),
                        district.id,
                        it.coordinate.x,
                        it.coordinate.y,
                        it.id
                    )
                }.toSet(),
                orientation
            )
        }
    }
    val emptyLots = districts.flatMap { (dCoord, district) ->
        district.emptyLots.map {
            val offsetX = dCoord.lotOffsetX + it.coordinate.x * lotWidth + it.coordinate.x / 2 * roadWidth
            val offsetY = dCoord.lotOffsetY + it.coordinate.y * lotWidth + it.coordinate.y / 2 * roadWidth
            LotRectElement(
                offsetX,
                offsetY,
                lotWidth.toDouble(),
                lotWidth.toDouble(),
                district.id,
                it.coordinate.x,
                it.coordinate.y,
                it.id
            )
        }
    }

    val districtElements = districts.map { (dCoord, district) ->
        StreetRectElement(
            dCoord.innerOffsetX,
            dCoord.innerOffsetY,
            (districtWidth - borderWidth * 2).toDouble(),
            (districtWidth - borderWidth * 2).toDouble(),
            district.id,
            district.getInfrastructure().any { it.type == InfrastructureType.PAVED_STREETS },
            setOf(
                SvgRectElement(dCoord.innerOffsetX, dCoord.innerOffsetY, roadWidth.toDouble(), innerDistrictWidth.toDouble()),
                SvgRectElement(dCoord.innerOffsetX + roadWidth + lotWidth * 2, dCoord.innerOffsetY, roadWidth.toDouble(), innerDistrictWidth.toDouble()),
                SvgRectElement(dCoord.innerOffsetX + roadWidth * 2 + lotWidth * 4, dCoord.innerOffsetY, roadWidth.toDouble(), innerDistrictWidth.toDouble()),
                SvgRectElement(dCoord.innerOffsetX + innerDistrictWidth - roadWidth, dCoord.innerOffsetY, roadWidth.toDouble(), innerDistrictWidth.toDouble()),
                SvgRectElement(dCoord.innerOffsetX, dCoord.innerOffsetY, innerDistrictWidth.toDouble(), roadWidth.toDouble()),
                SvgRectElement(dCoord.innerOffsetX, dCoord.innerOffsetY + roadWidth + lotWidth * 2, innerDistrictWidth.toDouble(), roadWidth.toDouble()),
                SvgRectElement(dCoord.innerOffsetX, dCoord.innerOffsetY + roadWidth * 2 + lotWidth * 4, innerDistrictWidth.toDouble(), roadWidth.toDouble()),
                SvgRectElement(dCoord.innerOffsetX, dCoord.innerOffsetY + innerDistrictWidth - roadWidth, innerDistrictWidth.toDouble(), roadWidth.toDouble()),
            ),
            emptyLots.filter { it.districtId == district.id }.toSet(),
            buildings.filter { it.districtId == district.id }.toSet()
        )
    }

    val borders = districts.flatMap { (dCoord, district) ->
        listOfNotNull(
            if (district.northBorder.facing == Cardinal.NORTH) BorderPolygonElement(
                northBorder + (dCoord.offsetX to dCoord.offsetY),
                southBorder + (dCoord.north.offsetX to dCoord.north.offsetY),
                district.northBorder.id,
                district.northBorder.type,
                district.northBorder.cityWall,
                district.northBorder.moat
            ) else null,
            if (district.eastBorder.facing == Cardinal.EAST) BorderPolygonElement(
                eastBorder + (dCoord.offsetX to dCoord.offsetY),
                westBorder + (dCoord.east.offsetX to dCoord.east.offsetY),
                district.eastBorder.id,
                district.eastBorder.type,
                district.eastBorder.cityWall,
                district.eastBorder.moat
            ) else null,
            if (district.westBorder.facing == Cardinal.WEST) BorderPolygonElement(
                westBorder + (dCoord.offsetX to dCoord.offsetY),
                eastBorder + (dCoord.west.offsetX to dCoord.west.offsetY),
                district.westBorder.id,
                district.westBorder.type,
                district.westBorder.cityWall,
                district.westBorder.moat
            ) else null,
            if (district.southBorder.facing == Cardinal.SOUTH) BorderPolygonElement(
                southBorder + (dCoord.offsetX to dCoord.offsetY),
                northBorder + (dCoord.south.offsetX to dCoord.south.offsetY),
                district.southBorder.id,
                district.southBorder.type,
                district.southBorder.cityWall,
                district.southBorder.moat
            ) else null
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
    operator fun Pair<Double, Double>.minus(other: Pair<Double, Double>) = first - other.first to second - other.second
    operator fun Set<Pair<Double, Double>>.plus(other: Pair<Double, Double>) = map { it + other }.toSet()

    val Coordinate.offsetX
        get() = x * districtWidth + borderWidth + roadWidth + mapOffsetX
    val Coordinate.offsetY
        get() = y * districtWidth + borderWidth + roadWidth + mapOffsetY
    val Coordinate.innerOffsetX
        get() = offsetX + borderWidth
    val Coordinate.innerOffsetY
        get() = offsetY + borderWidth
    val Coordinate.lotOffsetX
        get() = innerOffsetX + roadWidth
    val Coordinate.lotOffsetY
        get() = innerOffsetY + roadWidth
}
