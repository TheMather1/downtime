package pathfinder.domain.character

import jakarta.persistence.*
import pathfinder.domain.character.stats.Skill

@Embeddable
class Skills(
    character: PathfinderCharacter
) {
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val acrobatics = Skill.Acrobatics(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val appraise = Skill.Appraise(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val bluff = Skill.Bluff(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val climb = Skill.Climb(character)
    @OneToMany(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val crafts: MutableSet<Skill.Craft> = mutableSetOf()
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val diplomacy = Skill.Diplomacy(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val disableDevice = Skill.DisableDevice(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val disguise = Skill.Disguise(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val escapeArtist = Skill.EscapeArtist(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val fly = Skill.Fly(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val handleAnimal = Skill.HandleAnimal(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val heal = Skill.Heal(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val intimidate = Skill.Intimidate(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val knowledgeArcana = Skill.KnowledgeArcana(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val knowledgeDungeoneering = Skill.KnowledgeDungeoneering(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val knowledgeEngineering = Skill.KnowledgeEngineering(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val knowledgeGeography = Skill.KnowledgeGeography(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val knowledgeHistory = Skill.KnowledgeHistory(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val knowledgeLocal = Skill.KnowledgeLocal(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val knowledgeNature = Skill.KnowledgeNature(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val knowledgeNobility = Skill.KnowledgeNobility(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val knowledgePlanes = Skill.KnowledgePlanes(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val knowledgeReligion = Skill.KnowledgeReligion(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val linguistics = Skill.Linguistics(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val perception = Skill.Perception(character)
    @OneToMany(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val perform: MutableSet<Skill.Perform> = mutableSetOf()
    @OneToMany(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val profession: MutableSet<Skill.Profession> = mutableSetOf()
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val ride = Skill.Ride(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val senseMotive = Skill.SenseMotive(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val sleightOfHand = Skill.SleightOfHand(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val spellcraft = Skill.Spellcraft(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val stealth = Skill.Stealth(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val survival = Skill.Survival(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val swim = Skill.Swim(character)
    @OneToOne(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val useMagicDevice = Skill.UseMagicDevice(character)
    @OneToMany(mappedBy = "character", cascade = [CascadeType.ALL], orphanRemoval = true)
    val customSkills: MutableSet<Skill.Custom> = mutableSetOf()

    @get:Transient
    val allSkills: List<Skill>
        get() = listOf(
            acrobatics, appraise, bluff, climb, *crafts.toTypedArray(), diplomacy, disableDevice, disguise,
            escapeArtist, fly, handleAnimal, heal, intimidate, knowledgeArcana, knowledgeDungeoneering,
            knowledgeEngineering, knowledgeGeography, knowledgeHistory, knowledgeLocal, knowledgeNature,
            knowledgeNobility, knowledgePlanes, knowledgeReligion, linguistics, perception, *perform.toTypedArray(),
            *profession.toTypedArray(), ride, senseMotive, sleightOfHand, spellcraft, stealth, survival, swim,
            useMagicDevice, *customSkills.toTypedArray()
        )

    operator fun get(string: String) = allSkills.firstOrNull { it.valueName == string }
}
