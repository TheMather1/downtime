package pathfinder.downtimeBot.domain.rooms

import pathfinder.downtimeBot.domain.Capital.*
import pathfinder.downtimeBot.domain.Room

class ArcheryRange : Room(
    "Archery Range",
    mapOf(GOODS to 12, LABOR to 12),
    setOf(INFLUENCE),
    8,
    25,
    20..50,
    benefit = "Decreased chance of fired arrows being destroyed or lost."
)
