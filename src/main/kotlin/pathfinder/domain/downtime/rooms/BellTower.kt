package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.Room

class BellTower : Room(
    name = "Bell Tower",
    create = mapOf(Capital.GOODS to 11, Capital.INFLUENCE to 3, Capital.LABOR to 7),
    earnings = emptySet(),
    producesExisting = true,
    bonus = 1,
    time = 28,
    size = 9..25,
    benefit = "capital +1 (of a type the building already generates)"
)