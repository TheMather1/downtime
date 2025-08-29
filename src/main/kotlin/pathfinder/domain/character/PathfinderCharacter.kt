package pathfinder.domain.character

import jakarta.persistence.*
import pathfinder.domain.Campaign
import pathfinder.domain.character.stats.ACBonus.ArmorClass
import pathfinder.domain.character.stats.Speeds
import pathfinder.domain.downtime.Assets

@Entity
class PathfinderCharacter(
    var name: String,
    var race: String,
    var ownerId: Long?,
    @ManyToOne
    val campaign: Campaign
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
    @Embedded
    val abilityScores = AbilityScores()
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "base", column = Column(name = "ac_base")),
        AttributeOverride(name = "bonuses", column = Column(name = "ac_bonus"))
    )
    val armorClass = ArmorClass()
    val baseAttackBonus = 0
    @Embedded
    val saves = Saves()
    @Embedded
    var skills = Skills(this)
    @Embedded
    val speeds = Speeds()
    @Embedded
    val assets = Assets()
    var leadershipFeat = false

    fun getStat(stat: String) = when (stat) {
        "strength" -> abilityScores.strength
        "dexterity" -> abilityScores.dexterity
        "constitution" -> abilityScores.constitution
        "intelligence" -> abilityScores.intelligence
        "wisdom" -> abilityScores.wisdom
        "charisma" -> abilityScores.charisma
        "armorClass" -> armorClass
        "armorBonus" -> armorClass.armorBonus
        "shieldBonus" -> armorClass.shieldBonus
        "naturalArmorBonus" -> armorClass.naturalArmorBonus
        "landSpeed" -> speeds.landSpeed
        "flySpeed" -> speeds.flySpeed
        "swimSpeed" -> speeds.swimSpeed
        "burrowSpeed" -> speeds.burrowSpeed
        "reflex" -> saves.reflex
        "willpower" -> saves.willpower
        "fortitude" -> saves.fortitude
        else -> skills[stat] ?: throw IllegalArgumentException("Unknown stat: $stat")
    }
}
