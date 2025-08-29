package pathfinder.domain.character.stats

import jakarta.annotation.Nullable
import jakarta.persistence.AttributeOverride
import jakarta.persistence.AttributeOverrides
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded

//import jakarta.persistence.Embedded

@Embeddable
class Speeds {
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "base", column = Column(name = "speed_base")),
        AttributeOverride(name = "bonuses", column = Column(name = "speed_bonus"))
    )
    val landSpeed = Speed.LandSpeed()
    @Embedded
    @Nullable
    @AttributeOverrides(
        AttributeOverride(name = "base", column = Column(name = "swim_base")),
        AttributeOverride(name = "bonuses", column = Column(name = "swim_bonus"))
    )
    val swimSpeed: Speed.SwimSpeed? = null
    @Embedded
    @Nullable
    @AttributeOverrides(
        AttributeOverride(name = "base", column = Column(name = "climb_base")),
        AttributeOverride(name = "bonuses", column = Column(name = "climb_bonus"))
    )
    val climbSpeed: Speed.ClimbSpeed? = null
    @Embedded
    @Nullable
    @AttributeOverrides(
        AttributeOverride(name = "base", column = Column(name = "burrow_base")),
        AttributeOverride(name = "bonuses", column = Column(name = "burrow_bonus"))
    )
    val burrowSpeed: Speed.BurrowSpeed? = null
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "base", column = Column(name = "fly_base")),
        AttributeOverride(name = "bonuses", column = Column(name = "fly_bonus"))
    )
    val flySpeed: Speed.FlySpeed? = null
}
