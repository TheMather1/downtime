package pathfinder.domain.character.stats

import jakarta.persistence.Embeddable
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import pathfinder.domain.character.PathfinderCharacter

@Embeddable
@Inheritance(strategy = InheritanceType.JOINED)
sealed class ACBonus(base: Int = 0): Stat(base) {
    @Embeddable
    class ArmorBonus: ACBonus()
    @Embeddable
    class NaturalArmorBonus: ACBonus()
    @Embeddable
    class ShieldBonus: ACBonus()
    @Embeddable
    class ArmorClass: ACBonus(10) {
        val armorBonus = ArmorBonus()
        val shieldBonus = ShieldBonus()
        val naturalArmorBonus = NaturalArmorBonus()

        fun ac(character: PathfinderCharacter) = value + character.abilityScores.dexterity.bonus + armorBonus.value + shieldBonus.value + naturalArmorBonus.value
        fun flatFooted(conditions: List<*>) = value + armorBonus.value + shieldBonus.value + naturalArmorBonus.value
        fun touch(character: PathfinderCharacter) = value + character.abilityScores.dexterity.bonus
    }
}