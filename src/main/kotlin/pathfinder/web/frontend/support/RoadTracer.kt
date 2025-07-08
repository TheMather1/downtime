package pathfinder.web.frontend.support

import pathfinder.domain.kingdom.terrain.Hex
import pathfinder.domain.kingdom.terrain.KingdomMap
import pathfinder.domain.support.coordinate.Coordinate
import pathfinder.web.FLAT_TOP
import pathfinder.web.frontend.dto.Path

class RoadTracer(kingdomMap: KingdomMap, z: Int = 0) {
    val hexes = kingdomMap.hexes.filter { it.key.z == z }
    val roadHexes = hexes.filter { it.value.hasRoad }.mapKeys { it.key.toCoordinate(FLAT_TOP) }

    fun findRoads(): Set<Path> {
        val workingSet = roadHexes.toMutableMap()
        val roads = mutableSetOf<Path>()

        while (workingSet.isNotEmpty()) {
            val path = workingSet.flatMap { findRoads(mutableSetOf(), mutableSetOf(it.value)) }
                .maxBy { it.length }
            roads.add(path)
            path.forEach { workingSet.remove(Coordinate(it.x, it.y)) }
        }

        connectSubpaths(roads)

        return roads
    }

    private fun connectSubpaths(roads: MutableSet<Path>) {
        roadHexes.forEach { coordinate, hex ->
            val thisNode = roads.firstNotNullOf { it.find(coordinate.x, coordinate.y) }
            val ephemeralNode = Path(coordinate.x, coordinate.y, ephemeral = true)
            hex.neighbors { it.hasRoad }.forEach { neighbor ->
                val neighborCoordinate = neighbor.coordinate.toCoordinate(FLAT_TOP)
                if (thisNode.childNodes.none { it.x == neighborCoordinate.x && it.y == neighborCoordinate.y }
                    && roads.firstNotNullOf {
                        it.find(
                            neighborCoordinate.x,
                            neighborCoordinate.y
                        )
                    }.childNodes.none { it.x == coordinate.x && it.y == coordinate.y }) {
                    roads.firstNotNullOfOrNull { it.find(neighborCoordinate.x, neighborCoordinate.y) }?.let {
                        it.childNodes.add(ephemeralNode)
                        thisNode.childNodes.add(Path(it.x, it.y, ephemeral = true))
                    }
                }
            }
        }
    }

    private fun findRoads(parentNodes: MutableSet<Hex>, childNodes: MutableSet<Hex>): Set<Path> {
        if (childNodes.isEmpty()) return emptySet()
        parentNodes.addAll(childNodes)
        val grandChildren = childNodes.flatMap {
            it.neighbors.filter { it !in parentNodes && (it.hasRoad) }
        }.toMutableSet()
        val paths = findRoads(parentNodes, grandChildren)
        val usedPaths = mutableSetOf<Path>()
        return childNodes.map { childNode ->
            val coordinate = childNode.coordinate.toCoordinate(FLAT_TOP)
            Path(coordinate.x, coordinate.y, paths.filter { path ->
                path !in usedPaths && childNode.neighbors.any {
                    val childCoordinate = it.coordinate.toCoordinate(FLAT_TOP)
                    path.x == childCoordinate.x && path.y == childCoordinate.y
                }
            }.toMutableSet()).also { usedPaths.addAll(it.childNodes) }
        }.toSet()
    }
}
