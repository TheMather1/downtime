package pathfinder.domain.character.stats

import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType

@Embeddable
@Inheritance(strategy = InheritanceType.JOINED)
sealed class Speed(speed: Int): Stat(base = speed) {
    @Embeddable
    class LandSpeed(speed: Int = 30): Speed(speed)
    @Embeddable
    class FlySpeed(
        speed: Int = 0,
        @Enumerated(EnumType.STRING)
        val maneuverability: Maneuverability = Maneuverability.AVERAGE
    ): Speed(speed) {
        enum class Maneuverability(val modifier: Int) {
            CLUMSY(-8),
            POOR(-4),
            AVERAGE(0),
            GOOD(4),
            PERFECT(8)
        }
    }
    @Embeddable
    class SwimSpeed(speed: Int = 0): Speed(speed)
    @Embeddable
    class ClimbSpeed(speed: Int = 0): Speed(speed)
    @Embeddable
    class BurrowSpeed(speed: Int = 0): Speed(speed)
}