package pathfinder.downtimeBot.domain.rooms

import pathfinder.downtimeBot.domain.Capital.*
import pathfinder.downtimeBot.domain.Room

class AnimalPen : Room(
    "Animal Pen",
    mapOf(GOODS to 6, INFLUENCE to 1, LABOR to 5),
    setOf(GOODS, LABOR),
    8,
    16,
    4..16
)
