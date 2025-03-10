package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.Room

class ArcheryRange : Room(
    "Archery Range",
    mapOf(Capital.GOODS to 12, Capital.LABOR to 12),
    setOf(Capital.INFLUENCE),
    8,
    25,
    20..50,
    benefit = "Decreased chance of fired arrows being destroyed or lost."
)
