package pathfinder.service.generation

import pathfinder.domain.kingdom.terrain.features.TerrainFeature
import pathfinder.domain.support.coordinate.HexCoordinate

data class TerrainParams(
    val coordinate: HexCoordinate,  // Add this
    val elevation: Double,
    val moisture: Double,
    val temperature: Double,
    val features: MutableSet<TerrainFeature> = mutableSetOf()
)
