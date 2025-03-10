package pathfinder.web.frontend.dto

import pathfinder.domain.kingdom.terrain.TerrainType
import pathfinder.domain.kingdom.terrain.features.TerrainFeature
import pathfinder.domain.kingdom.terrain.improvements.Improvement

class HexData(
    val q: Int,
    val r: Int,
    val terrainType: TerrainType? = null,
    val improvements: Set<Improvement> = emptySet(),
    val features: Set<TerrainFeature> = emptySet(),
    val roadDirections: Int = 0,
    val riverDirections: Int = 0,
    val settlementData: SettlementData? = null
)