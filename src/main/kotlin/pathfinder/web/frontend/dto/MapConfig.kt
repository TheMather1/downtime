package pathfinder.web.frontend.dto

import pathfinder.domain.kingdom.terrain.Hex
import pathfinder.domain.kingdom.terrain.TerrainType
import pathfinder.domain.support.coordinate.HexCoordinate
import kotlin.math.cos
import kotlin.math.sin

class MapConfig(
    val circumradius: Int = 50,
    val riverWidth: Double = 0.25,
    val roadWidth: Double = 0.15,
    val lakeRadius: Int = 25,
    val horizontal: Boolean = true,
    val offsetX: Double,
    val offsetY: Double,
    val z: Int
) {
    val halfCircumradius: Int = circumradius / 2
    val inradius: Int = (0.8660254 * circumradius).toInt()
    val hexHeight: Int = if (horizontal) inradius * 2 + 6 else circumradius * 2 + 2
    val hexWidth: Int = if (horizontal) circumradius * 2 + 2 else inradius * 2 + 6
    val patternOffsetX = if (horizontal) -1.0 else -2.5
    val patternOffsetY = if (horizontal) -2.5 else -1.0
    val mapTransform = hexTransform(offsetX, offsetY)
    val settlementTransform = "translate(-${(if(horizontal) circumradius else inradius)/2} -${(if(horizontal) inradius else circumradius)/2})"
    val hexPoints = if(horizontal) horizontalPoints() else verticalPoints()

    fun centerX(x: Number, y: Number): Double = (if (horizontal) x.toDouble() * 1.5 * circumradius
        else (x.toDouble() * 2 + (y.toInt() and 1)) * inradius)
    fun centerY(x: Number, y: Number): Double = (if (horizontal) (y.toDouble() * 2 + (x.toInt() and 1)) * inradius
        else y.toDouble() * 1.5 * circumradius)
    fun center(x: Number, y: Number) = centerX(x, y) to centerY(x, y)

    fun hexTransform(x: Double, y: Double) = "translate(${centerX(x,y)} ${centerY(x,y)}) "
    fun hexTransform(coordinate: HexCoordinate) = hexTransform(coordinate.x.toDouble(), coordinate.y.toDouble())

    fun horizontalPoints(): String {
        val ul = point(0 - halfCircumradius, 0 - inradius)
        val ur = point(halfCircumradius, 0 - inradius)
        val r = point(circumradius, 0)
        val br = point(halfCircumradius, inradius)
        val bl = point(0 - halfCircumradius, inradius)
        val l = point(0 - circumradius, 0)
        return "$ul $ur $r $br $bl $l"
    }

    fun verticalPoints(): String {
        val ul = point(0 - inradius, 0 - halfCircumradius)
        val ur = point(0 - inradius, halfCircumradius)
        val b = point(0, circumradius)
        val br = point(inradius, halfCircumradius)
        val bl = point(inradius, 0 - halfCircumradius)
        val u = point(0, 0 - circumradius)
        return "$ul $ur $b $br $bl $u"
    }

    fun randomLake(coordinate: HexCoordinate): String {
        val (centerX, centerY) = center(coordinate.x, coordinate.y)

        return variations(lakeRadius.toDouble(), 15.0, coordinate.x + coordinate.y, 8).mapIndexed { i, radius ->
            val angle = (i / 8.0) * Math.PI * 2.0
            cos(angle) * radius + centerX to sin(angle) * radius + centerY
        }.let {
            val variationsX = variations(0.0, 10.0, coordinate.x.times(coordinate.x) + coordinate.y.times(coordinate.y), 8)
            val variationsY = variations(0.0, 10.0, coordinate.x.times(coordinate.y) + coordinate.y.times(coordinate.x), 8)
            it.zipWithNext { current, next ->
                val controlPointX = current.first + (next.first - current.first) * 0.5 + variationsX[it.indexOf(current)]
                val controlPointY = current.second + (next.second - current.second) * 0.5 + variationsY[it.indexOf(current)]
                " S $controlPointX $controlPointY, ${next.first} ${next.second}"
            }.fold("M ${it[0].first} ${it[0].second}") { r, o ->
                r + o
            }
        } + " Z"
    }

    fun point(x: Int, y: Int) = "$x,$y"

    fun variations(base: Double, deviation: Double, seed: Int, count: Int) = (0 until count).map {
        variate(base, deviation, seed + it)
    }
    fun variate(base: Double, deviation: Double, seed: Int) = base + (((seed % 10) / 10.0 * seed) % 1 - 0.5) * deviation

    fun processRiverPath(path: Path, hexes: Map<HexCoordinate, Hex>, start: Boolean = true, startEdgeX: Double? = null, startEdgeY: Double? = null): String {
        var pathString = ""
        val center = center(path.x, path.y)
        if (start) pathString += "M ${center.first} ${center.second}"
        val isWater = hexes.any { it.key.x == path.x && it.key.y == path.y && it.value.rawTerrain == TerrainType.WATER }
        if (!isWater && path.childNodes.isEmpty()) pathString += " L ${center.first} ${center.second}"
        else path.childNodes.forEachIndexed { index, childPath ->
            // Get centers for both hexes
            val childCenter = center(childPath.x, childPath.y)

            // Calculate control point for curve
            val edgeX = (center.first + childCenter.first) / 2
            val edgeY = (center.second + childCenter.second) / 2

            // Create SVG path
            if (startEdgeX == null || startEdgeY == null) pathString += " M ${center.first} ${center.second} L $edgeX $edgeY"
            else {
                if (index != 0) {
                    pathString += " M $startEdgeX $startEdgeY"
                }
                pathString += " Q ${center.first} ${center.second} $edgeX $edgeY"
            }
            // Continue processing child paths
            pathString += processRiverPath(childPath, hexes, false, edgeX, edgeY)
        }
        return pathString
    }

    fun processRoadPath(path: Path): String {
        var currX = 0.0
        var currY = 0.0

        fun processRoadPath(path: Path, start: Boolean = true, startEdgeX: Double? = null, startEdgeY: Double? = null): String {
            var pathString = ""
            val center = center(path.x, path.y)
            if (start) {
                pathString += "M ${center.first} ${center.second}"
                currX = center.first
                currY = center.second
            }
            if (path.childNodes.isEmpty() && !path.ephemeral) {
                pathString += " L ${center.first} ${center.second}"
                currX = center.first
                currY = center.second
            }
            else path.childNodes.forEachIndexed { index, childPath ->
                // Get centers for both hexes
                val childCenter = center(childPath.x, childPath.y)

                // Calculate control point for curve
                val edgeX = (center.first + childCenter.first) / 2
                val edgeY = (center.second + childCenter.second) / 2

                // Create SVG path
                if (startEdgeX == null || startEdgeY == null) {
                    if(path.childNodes.size < 2) {
                        pathString += " M ${center.first} ${center.second} L $edgeX $edgeY"
                        currX = edgeX
                        currY = edgeY
                    }
                } else {
                    if (index != 0 || (currX != startEdgeX && currY != startEdgeY)) {
                        pathString += " M $startEdgeX $startEdgeY"
                    }
                    pathString += " Q ${center.first} ${center.second} $edgeX $edgeY"
                    currX = edgeX
                    currY = edgeY
                }

                path.childNodes.forEachIndexed { indexB, childPathB ->
                    if (indexB > index) {
                        val childCenterB = center(childPathB.x, childPathB.y)
                        val edgeBX = (center.first + childCenterB.first) / 2
                        val edgeBY = (center.second + childCenterB.second) / 2

                        pathString += " M $edgeBX $edgeBY Q ${center.first} ${center.second} $edgeX $edgeY"
                        currX = edgeX
                        currY = edgeY
                    }
                }
                // Continue processing child paths
                if(!path.ephemeral) pathString += processRoadPath(childPath, false, edgeX, edgeY)
            }
            return pathString
        }
        return processRoadPath(path)
    }
}
