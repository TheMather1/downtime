package pathfinder.domain.kingdom.settlement.buildings

enum class UpgradeType(
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
    BRIDGE(
        cost = 6,
        economyBonus = 1
    ) {
        override fun eligible(lot: Lot, neighbors: List<Lot>) = lot.building?.type == LotBuildingType.WATERWAY
    },
    CISTERN(
        cost = 6,
        stabilityBonus = 1
    ) {
        private val ineligibleNeighbors = setOf(
            LotBuildingType.DUMP,
            LotBuildingType.GRAVEYARD,
            LotBuildingType.STABLE,
            LotBuildingType.STOCKYARD,
            LotBuildingType.TANNERY
        )
        override fun eligible(lot: Lot, neighbors: List<Lot>) = (neighbors + lot).none {
            it.building?.type in ineligibleNeighbors
        }
    },
    EVERFLOWING_SPRING(
        cost = 5
    ) {
        private val eligibleBuldings = setOf(
            LotBuildingType.CASTLE,
            LotBuildingType.CATHEDRAL,
            LotBuildingType.MARKET,
            LotBuildingType.MONUMENT,
            LotBuildingType.PARK,
            LotBuildingType.TOWN_HALL
        )
        override fun eligible(lot: Lot, neighbors: List<Lot>) = lot.building?.type in eligibleBuldings
    },
    MAGICAL_STREETLAMPS(
        cost = 5,
        crimeBonus = -1
    );

    open fun eligible(lot: Lot, neighbors: List<Lot>): Boolean = true
}