package pathfinder.domain.kingdom

enum class KingdomAlignment(
    val economyBonus: Int = 0,
    val stabilityBonus: Int = 0,
    val loyaltyBonus: Int = 0,
) {
    LAWFUL_GOOD(
        economyBonus = 2,
        loyaltyBonus = 2
    ),
    NEUTRAL_GOOD(
        loyaltyBonus = 2,
        stabilityBonus = 2
    ),
    CHAOTIC_GOOD(
        loyaltyBonus = 4,
    ),
    LAWFUL_NEUTRAL(
        economyBonus = 2,
        stabilityBonus = 2
    ),
    TRUE_NEUTRAL(
        stabilityBonus = 4
    ),
    CHAOTIC_NEUTRAL(
        loyaltyBonus = 2,
        stabilityBonus = 2
    ),
    LAWFUL_EVIL(
        economyBonus = 4,
    ),
    NEUTRAL_EVIL(
        economyBonus = 2,
        stabilityBonus = 2
    ),
    CHAOTIC_EVIL(
        economyBonus = 2,
        loyaltyBonus = 2
    );

    val displayName = name.split('_').joinToString(" ") { it.lowercase().replaceFirstChar { it.uppercase() } }
}
