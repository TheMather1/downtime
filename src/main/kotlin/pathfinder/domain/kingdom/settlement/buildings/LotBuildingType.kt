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
        override fun upgradesTo(type: LotBuildingType) = type == UNIVERSITY
    },
    ALCHEMIST(
        cost = 18,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        baseValueBonus = 1000,
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.any { l ->
            l.neighbors.any { it !in lots && it.building?.type?.countsAs(HOUSE) == true}
        }
    },
    ARENA(
        cost = 40,
        size = BuildingSize.HUGE,
        stabilityBonus = 4,
        crimeBonus = 1
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.first().district.settlement.buildings.none { it.type == ARENA }
    },
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
        override fun upgradesTo(type: LotBuildingType) = type == GARRISON
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
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.sumOf { l ->
            l.neighbors.count { it !in lots && it.building?.type?.countsAs(HOUSE) == true}
        } >= 2
    },
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
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.first().district.settlement.buildings.none { it.type == CASTLE }
    },
    CATHEDRAL(
        cost = 58,
        size = BuildingSize.HUGE,
        loyaltyBonus = 4,
        stabilityBonus = 4,
        unrestBonus = -4,
        lawBonus = 2
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.first().district.settlement.buildings.none { it.type == CATHEDRAL }
    },
    DANCE_HALL(
        cost = 4,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        loyaltyBonus = 2,
        unrestBonus = 1,
        corruptionBonus = 1,
        crimeBonus = 1
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.any { l ->
            l.neighbors.any { it !in lots && it.building?.type?.countsAs(HOUSE) == true }
        }
    },
    DUMP(
        cost = 4,
        size = BuildingSize.NORMAL,
        stabilityBonus = 1
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.any { l ->
            l.neighbors.none { it !in lots && it.building?.type?.countsAs(HOUSE, MANSION, NOBLE_VILLA) == true}
        }
    },
    EXOTIC_ARTISAN(
        cost = 10,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        stabilityBonus = 1
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.any { l ->
            l.neighbors.any { it !in lots && it.building?.type?.countsAs(HOUSE) == true}
        }
    },
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
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.any { l ->
            l.neighbors.any { it !in lots && it.building?.type?.countsAs(WATERWAY) == true}
                    || (l.coordinate.y == 0 && l.district.northBorder.hasWater)
                    || (l.coordinate.y == 5 && l.district.southBorder.hasWater)
                    || (l.coordinate.x == 0 && l.district.westBorder.hasWater)
                    || (l.coordinate.x == 5 && l.district.eastBorder.hasWater)
        }
    },
    GARRISON(
        cost = 28,
        size = BuildingSize.LARGE,
        loyaltyBonus = 2,
        stabilityBonus = 2,
        unrestBonus = -2
    ),
    GRANARY(
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
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.any { l ->
            l.neighbors.any { it !in lots && it.building?.type?.countsAs(HOUSE) == true}
        }
    },
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
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.any { l ->
            l.neighbors.any { it !in lots && it.building?.type?.countsAs(HOUSE) == true}
        }
    },
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
        override fun upgradesTo(type: LotBuildingType) = type == ACADEMY
    },
    LUXURY_STORE(
        cost = 28,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        baseValueBonus = 2000
    ){
        override fun validPosition(lots: Set<Lot>) = lots.any { l ->
            l.neighbors.any { it !in lots && it.building?.type?.countsAs(HOUSE) == true}
        }
        override fun upgradesTo(type: LotBuildingType) = type == MAGIC_SHOP
    },
    MAGIC_SHOP(
        cost = 68,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        baseValueBonus = 2000
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.sumOf { l ->
            l.neighbors.count { it !in lots && it.building?.type?.countsAs(HOUSE) == true}
        } >= 2
      },
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
        override fun upgradesTo(type: LotBuildingType) = type == NOBLE_VILLA
    },
    MARKET(
        cost = 48,
        size = BuildingSize.LARGE,
        economyBonus = 2,
        stabilityBonus = 2,
        baseValueBonus = 2000
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.sumOf { l ->
            l.neighbors.count { it !in lots && it.building?.type?.countsAs(HOUSE) == true}
        } >= 2
    },
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
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.first().district.settlement.buildings.none { it.type == MILITARY_ACADEMY }
    },
    MILL(
        cost = 6,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        stabilityBonus = 1,
        productivityBonus = 1
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.any { l ->
            l.neighbors.any { it !in lots && it.building?.type?.countsAs(WATERWAY, MILLPOND) == true}
                    || (l.coordinate.y == 0 && l.district.northBorder.hasWater)
                    || (l.coordinate.y == 5 && l.district.southBorder.hasWater)
                    || (l.coordinate.x == 0 && l.district.westBorder.hasWater)
                    || (l.coordinate.x == 5 && l.district.eastBorder.hasWater)
        }
    },
    MILLPOND(
        cost = 3,
        size = BuildingSize.NORMAL,
        loyaltyBonus = 1
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.first().district.settlement.hex.hasRiver
    },
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
        override fun validPosition(lots: Set<Lot>) = lots.any { l ->
            l.neighbors.any { it !in lots && it.building?.type?.countsAs(WATERWAY) == true}
                    || (l.coordinate.y == 0 && l.district.northBorder.hasWater)
                    || (l.coordinate.y == 5 && l.district.southBorder.hasWater)
                    || (l.coordinate.x == 0 && l.district.westBorder.hasWater)
                    || (l.coordinate.x == 5 && l.district.eastBorder.hasWater)
        }
        override fun upgradesTo(type: LotBuildingType) = type == WATERFRONT
    },
    SHOP(
        cost = 8,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        productivityBonus = 1,
        baseValueBonus = 500
    ){
        override fun validPosition(lots: Set<Lot>) = lots.any { l ->
            l.neighbors.any { it !in lots && it.building?.type?.countsAs(HOUSE) == true}
        }
        override fun upgradesTo(type: LotBuildingType) = type == LUXURY_STORE || type == MARKET
    },
    SHRINE(
        cost = 8,
        size = BuildingSize.NORMAL,
        loyaltyBonus = 1,
        unrestBonus = -1
    ){
        override fun upgradesTo(type: LotBuildingType) = type == TEMPLE
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
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.any { l ->
            l.neighbors.any { it !in lots && it.building?.type?.countsAs(HOUSE, MANSION, NOBLE_VILLA) == true}
        }
    },
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
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.any { l ->
            l.neighbors.none { it !in lots && it.building?.type?.countsAs(HOUSE) == true}
        }
    },
    TAVERN(
        cost = 12,
        size = BuildingSize.NORMAL,
        economyBonus = 1,
        loyaltyBonus = 1,
        corruptionBonus = 1,
        baseValueBonus = 500
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.any { l ->
            l.neighbors.any { it !in lots && it.building?.type?.countsAs(HOUSE) == true}
        }
    },
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
        override fun countsAs(vararg type: BuildingType) = type.any { it == HOUSE || it == this }
        override fun upgradesTo(type: LotBuildingType) = type == HOUSE
    },
    THEATER(
        cost = 24,
        size = BuildingSize.LARGE,
        economyBonus = 2,
        stabilityBonus = 2
    ) {
        override fun upgradesTo(type: LotBuildingType) = type == ARENA
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
        override fun validPosition(lots: Set<Lot>) = lots.any { l ->
            l.neighbors.any { it !in lots && it.building?.type?.countsAs(HOUSE) == true}
        }
        override fun upgradesTo(type: LotBuildingType) = type == GUILDHALL
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
    ) {
        override fun validPosition(lots: Set<Lot>) = lots.first().district.settlement.buildings.none { it.type == WATERFRONT }
                && lots.any { l ->
                    l.neighbors.any { it !in lots && it.building?.type?.countsAs(WATERWAY) == true}
                            || (l.coordinate.y == 0 && l.district.northBorder.hasWater)
                            || (l.coordinate.y == 5 && l.district.southBorder.hasWater)
                            || (l.coordinate.x == 0 && l.district.westBorder.hasWater)
                            || (l.coordinate.x == 5 && l.district.eastBorder.hasWater)
        }
    },
    WATERWAY(
        cost = 3,
        size = BuildingSize.NORMAL_OR_LARGE
    );

    open fun countsAs(vararg type: BuildingType) = type.any { it == this }
    open fun upgradesTo(type: LotBuildingType) = false
    open fun validPosition(lots: Set<Lot>) = true
    fun eligible(lots: Set<Lot>): Boolean {
        val buildings = lots.mapNotNull { it.building }.distinctBy { it.id }
        return buildings.size <= 1
                && size.isSize(lots.size)
                && buildings.firstOrNull()?.type?.let{ if (it == this) return@eligible true else it.upgradesTo(this) } != false
                && validPosition(lots)
    }

    override val displayName = name.split("_").joinToString(" ") { it.lowercase().replaceFirstChar { c -> c.uppercase() } }

    enum class BuildingSize {
        NORMAL,
        NORMAL_OR_LARGE {
            override fun isSize(size: BuildingSize) = size == NORMAL || size == NORMAL_OR_LARGE || size == LARGE
        },
        LARGE,
        HUGE;

        open fun isSize(size: BuildingSize) = this == size
        open fun isSize(size: Int) = when(size) {
            1 -> isSize(NORMAL)
            2 -> isSize(LARGE)
            4 -> isSize(HUGE)
            else -> false
        }
    }
}
