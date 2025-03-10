package pathfinder.domain.character

import jakarta.persistence.Embeddable
import pathfinder.domain.character.stats.AbilityScore
import pathfinder.domain.character.stats.AbilityScore.Charisma
import pathfinder.domain.character.stats.AbilityScore.Constitution
import pathfinder.domain.character.stats.AbilityScore.Dexterity
import pathfinder.domain.character.stats.AbilityScore.Intelligence
import pathfinder.domain.character.stats.AbilityScore.Strength
import pathfinder.domain.character.stats.AbilityScore.Wisdom

@Embeddable
class AbilityScores {
    var strength = Strength()
    var dexterity = Dexterity()
    var constitution = Constitution()
    var intelligence = Intelligence()
    var wisdom = Wisdom()
    var charisma = Charisma()

    operator fun get(enum: AbilityScore.Value) = when (enum) {
        AbilityScore.Value.STRENGTH -> strength
        AbilityScore.Value.DEXTERITY -> dexterity
        AbilityScore.Value.CONSTITUTION -> constitution
        AbilityScore.Value.INTELLIGENCE -> intelligence
        AbilityScore.Value.WISDOM -> wisdom
        AbilityScore.Value.CHARISMA -> charisma
    }
}