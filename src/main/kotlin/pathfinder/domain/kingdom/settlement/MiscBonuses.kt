package pathfinder.domain.kingdom.settlement

import jakarta.persistence.Embeddable

@Embeddable
class MiscBonuses(
    var corruption: Int = 0,
    var crime: Int = 0,
    var law: Int = 0,
    var lore: Int = 0,
    var productivity: Int = 0,
    var society: Int = 0,
    var consumption: Int = 0,
    var economy: Int = 0,
    var loyalty: Int = 0,
    var stability: Int = 0,
)
