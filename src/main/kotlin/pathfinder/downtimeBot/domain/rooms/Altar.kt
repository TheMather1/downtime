package pathfinder.downtimeBot.domain.rooms

import pathfinder.downtimeBot.domain.Capital.*
import pathfinder.downtimeBot.domain.Room

class Altar : Room(
    "Altar",
    mapOf(GOODS to 2, INFLUENCE to 1, LABOR to 2),
    setOf(INFLUENCE),
    3,
    4,
    2..8,
    benefit = "Counts as a permanent fixture dedicated to your deity for the purpose of consecrate and similar spells."
)
