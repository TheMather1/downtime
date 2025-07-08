package pathfinder.web.frontend.dto.settlement

import pathfinder.domain.kingdom.settlement.SettlementType

data class SettlementData(
    val id: Long,
    val name : String,
    val size: String
) {
    constructor(id: Long, name: String, size: SettlementType): this(id, name, size.displayName)
}
