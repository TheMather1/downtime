package pathfinder.domain.character.stats

import jakarta.persistence.*
import pathfinder.domain.character.PathfinderCharacter
import pathfinder.domain.support.jpa.BonusConverter

@MappedSuperclass
sealed class Save(): Stat() {
    abstract var abilityScore: AbilityScore.Value

    @Embeddable
    class Fortitude: Save() {
        override var base = 0
        @Enumerated(EnumType.STRING)
        override var abilityScore: AbilityScore.Value = AbilityScore.Value.CONSTITUTION

        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
    @Embeddable
    class Reflex: Save() {
        override var base = 0
        @Enumerated(EnumType.STRING)
        override var abilityScore: AbilityScore.Value = AbilityScore.Value.DEXTERITY

        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
    @Embeddable
    class Willpower: Save() {
        override var base = 0
        @Enumerated(EnumType.STRING)
        override var abilityScore: AbilityScore.Value = AbilityScore.Value.WISDOM

        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }

    fun bonus(character: PathfinderCharacter) = value + character.abilityScores[abilityScore].bonus
}
