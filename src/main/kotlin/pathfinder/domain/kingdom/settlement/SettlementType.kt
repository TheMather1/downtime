package pathfinder.domain.kingdom.settlement

enum class SettlementType(
    val modifiers: Int,
    val qualities: Int,
    val danger: Int,
    val baseValue: Int,
    val purchaseLimit: Int,
    val spellcasting: Int
) {
    THORP(-4, 1, -10, 50, 500, 1),
    HAMLET(-2, 1, -5, 200, 1000, 2),
    VILLAGE(-1, 2, 0, 500, 2500, 3),
    SMALL_TOWN(0, 2, 0, 1000, 5000, 4),
    LARGE_TOWN(0, 3, 5, 2000, 10000, 5),
    SMALL_CITY(1, 4, 5, 4000, 25000, 6),
    LARGE_CITY(2, 5, 10, 8000, 50000, 7),
    METROPOLIS(4, 6, 10, 16000, 100000, 8);
}