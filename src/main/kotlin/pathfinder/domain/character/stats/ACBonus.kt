package pathfinder.domain.character.stats

import jakarta.persistence.*
import pathfinder.domain.character.PathfinderCharacter
import pathfinder.domain.support.jpa.BonusConverter

@MappedSuperclass
sealed class ACBonus(): Stat() {
    @Embeddable
    class ArmorBonus: ACBonus() {
        override var base = 0
        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
    @Embeddable
    class NaturalArmorBonus: ACBonus() {
        override var base = 0
        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
    @Embeddable
    class ShieldBonus: ACBonus() {
        override var base = 0
        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
    @Embeddable
    class ArmorClass: ACBonus() {
        override var base = 10
        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
        @Embedded
        @AttributeOverrides(
            AttributeOverride(name = "base", column = Column(name = "armor_base")),
            AttributeOverride(name = "bonuses", column = Column(name = "armor_bonus"))
        )
        val armorBonus = ArmorBonus()
        @Embedded
        @AttributeOverrides(
            AttributeOverride(name = "base", column = Column(name = "shield_base")),
            AttributeOverride(name = "bonuses", column = Column(name = "shield_bonus"))
        )
        val shieldBonus = ShieldBonus()
        @Embedded
        @AttributeOverrides(
            AttributeOverride(name = "base", column = Column(name = "natural_armor_base")),
            AttributeOverride(name = "bonuses", column = Column(name = "natural_armor_bonus"))
        )
        val naturalArmorBonus = NaturalArmorBonus()

        fun ac(character: PathfinderCharacter) = value + character.abilityScores.dexterity.bonus + armorBonus.value + shieldBonus.value + naturalArmorBonus.value
        fun flatFooted(conditions: List<*>) = value + armorBonus.value + shieldBonus.value + naturalArmorBonus.value
        fun touch(character: PathfinderCharacter) = value + character.abilityScores.dexterity.bonus
    }
}
