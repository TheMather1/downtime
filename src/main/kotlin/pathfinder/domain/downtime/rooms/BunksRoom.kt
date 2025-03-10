package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.Room

class Bunks : Room(
    name = "Bunks",
    create = mapOf(Capital.GOODS to 7, Capital.INFLUENCE to 4, Capital.LABOR to 7),
    earnings = setOf(Capital.LABOR),
    bonus = 8,
    time = 24,
    size = 15..35,
)