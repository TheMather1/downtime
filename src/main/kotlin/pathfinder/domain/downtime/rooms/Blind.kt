package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.Room

class Blind : Room(
    name = "Blind",
    create = mapOf(Capital.GOODS to 3, Capital.INFLUENCE to 2, Capital.LABOR to 3),
    earnings = setOf(Capital.INFLUENCE),
    bonus = 2,
    time = 10,
    size = 10..30,
    benefit = "Perception check required to see through"
)