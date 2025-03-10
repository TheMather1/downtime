package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.Room

class BloodSpa : Room(
    name = "Blood Spa",
    create = mapOf(Capital.GOODS to 10, Capital.INFLUENCE to 4, Capital.LABOR to 7, Capital.MAGIC to 6),
    earnings = setOf(),
    bonus = 0,
    time = 28,
    size = 4..8,
    upgradesFrom = Bath::class,
    benefit = "temporary age penalty reduction"
)