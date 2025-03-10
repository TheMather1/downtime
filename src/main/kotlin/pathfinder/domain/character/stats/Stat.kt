package pathfinder.domain.character.stats

import jakarta.persistence.*

@Embeddable
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Stat(open var base: Int = 0) {
    @Lob
    @Column(columnDefinition = "CLOB")
    val bonuses: MutableList<Bonus> = mutableListOf()

    val value
        get() = base + bonuses.groupBy(Bonus::type).map {
            if(it.key.stacks) it.value.sumOf { it.value }
            else it.value.maxOf { it.value }
        }.sum()
}