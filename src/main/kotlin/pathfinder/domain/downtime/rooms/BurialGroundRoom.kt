package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.Room

class BurialGround : Room(
    name = "Burial Ground",
    create = mapOf(Capital.GOODS to 4, Capital.INFLUENCE to 3, Capital.LABOR to 4, Capital.MAGIC to 1),
    earnings = setOf(Capital.INFLUENCE),
    bonus = 4,
    time = 8,
    size = 20..30,
    benefit = "prevents or creates undead"
)