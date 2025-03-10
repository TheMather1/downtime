package pathfinder.domain.character.stats

import jakarta.persistence.Embeddable
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType

@Embeddable
@Inheritance(strategy = InheritanceType.JOINED)
sealed class Skill: Stat(0) {
}