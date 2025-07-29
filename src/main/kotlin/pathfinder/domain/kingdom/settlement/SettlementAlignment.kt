package pathfinder.domain.kingdom.settlement

enum class SettlementAlignment(
    val corruptionBonus: Int = 0,
    val crimeBonus: Int = 0,
    val lawBonus: Int = 0,
    val loreBonus: Int = 0,
    val societyBonus: Int = 0
) {
    LAWFUL_GOOD(
        lawBonus = 1,
        societyBonus = 1
    ),
    NEUTRAL_GOOD(
        loreBonus = 1,
        societyBonus = 1
    ),
    CHAOTIC_GOOD(
        crimeBonus = 1,
        societyBonus = 1
    ),
    LAWFUL_NEUTRAL(
        lawBonus = 1,
        loreBonus = 1
    ),
    TRUE_NEUTRAL(
        loreBonus = 2
    ),
    CHAOTIC_NEUTRAL(
        crimeBonus = 1,
        loreBonus = 1
    ),
    LAWFUL_EVIL(
        lawBonus = 1,
        corruptionBonus = 1
    ),
    NEUTRAL_EVIL(
        loreBonus = 1,
        corruptionBonus = 1
    ),
    CHAOTIC_EVIL(
        crimeBonus = 1,
        corruptionBonus = 1
    );

    val displayName = name.split('_').joinToString(" ") { it.lowercase().replaceFirstChar { it.uppercase() } }
}
