package pathfinder.domain.character.stats

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import pathfinder.domain.support.jpa.BonusConverter

@MappedSuperclass
sealed class Speed(): Stat() {
    @Embeddable
    class LandSpeed(): Speed() {
        override var base = 30
        @Column(columnDefinition = "CLOB")
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
    @Embeddable
    class FlySpeed(
        @Enumerated(EnumType.STRING)
        val maneuverability: Maneuverability = Maneuverability.AVERAGE
    ): Speed() {
        @ColumnDefault("0")
        override var base = 0
        @Column(columnDefinition = "CLOB", nullable = true)
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()

        enum class Maneuverability(val modifier: Int) {
            CLUMSY(-8),
            POOR(-4),
            AVERAGE(0),
            GOOD(4),
            PERFECT(8)
        }
    }
    @Embeddable
    class SwimSpeed(): Speed() {
        @ColumnDefault("0")
        override var base = 0
        @Column(columnDefinition = "CLOB", nullable = true)
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
    @Embeddable
    class ClimbSpeed(): Speed() {
        @ColumnDefault("0")
        override var base = 0
        @Column(columnDefinition = "CLOB", nullable = true)
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
    @Embeddable
    class BurrowSpeed(): Speed() {
        @ColumnDefault("0")
        override var base = 0
        @Column(columnDefinition = "CLOB", nullable = true)
        @Convert(converter = BonusConverter::class)
        override val bonuses: BonusSet = BonusSet()
    }
}
