package pathfinder.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import pathfinder.domain.downtime.DowntimeBuilding
import pathfinder.domain.downtime.Capital
import kotlin.random.Random
import kotlin.random.nextInt

@Service
class DowntimeService {

    @Scheduled(cron = "@daily")
    fun execute() {

    }

    fun incomePhase(buildings: List<DowntimeBuilding>) {
        buildings.forEach { building ->
            building.owner?.assets?.run {
                building.bonusFor(null)?.let {
                    gold += it.plus(Random.nextInt(1..20)).div(10.0)
                }
                Capital.entries.map {
                    val roll = building.bonusFor(it)?.plus(Random.nextInt(1..20))
                    val earned = roll?.div(10)?.coerceAtMost(gold.div(it.value).toInt())
                    if (earned != null && earned > 0) {
                        addCapital(it, earned)
                        gold -= earned * it.value
                    }
                }
            }
        }
    }

}
