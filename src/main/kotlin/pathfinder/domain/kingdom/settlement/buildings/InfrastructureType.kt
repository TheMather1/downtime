package pathfinder.domain.kingdom.settlement.buildings

enum class InfrastructureType(
    override val cost: Int,
    override val economyBonus: Int = 0,
    override val loyaltyBonus: Int = 0,
    override val stabilityBonus: Int = 0,
    override val unrestBonus: Int = 0,
    override val consumptionBonus: Int = 0,
    override val corruptionBonus: Int = 0,
    override val crimeBonus: Int = 0,
    override val lawBonus: Int = 0,
    override val loreBonus: Int = 0,
    override val productivityBonus: Int = 0,
    override val societyBonus: Int = 0,
    override val baseValueBonus: Int = 0
): BuildingType {
    MAGICAL_STREETLAMPS(
        cost = 6,
        crimeBonus = -1
    ),
    PAVED_STREETS(
        cost = 24,
        economyBonus = 2,
        stabilityBonus = 1,
        productivityBonus = 2
    ),
    SEWER_SYSTEM(
        cost = 24,
        loyaltyBonus = 1,
        stabilityBonus = 2,
        crimeBonus = 1,
        productivityBonus = 2
    );

    override val displayName = name.split('_').joinToString(" ") { it.lowercase().replaceFirstChar { it.uppercase() } }
}
