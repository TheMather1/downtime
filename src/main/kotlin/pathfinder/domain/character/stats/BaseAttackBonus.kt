package pathfinder.domain.character.stats

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Embeddable
import pathfinder.domain.support.jpa.BonusConverter

@Embeddable
open class BaseAttackBonus: Stat() {
    override var base = 0

    @Column(columnDefinition = "CLOB")
    @Convert(converter = BonusConverter::class)
    override val bonuses: BonusSet = BonusSet()
}
