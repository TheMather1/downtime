package pathfinder.downtimeBot.domain.rooms

import pathfinder.downtimeBot.domain.Capital.*
import pathfinder.downtimeBot.domain.Room

class AlchemyLab : Room(
    "Alchemy Lab",
    mapOf(GOODS to 8, INFLUENCE to 1, LABOR to 5, MAGIC to 1),
    setOf(GOODS, MAGIC),
    10,
    16,
    8..16,
    benefit = "Counts as an alchemist’s lab (equipment)."
)
