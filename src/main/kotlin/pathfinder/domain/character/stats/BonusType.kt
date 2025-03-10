package pathfinder.domain.character.stats

import pathfinder.domain.character.stats.ACBonus.ArmorClass
import pathfinder.domain.character.stats.AbilityScore.*
import java.io.Serializable
import kotlin.reflect.KClass

enum class BonusType(vararg val applicableStats: KClass<out Stat>): Serializable {
    ALCHEMICAL(AbilityScore::class, Save::class),
    ARMOR(ArmorClass::class),
    CIRCUMSTANCE(Skill::class),
    COMPETENCE(Skill::class, Save::class),
    DEFLECTION(ArmorClass::class),
    DODGE(ArmorClass::class) {
        override val stacks = true
    },
    ENHANCEMENT(AbilityScore::class, ACBonus::class, Speed::class),
    INHERENT(AbilityScore::class),
    INSIGHT(ACBonus::class, Skill::class, Save::class),
    LUCK(ACBonus::class, Skill::class, Save::class),
    MORALE(Skill::class, Save::class, Strength::class, Constitution::class, Dexterity::class),
    NATURAL_ARMOR(ArmorClass::class),
    PROFANE(ArmorClass::class, Skill::class, Save::class),
    RACIAL {
        override val stacks = true
    },
    RESISTANCE(Save::class),
    SACRED(ArmorClass::class, Skill::class, Save::class),
    SHIELD(ArmorClass::class),
    SIZE(AbilityScore::class, ArmorClass::class),
    TRAIT,
    TYPELESS {
        override val stacks = true
    };
    open val stacks = false
}