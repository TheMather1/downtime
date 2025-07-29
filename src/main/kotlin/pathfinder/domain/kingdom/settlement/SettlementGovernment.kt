package pathfinder.domain.kingdom.settlement

enum class SettlementGovernment(
    val corruptionBonus: Int = 0,
    val crimeBonus: Int = 0,
    val lawBonus: Int = 0,
    val loreBonus: Int = 0,
    val productivityBonus: Int = 0,
    val societyBonus: Int = 0,
    val spellcastingBonus: Int = 0,
) {
    AUTOCRACY,
    COUNCIL(
        societyBonus = 4,
        lawBonus = -2,
        loreBonus = -2
    ),
    MAGICAL(
        loreBonus = 2,
        corruptionBonus = -2,
        societyBonus = -2,
        spellcastingBonus = 1,
    ),
    OVERLORD(
        corruptionBonus = 2,
        lawBonus = 2,
        crimeBonus = -2,
        societyBonus = -2,
    ),
    SECRET_SYNDICATE(
        corruptionBonus = 2,
        crimeBonus = 2,
        productivityBonus = 2,
        lawBonus = -6
    );

    val displayName = name.split('_').joinToString(" ") { it.lowercase().replaceFirstChar { it.uppercase() } }
}
