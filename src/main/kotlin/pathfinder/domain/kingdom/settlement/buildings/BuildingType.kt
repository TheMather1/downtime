package pathfinder.domain.kingdom.settlement.buildings

sealed interface BuildingType {
    val name: String
    val cost: Int
    val economyBonus: Int
    val loyaltyBonus: Int
    val stabilityBonus: Int
    val unrestBonus: Int
    val consumptionBonus: Int
    val corruptionBonus: Int
    val crimeBonus: Int
    val lawBonus: Int
    val loreBonus: Int
    val productivityBonus: Int
    val societyBonus: Int
    val baseValueBonus: Int
}