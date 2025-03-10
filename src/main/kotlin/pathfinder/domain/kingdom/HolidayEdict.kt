package pathfinder.domain.kingdom

enum class HolidayEdict(val printName: String, val loyaltyBonus: Int, val consumptionPenalty: Int) {
    NONE("None", -1, 0),
    ONE("1", 1, 1),
    FEW("6", 2, 2),
    SOME("12", 3, 4),
    MANY("24", 4, 8);
}