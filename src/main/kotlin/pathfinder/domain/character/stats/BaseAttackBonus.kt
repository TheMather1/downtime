package pathfinder.domain.character.stats

import jakarta.persistence.Embeddable

@Embeddable
open class BaseAttackBonus: Stat() {
    override var base = 0
}