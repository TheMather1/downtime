package pathfinder.domain.kingdom.leadership

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import pathfinder.domain.kingdom.KingdomScore

@Embeddable
class RulerDuties {
    @Column(name = "ruler_first_score")
    var firstScore: KingdomScore? = null
        set(value) {
            field = when(value) {
                null -> value
                secondScore, thirdScore -> throw IllegalArgumentException()
                else -> value
            }
        }
    @Column(name = "ruler_second_score")
    var secondScore: KingdomScore? = null
        set(value) {
            field = when(value) {
                null -> value
                firstScore, thirdScore -> throw IllegalArgumentException()
                else -> value
            }
        }
    @Column(name = "ruler_third_score")
    var thirdScore: KingdomScore? = null
        set(value) {
            field = when(value) {
                null -> value
                firstScore, secondScore -> throw IllegalArgumentException()
                else -> value
            }
        }
    fun selected(score: KingdomScore) = firstScore == score || secondScore == score || thirdScore == score
}
