package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.Room

class AnimalPen : Room(
    "Animal Pen",
    mapOf(Capital.GOODS to 6, Capital.INFLUENCE to 1, Capital.LABOR to 5),
    setOf(Capital.GOODS, Capital.LABOR),
    8,
    16,
    4..16
)
