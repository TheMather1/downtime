package pathfinder.domain.character.stats

import jakarta.persistence.*
import pathfinder.domain.support.jpa.BonusConverter
import kotlin.math.truncate

@MappedSuperclass
sealed class AbilityScore(
    @Transient
    val ability: Value,
    @Transient
    val shorthand: String
): Stat() {
    @Embeddable
    class Strength: AbilityScore(Value.STRENGTH, "STR") {
        override var base = 10
        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
    @Embeddable
    class Dexterity: AbilityScore(Value.DEXTERITY, "DEX") {
        override var base = 10
        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
    @Embeddable
    class Constitution: AbilityScore(Value.CONSTITUTION, "CON") {
        override var base = 10
        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
    @Embeddable
    class Intelligence: AbilityScore(Value.INTELLIGENCE, "INT") {
        override var base = 10
        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
    @Embeddable
    class Wisdom: AbilityScore(Value.WISDOM, "WIS") {
        override var base = 10
        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
    @Embeddable
    class Charisma: AbilityScore(Value.CHARISMA, "CHA") {
        override var base = 10
        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }

    val modifier
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
