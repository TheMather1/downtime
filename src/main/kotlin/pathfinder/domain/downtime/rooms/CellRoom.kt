package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.Room

class Cell : Room(
    name = "Cell",
    create = mapOf(Capital.GOODS to 5, Capital.LABOR to 4),
    earnings = emptySet(),
    bonus = 0,
    time = 16,
    size = 1..9
)