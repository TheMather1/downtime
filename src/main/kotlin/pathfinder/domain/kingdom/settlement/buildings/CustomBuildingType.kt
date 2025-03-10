package pathfinder.domain.kingdom.settlement.buildings

import jakarta.persistence.Embeddable

@Embeddable
class CustomBuildingType(
    override var name: String,
    override var corruptionBonus: Int = 0,
    override var crimeBonus: Int = 0,
    override var lawBonus: Int = 0,
    override var loreBonus: Int = 0,
    override var productivityBonus: Int = 0,
    override var societyBonus: Int = 0,
    override var economyBonus: Int = 0,
    override var loyaltyBonus: Int = 0,
    override var stabilityBonus: Int = 0,
    override var unrestBonus: Int = 0,
    override var consumptionBonus: Int = 0,
    override val baseValueBonus: Int = 0
) : BuildingType {
    override val cost = 0
}