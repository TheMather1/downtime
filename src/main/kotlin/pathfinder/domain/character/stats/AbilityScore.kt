package pathfinder.domain.character.stats

import jakarta.persistence.*
import pathfinder.domain.support.jpa.BonusConverter
import kotlin.math.truncate

@MappedSuperclass
sealed class AbilityScore(
    @Transient
    val ability: Value
): Stat() {
    @Embeddable
    class Strength: AbilityScore(Value.STRENGTH) {
        override var base = 10
        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
    @Embeddable
    class Dexterity: AbilityScore(Value.DEXTERITY) {
        override var base = 10
        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
    @Embeddable
    class Constitution: AbilityScore(Value.CONSTITUTION) {
        override var base = 10
        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
    @Embeddable
    class Intelligence: AbilityScore(Value.INTELLIGENCE) {
        override var base = 10
        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
    @Embeddable
    class Wisdom: AbilityScore(Value.WISDOM) {
        override var base = 10
        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
    @Embeddable
    class Charisma: AbilityScore(Value.CHARISMA) {
        override var base = 10
        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }

    val bonus
        get() = truncate((value-10)/2.0).toInt()

    enum class Value {
        STRENGTH,
        DEXTERITY,
        CONSTITUTION,
        INTELLIGENCE,
        WISDOM,
        CHARISMA;

        val displayName = name.lowercase().replaceFirstChar { it.uppercase() }
    }
}
