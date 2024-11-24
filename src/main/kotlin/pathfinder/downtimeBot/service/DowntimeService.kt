package pathfinder.downtimeBot.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import pathfinder.downtimeBot.domain.Building
import pathfinder.downtimeBot.domain.Capital
import kotlin.random.Random
import kotlin.random.nextInt

@Service
class DowntimeService {

    @Scheduled(cron = "@daily")
    fun execute() {

    }

    fun incomePhase(buildings: List<Building>) {
        buildings.forEach { building ->
            building.owner?.run {
                building.bonusFor(null)?.let {
                    funds += it.plus(Random.nextInt(1..20)).div(10.0)
                }
                Capital.values().map {
                    val roll = building.bonusFor(it)?.plus(Random.nextInt(1..20))
                    val earned = roll?.div(10)?.coerceAtMost(funds.div(it.value).toInt())
                    if (earned != null && earned > 0) {
                        capital.compute(it) { _, value -> value?.plus(earned) ?: earned }
                        funds -= earned * it.value
                    }
                }
            }
        }
    }

}
