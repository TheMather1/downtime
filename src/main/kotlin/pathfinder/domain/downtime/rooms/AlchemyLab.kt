package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.Room

class AlchemyLab : Room(
    "Alchemy Lab",
    mapOf(Capital.GOODS to 8, Capital.INFLUENCE to 1, Capital.LABOR to 5, Capital.MAGIC to 1),
    setOf(Capital.GOODS, Capital.MAGIC),
    10,
    16,
    8..16,
    benefit = "Counts as an alchemist’s lab (equipment)."
)
