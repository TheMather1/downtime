package pathfinder.domain.kingdom

enum class PromotionEdict(val stabilityBonus: Int, val consumptionPenalty: Int) {
    NONE(-1, 0),
    TOKEN(1, 1),
    STANDARD(2, 2),
    AGGRESSIVE(3, 4),
    EXPANSIONIST(4, 8);
}