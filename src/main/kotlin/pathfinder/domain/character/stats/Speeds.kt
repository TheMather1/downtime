package pathfinder.domain.character.stats

import jakarta.persistence.Embeddable

@Embeddable
class Speeds {
    val landBonus = Speed.LandSpeed()
    val swimSpeed: Speed.SwimSpeed? = null
    val climbSpeed: Speed.ClimbSpeed? = null
    val burrowSpeed: Speed.BurrowSpeed? = null
    val flySpeed: Speed.FlySpeed? = null
}