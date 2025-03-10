package pathfinder.domain.downtime

import java.io.Serializable

open class DowntimeBuilding(val name: String, val owner: Character?, val rooms: List<Room>): Serializable {
    infix fun produces(capital: Capital) = rooms.any { capital in it.earnings }

    fun bonusFor(capital: Capital?) = rooms.mapNotNull { it.bonusFor(this, capital) }
        .takeUnless { it.isEmpty() }
        ?.sum()
}
