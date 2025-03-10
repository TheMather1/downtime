package pathfinder.domain.kingdom.settlement.buildings

enum class LotBuildingType(
    val size: BuildingSize,
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
    override val baseValueBonus: Int = 0,
): BuildingType {
    ACADEMY(
        cost = 52,
        size = BuildingSize.LARGE,
        economyBonus = 2,
        loyaltyBonus = 2,
        loreBonus = 2,
        productivityBonus = 1,
        societyBonus = 2
    ) {
        override fun upgradesTo(building: LotBuildingType) = building == UNIVERSITY
    },
    ALCHEMIST(
        cost = 18,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        baseValueBonus = 1000,
    ),
    ARENA(
        cost = 40,
        size = BuildingSize.HUGE,
        stabilityBonus = 4,
        crimeBonus = 1
    ),
    BANK(
        cost = 28,
        size = BuildingSize.NORMAL,
        economyBonus = 4,
        baseValueBonus = 2000
    ),
    BARDIC_COLLEGE(
        cost =40,
        size = BuildingSize.LARGE,
        economyBonus = 1,
        loyaltyBonus = 3,
        stabilityBonus = 1
    ),
    BARRACKS(
        cost = 6,
        size = BuildingSize.LARGE,
        unrestBonus = -1,
        lawBonus = 1
    ){
        override fun upgradesTo(building: LotBuildingType) = building == GARRISON
    },
    BLACK_MARKET(
        cost = 50,
        size = BuildingSize.NORMAL,
        economyBonus = 2,
        stabilityBonus = 1,
        unrestBonus = 1,
        corruptionBonus = 2,
        crimeBonus = 2,
        baseValueBonus = 2000
    ),
    BREWERY(
        cost = 6,
        size = BuildingSize.NORMAL,
        loyaltyBonus = 1,
        stabilityBonus = 1
    ),
    BUREAU(
        cost = 10,
        size = BuildingSize.LARGE,
        economyBonus = 1,
        loyaltyBonus = -1,
        stabilityBonus = 1,
        corruptionBonus = 1,
        lawBonus = 1
    ),
    CASTERS_TOWER(
        cost = 30,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        loyaltyBonus = 1
    ),
    CASTLE(
        cost = 54,
        size = BuildingSize.HUGE,
        economyBonus = 2,
        loyaltyBonus = 2,
        stabilityBonus = 2,
        unrestBonus = -4
    ),
    CATHEDRAL(
        cost = 58,
        size = BuildingSize.HUGE,
        loyaltyBonus = 4,
        stabilityBonus = 4,
        unrestBonus = -4,
        lawBonus = 2
    ),
    DANCE_HALL(
        cost = 4,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        loyaltyBonus = 2,
        unrestBonus = 1,
        corruptionBonus = 1,
        crimeBonus = 1
    ),
    DUMP(
        cost = 4,
        size = BuildingSize.NORMAL,
        stabilityBonus = 1
    ),
    EXOTIC_ARTISAN(
        cost = 10,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        stabilityBonus = 1
    ),
    FOREIGN_QUARTERS(
        cost = 30,
        size = BuildingSize.HUGE,
        economyBonus = 3,
        stabilityBonus = -1,
        crimeBonus = 1,
        loreBonus = 1,
        societyBonus = 2
    ),
    FORTRESS_OF_THE_FAITH(
        cost = 80,
        size = BuildingSize.HUGE,
        consumptionBonus = 1,
        loyaltyBonus = 3,
        stabilityBonus = 3
    ),
    FOUNDRY(
        cost = 16,
        size = BuildingSize.LARGE,
        economyBonus = 1,
        stabilityBonus = 1,
        unrestBonus = 1,
        productivityBonus = 1
    ),
    GARRISON(
        cost = 28,
        size = BuildingSize.LARGE,
        loyaltyBonus = 2,
        stabilityBonus = 2,
        unrestBonus = -2
    ),
    GRANARY( //TODO: Store excess BP for future Consumption
        cost = 12,
        size = BuildingSize.NORMAL,
        loyaltyBonus = 1,
        stabilityBonus = 1,
    ),
    GRAVEYARD(
        cost = 4,
        size = BuildingSize.NORMAL,
        loyaltyBonus = 1
    ),
    GUILDHALL(
        cost = 34,
        size = BuildingSize.LARGE,
        economyBonus = 2,
        loyaltyBonus = 2,
        lawBonus = 1,
        productivityBonus = 2,
        baseValueBonus = 1000
    ),
    HERBALIST(
        cost = 10,
        size = BuildingSize.NORMAL,
        loyaltyBonus = 1,
        stabilityBonus = 1
    ),
    HOSPITAL(
        cost = 30,
        size = BuildingSize.LARGE,
        loyaltyBonus = 1,
        stabilityBonus = 2,
        loreBonus = 1,
        productivityBonus = 2
    ),
    HOUSE(
        cost = 3,
        size = BuildingSize.NORMAL,
        unrestBonus = -1
    ),
    INN(
        cost = 10,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        loyaltyBonus = 1,
        societyBonus = 1,
        baseValueBonus = 500
    ),
    JAIL(
        cost = 14,
        size = BuildingSize.NORMAL,
        loyaltyBonus = 2,
        stabilityBonus = 2,
        unrestBonus = -2,
        crimeBonus = -1,
        lawBonus = 1
    ),
    LIBRARY(
        cost = 6,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        loyaltyBonus = 1,
        loreBonus = 1
    ){
        override fun upgradesTo(building: LotBuildingType) = building == ACADEMY
    },
    LUXURY_STORE(
        cost = 28,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        baseValueBonus = 2000
    ){
        override fun upgradesTo(building: LotBuildingType) = building == MAGIC_SHOP
    },
    MAGIC_SHOP(
        cost = 68,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        baseValueBonus = 2000
    ),
    MAGICAL_ACADEMY(
        cost = 58,
        size = BuildingSize.LARGE,
        economyBonus = 2,
        loreBonus = 2,
        societyBonus = 1
    ),
    MANSION(
        cost = 10,
        size = BuildingSize.NORMAL,
        stabilityBonus = 1,
        lawBonus = 1,
        societyBonus = 1
    ){
        override fun upgradesTo(building: LotBuildingType) = building == NOBLE_VILLA
    },
    MARKET(
        cost = 48,
        size = BuildingSize.LARGE,
        economyBonus = 2,
        stabilityBonus = 2,
        baseValueBonus = 2000
    ),
    MENAGERIE(
        cost = 16,
        size = BuildingSize.HUGE,
        economyBonus = 1,
    ),
    MILITARY_ACADEMY(
        cost = 36,
        size = BuildingSize.LARGE,
        loyaltyBonus = 1,
        stabilityBonus = 1,
        lawBonus = 1,
        loreBonus = 1
    ),
    MILL(
        cost = 6,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        stabilityBonus = 1,
        productivityBonus = 1
    ),
    MINT(
        cost = 30,
        size = BuildingSize.NORMAL,
        economyBonus = 3,
        loyaltyBonus = 3,
        stabilityBonus = 1,
    ),
    MONASTERY(
        cost = 16,
        size = BuildingSize.LARGE,
        stabilityBonus = 1,
        lawBonus = 1,
        loreBonus = 1
    ),
    MONUMENT(
        cost = 6,
        size = BuildingSize.NORMAL,
        loyaltyBonus = 1,
        unrestBonus = -1
    ),
    MUSEUM(
        cost = 30,
        size = BuildingSize.LARGE,
        economyBonus = 1,
        loyaltyBonus = 1,
        loreBonus = 2,
        societyBonus = 1
    ),
    NOBLE_VILLA(
        cost = 24,
        size = BuildingSize.LARGE,
        economyBonus = 1,
        loyaltyBonus = 1,
        stabilityBonus = 1,
        societyBonus = 1
    ),
    OBSERVATORY(
        cost = 12,
        size = BuildingSize.NORMAL,
        stabilityBonus = 1,
        loreBonus = 2
    ),
    ORPHANAGE(
        cost = 6,
        size = BuildingSize.NORMAL,
        stabilityBonus = 1,
        unrestBonus = -1
    ),
    PALACE(
        cost = 108,
        size = BuildingSize.HUGE,
        economyBonus = 2,
        loyaltyBonus = 6,
        stabilityBonus = 2,
        lawBonus = 2,
        baseValueBonus = 1000
    ),
    PARK(
        cost = 4,
        size = BuildingSize.NORMAL,
        loyaltyBonus = 1,
        unrestBonus = -1
    ),
    PIER(
        cost = 16,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        stabilityBonus = 1,
        crimeBonus = 1,
        baseValueBonus = 1000
    ) {
        override fun upgradesTo(building: LotBuildingType) = building == WATERFRONT
    },
    SHOP(
        cost = 8,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        productivityBonus = 1,
        baseValueBonus = 500
    ){
        override fun upgradesTo(building: LotBuildingType) = building == LUXURY_STORE || building == MARKET
    },
    SHRINE(
        cost = 8,
        size = BuildingSize.NORMAL,
        loyaltyBonus = 1,
        unrestBonus = -1
    ){
        override fun upgradesTo(building: LotBuildingType) = building == TEMPLE
    },
    SMITHY(
        cost = 6,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        stabilityBonus = 1
    ),
    STABLE(
        cost = 10,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        loyaltyBonus = 1,
        baseValueBonus = 500
    ),
    STOCKYARD(
        cost = 20,
        size = BuildingSize.HUGE,
        economyBonus = 1,
        stabilityBonus = -1,
        productivityBonus = 1
    ),
    TANNERY(
        cost = 6,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        stabilityBonus = 1,
        societyBonus = -1
    ),
    TAVERN(
        cost = 12,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        loyaltyBonus = 1,
        corruptionBonus = 1,
        baseValueBonus = 500
    ),
    TEMPLE(
        cost = 32,
        size = BuildingSize.LARGE,
        loyaltyBonus = 2,
        stabilityBonus = 2,
        unrestBonus = -2
    ),
    TENEMENT(
        cost = 1,
        size = BuildingSize.NORMAL,
        unrestBonus = 2
    ) {
        override fun upgradesTo(building: LotBuildingType) = building == HOUSE
    },
    THEATER(
        cost = 24,
        size = BuildingSize.LARGE,
        economyBonus = 2,
        stabilityBonus = 2
    ) {
        override fun upgradesTo(building: LotBuildingType) = building == ARENA
    },
    TOWN_HALL(
        cost = 22,
        size = BuildingSize.LARGE,
        economyBonus = 1,
        loyaltyBonus = 1,
        stabilityBonus = 1,
        lawBonus = 1
    ),
    TRADE_SHOP(
        cost = 10,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        stabilityBonus = 1,
        productivityBonus = 1,
        baseValueBonus = 500
    ) {
        override fun upgradesTo(building: LotBuildingType) = building == GUILDHALL
    },
    UNIVERSITY(
        cost = 78,
        size = BuildingSize.HUGE,
        economyBonus = 3,
        loyaltyBonus = 3
    ),
    WATCHTOWER(
        cost = 12,
        size = BuildingSize.NORMAL,
        stabilityBonus = 1,
        unrestBonus = -1
    ),
    WATERFRONT(
        cost = 90,
        size = BuildingSize.HUGE,
        economyBonus = 4,
        productivityBonus = 2,
        baseValueBonus = 4000
    ),
    WATERWAY(
        cost = 3,
        size = BuildingSize.NORMAL_OR_LARGE
    );

    open fun upgradesTo(type: LotBuildingType) = false

    enum class BuildingSize {
        NORMAL,
        NORMAL_OR_LARGE {
            override fun isSize(size: BuildingSize) = size == NORMAL || size == NORMAL_OR_LARGE || size == LARGE
        },
        LARGE,
        HUGE;

        open fun isSize(size: BuildingSize) = this == size
    }
}