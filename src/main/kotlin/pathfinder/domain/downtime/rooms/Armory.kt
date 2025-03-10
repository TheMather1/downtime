package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.Room

class Armory : Room(
    "Armory",
    mapOf(Capital.GOODS to 9, Capital.INFLUENCE to 3, Capital.LABOR to 6),
    setOf(),
    0,
    16,
    5..15,
    benefit = "Provides for 1 Bunks or Guard Post, hastens donning armor."
)