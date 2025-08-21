package pathfinder.domain.character

import jakarta.persistence.*
import pathfinder.domain.character.stats.AbilityScore
import pathfinder.domain.character.stats.AbilityScore.*

@Embeddable
class AbilityScores {
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "base", column = Column(name = "str_base")),
        AttributeOverride(name = "bonuses", column = Column(name = "str_bonus"))
    )
    var strength = Strength()
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "base", column = Column(name = "dex_base")),
        AttributeOverride(name = "bonuses", column = Column(name = "dex_bonus"))
    )
    var dexterity = Dexterity()
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "base", column = Column(name = "con_base")),
        AttributeOverride(name = "bonuses", column = Column(name = "con_bonus"))
    )
    var constitution = Constitution()
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "base", column = Column(name = "int_base")),
        AttributeOverride(name = "bonuses", column = Column(name = "int_bonus"))
    )
    var intelligence = Intelligence()
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "base", column = Column(name = "wis_base")),
        AttributeOverride(name = "bonuses", column = Column(name = "wis_bonus"))
    )
    var wisdom = Wisdom()
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "base", column = Column(name = "cha_base")),
        AttributeOverride(name = "bonuses", column = Column(name = "cha_bonus"))
    )
    var charisma = Charisma()

    @get:Transient
    val scores
        get() = setOf(strength, dexterity, constitution, intelligence, wisdom, charisma)

    operator fun get(enum: Value) = when (enum) {
        Value.STRENGTH -> strength
        Value.DEXTERITY -> dexterity
        Value.CONSTITUTION -> constitution
        Value.INTELLIGENCE -> intelligence
        Value.WISDOM -> wisdom
        Value.CHARISMA -> charisma
    }

    companion object {
        @JvmStatic
        val proxy
            get() = AbilityScores()
    }
}
