package pathfinder.domain.character.stats

import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import pathfinder.domain.character.PathfinderCharacter

@Embeddable
@Inheritance(strategy = InheritanceType.JOINED)
sealed class Save(
    @Enumerated(EnumType.STRING)
    var abilityScore: AbilityScore.Value,
): Stat(0) {
    @Embeddable
    class Fortitude: Save(AbilityScore.Value.CONSTITUTION)
    @Embeddable
    class Reflex: Save(AbilityScore.Value.DEXTERITY)
    @Embeddable
    class Willpower: Save(AbilityScore.Value.WISDOM)

    fun bonus(character: PathfinderCharacter) = value + character.abilityScores[abilityScore].bonus
}