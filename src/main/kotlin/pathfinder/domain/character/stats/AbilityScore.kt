package pathfinder.domain.character.stats

import jakarta.persistence.Embeddable
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import kotlin.math.truncate

@Embeddable
@Inheritance(strategy = InheritanceType.JOINED)
sealed class AbilityScore: Stat(10) {
    @Embeddable
    class Strength: AbilityScore()
    @Embeddable
    class Dexterity: AbilityScore()
    @Embeddable
    class Constitution: AbilityScore()
    @Embeddable
    class Intelligence: AbilityScore()
    @Embeddable
    class Wisdom: AbilityScore()
    @Embeddable
    class Charisma: AbilityScore()

    val bonus
        get() = truncate((value-10)/2.0).toInt()

    enum class Value {
        STRENGTH,
        DEXTERITY,
        CONSTITUTION,
        INTELLIGENCE,
        WISDOM,
        CHARISMA;
    }
}