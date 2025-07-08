package pathfinder.web.frontend.support

import pathfinder.domain.kingdom.terrain.Hex
import pathfinder.domain.kingdom.terrain.KingdomMap
import pathfinder.domain.kingdom.terrain.TerrainType
import pathfinder.web.FLAT_TOP
import pathfinder.web.frontend.dto.Path

class RiverTracer(kingdomMap: KingdomMap, z: Int = 0) {
    val hexes = kingdomMap.hexes.filter { it.key.z == z }
    val riverHexes = hexes.filter { it.value.hasRiver }

    fun findRivers(): Set<Path> {
        val workingSet = riverHexes.values.toMutableSet()
        val rivers = mutableSetOf<Path>()

        while (workingSet.isNotEmpty()) {
            val path = workingSet.flatMap { findRivers(mutableSetOf(), mutableSetOf(it)) }
                .maxBy { it.length }
            rivers.add(path)
            path.forEach { workingSet.removeAll { hex ->
                val coordinate = hex.coordinate.toCoordinate(FLAT_TOP)
                coordinate.x == it.x && coordinate.y == it.y
            } }
        }

        return rivers
    }

    private fun findRivers(parentNodes: MutableSet<Hex>, childNodes: MutableSet<Hex>): Set<Path> {
        if (childNodes.isEmpty()) return emptySet()
        parentNodes.addAll(childNodes)
        val grandChildren = childNodes.flatMap {
            if (it.rawTerrain == TerrainType.WATER) emptyList()
            else it.neighbors.filter { it !in parentNodes && (it.hasRiver || it.rawTerrain == TerrainType.WATER) }
        }.toMutableSet()
        val paths = findRivers(parentNodes, grandChildren)
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
