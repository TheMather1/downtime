package pathfinder.downtimeBot.domain

import java.io.Serializable

open class Building(val name: String, val owner: Character?, val rooms: List<Room>): Serializable {
    infix fun produces(capital: Capital) = rooms.any { capital in it.earnings }

    fun bonusFor(capital: Capital?) = rooms.mapNotNull { it.bonusFor(this, capital) }
        .takeUnless { it.isEmpty() }
        ?.sum()
}
