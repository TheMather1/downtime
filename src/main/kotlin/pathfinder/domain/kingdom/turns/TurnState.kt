package pathfinder.domain.kingdom.turns

import jakarta.persistence.*
import pathfinder.domain.kingdom.Kingdom
import java.time.LocalDate
import java.time.Month

//@Entity
class TurnState(
    @ManyToOne
    val kingdom: Kingdom
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
    @Enumerated(EnumType.STRING)
    var month: Month = Month.from(LocalDate.now())
    @Embedded
    var upkeepPhase = UpkeepPhase()
    var edictPhase = EdictPhase(kingdom)
//    var incomePhase = IncomePhase()
//    var eventPhase = EventPhase()
}