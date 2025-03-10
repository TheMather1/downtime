package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.Room

class Bar : Room(
    "Bar",
    mapOf(Capital.GOODS to 6, Capital.INFLUENCE to 1, Capital.LABOR to 5),
    setOf(Capital.INFLUENCE),
    10,
    16,
    10..20,
    benefit = "Bonus on Diplomacy checks to gather information."
)