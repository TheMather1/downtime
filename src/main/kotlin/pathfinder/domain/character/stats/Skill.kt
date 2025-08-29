package pathfinder.domain.character.stats

import jakarta.persistence.*
import pathfinder.domain.character.PathfinderCharacter
import pathfinder.domain.support.jpa.BonusConverter

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
sealed class Skill(
    @ManyToOne(fetch = FetchType.EAGER)
    var character: PathfinderCharacter,
    @Enumerated(EnumType.STRING)
    var score: AbilityScore.Value,
): Stat() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
    var ranks = 0
    @Column(columnDefinition = "CLOB", nullable = true)
    @Convert(converter = BonusConverter::class)
    override val bonuses: BonusSet = BonusSet()


    @get:Transient
    @set:Transient
    override var base
        get() = ranks + scoreMod
        set(_) = throw IllegalArgumentException("Cannot set base value for skill. Are you trying to set ranks?")
    @get:Transient
    private val scoreMod
        get() = character.abilityScores[score].modifier

    @Entity
    class Acrobatics(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.DEXTERITY)
    @Entity
    class Appraise(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.INTELLIGENCE)
    @Entity
    class Bluff(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.CHARISMA)
    @Entity
    class Climb(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.DEXTERITY)
    @Entity
    class Craft(character: PathfinderCharacter, val type: String) : Skill(character, AbilityScore.Value.INTELLIGENCE) {
        override val displayName = "Craft($type)"
        override val valueName = "craft${type.split(" ").joinToString("") { it.replaceFirstChar { it.uppercase() } }}"
    }
    @Entity
    class Diplomacy(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.CHARISMA)
    @Entity
    class DisableDevice(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.DEXTERITY)
    @Entity
    class Disguise(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.CHARISMA)
    @Entity
    class EscapeArtist(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.DEXTERITY)
    @Entity
    class Fly(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.WISDOM)
    @Entity
    class HandleAnimal(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.WISDOM)
    @Entity
    class Heal(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.WISDOM)
    @Entity
    class Intimidate(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.CHARISMA)
    @Entity
    class KnowledgeArcana(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.INTELLIGENCE)
    @Entity
    class KnowledgeDungeoneering(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.INTELLIGENCE)
    @Entity
    class KnowledgeEngineering(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.INTELLIGENCE)
    @Entity
    class KnowledgeGeography(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.INTELLIGENCE)
    @Entity
    class KnowledgeHistory(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.INTELLIGENCE)
    @Entity
    class KnowledgeLocal(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.INTELLIGENCE)
    @Entity
    class KnowledgeNature(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.INTELLIGENCE)
    @Entity
    class KnowledgeNobility(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.INTELLIGENCE)
    @Entity
    class KnowledgePlanes(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.INTELLIGENCE)
    @Entity
    class KnowledgeReligion(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.INTELLIGENCE)
    @Entity
    class Linguistics(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.INTELLIGENCE)
    @Entity
    class Perception(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.WISDOM)
    @Entity
    class Perform(character: PathfinderCharacter, val type: String) : Skill(character, AbilityScore.Value.CHARISMA) {
        override val displayName = "Perform($type)"
        override val valueName = "perform${type.split(" ").joinToString("") { it.replaceFirstChar { it.uppercase() } }}"
    }
    @Entity
    class Profession(character: PathfinderCharacter, val type: String) : Skill(character, AbilityScore.Value.WISDOM) {
        override val displayName = "Profession($type)"
        override val valueName = "profession${type.split(" ").joinToString("") { it.replaceFirstChar { it.uppercase() } }}"
    }
    @Entity
    class Ride(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.DEXTERITY)
    @Entity
    class SenseMotive(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.WISDOM)
    @Entity
    class SleightOfHand(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.DEXTERITY)
    @Entity
    class Spellcraft(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.INTELLIGENCE)
    @Entity
    class Stealth(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.DEXTERITY)
    @Entity
    class Survival(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.WISDOM)
    @Entity
    class Swim(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.WISDOM)
    @Entity
    class UseMagicDevice(character: PathfinderCharacter) : Skill(character, AbilityScore.Value.CHARISMA)
    @Entity
    class Custom(character: PathfinderCharacter, score: AbilityScore.Value, val type: String) : Skill(character, score) {
        override val displayName = type
        override val valueName = "customSkill${type.split(" ").joinToString("") { it.replaceFirstChar { it.uppercase() } }}"
    }
}
