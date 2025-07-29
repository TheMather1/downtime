package pathfinder.domain.character

import jakarta.persistence.*
import pathfinder.domain.Campaign
import pathfinder.domain.character.stats.ACBonus.ArmorClass
import pathfinder.domain.character.stats.BaseAttackBonus
import pathfinder.domain.character.stats.Speeds
import pathfinder.domain.downtime.Assets

@Entity
class PathfinderCharacter(
    var name: String,
    var race: String,
    var ownerId: Long,
    @ManyToOne
    val campaign: Campaign
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
    val abilityScores = AbilityScores()
    val armorClass = ArmorClass()
    val baseAttackBonus = BaseAttackBonus()
    val saves = Saves()
    val speeds = Speeds()
    val assets = Assets()
}
