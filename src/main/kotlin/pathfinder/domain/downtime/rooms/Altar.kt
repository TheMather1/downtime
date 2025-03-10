package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.Room

class Altar : Room(
    "Altar",
    mapOf(Capital.GOODS to 2, Capital.INFLUENCE to 1, Capital.LABOR to 2),
    setOf(Capital.INFLUENCE),
    3,
    4,
    2..8,
    benefit = "Counts as a permanent fixture dedicated to your deity for the purpose of consecrate and similar spells."
)
