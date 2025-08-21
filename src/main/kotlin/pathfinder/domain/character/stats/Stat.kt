package pathfinder.domain.character.stats

import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Transient

@MappedSuperclass
abstract class Stat() {
    abstract var base: Int
    abstract val bonuses: BonusSet

    @get:Transient
    val applicableBonuses
        get() = BonusType.applicableTo(this)

    @get:Transient
    val value
        get() = base + bonuses.sum()
}
