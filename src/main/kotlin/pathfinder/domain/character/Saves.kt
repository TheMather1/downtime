package pathfinder.domain.character

import jakarta.persistence.Embeddable
import pathfinder.domain.character.stats.Save.*

@Embeddable
class Saves {
    val fortitude = Fortitude()
    val reflex = Reflex()
    val willpower = Willpower()
}