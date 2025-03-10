package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.Room

class Brewery : Room(
    name = "Brewery",
    create = mapOf(Capital.GOODS to 9, Capital.INFLUENCE to 2, Capital.LABOR to 7),
    earnings = setOf(Capital.INFLUENCE),
    bonus = 10,
    time = 24,
    size = 12..24,
)