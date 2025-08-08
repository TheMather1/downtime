package pathfinder.domain.kingdom.settlement.buildings

enum class BorderBuildingType(
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
    CITY_WALL(
        cost = 2
    ),
    MAGICAL_STREETLAMPS(
        cost = 5,
        crimeBonus = -1
    ),
    MOAT(
        cost = 2
    ),
    WATERGATE(
        cost = 2
    );

    override val displayName = name.split('_').joinToString(" ") { it.lowercase().replaceFirstChar { it.uppercase() } }
}
