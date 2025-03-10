package pathfinder.domain.kingdom

enum class TaxationEdict(val economyBonus: Int, val loyaltyPenalty: Int) {
    NONE(-1, 0),
    LIGHT(1, 1),
    NORMAL(2, 2),
    HEAVY(3, 4),
    OVERWHELMING(4, 8);
}