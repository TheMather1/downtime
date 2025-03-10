package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.Room

class Bedroom : Room(
    "Bedroom",
    mapOf(Capital.GOODS to 8, Capital.LABOR to 7),
    setOf(Capital.INFLUENCE),
    3,
    20,
    4..8
)