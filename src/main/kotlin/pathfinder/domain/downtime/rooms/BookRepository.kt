package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.Room

class BookRepository : Room(
    name = "Book Repository",
    create = mapOf(Capital.GOODS to 8, Capital.INFLUENCE to 2, Capital.LABOR to 7, Capital.MAGIC to 1),
    earnings = setOf(Capital.INFLUENCE),
    bonus = 8,
    time = 16,
    size = 4..12,
    benefit = "bonus on Knowledge checks of one type"
)