package pathfinder.web.frontend.dto

import pathfinder.domain.kingdom.terrain.TerrainType
import pathfinder.domain.kingdom.terrain.features.TerrainFeature
import pathfinder.domain.kingdom.terrain.improvements.Improvement
import pathfinder.web.frontend.dto.settlement.SettlementData

class HexData(
    val q: Int,
    val r: Int,
    val id: Long = 0,
    val terrainType: TerrainType? = null,
    val eligibleImprovements: Set<Improvement> = emptySet(),
    val improvements: Set<Improvement> = emptySet(),
    val eligibleFeatures: Set<TerrainFeature> = emptySet(),
    val features: Set<TerrainFeature> = emptySet(),
    val roadDirections: Int = 0,
    val riverDirections: Int = 0,
    val visibleText: String = "",
    val hiddenText: String = "",
    val settlementData: SettlementData? = null
)
