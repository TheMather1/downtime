package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.Room

class Bath : Room(
    "Bath",
    mapOf(Capital.GOODS to 3, Capital.INFLUENCE to 1, Capital.LABOR to 2),
    setOf(Capital.INFLUENCE),
    3,
    8,
    3..6,
    benefit = "Bonus on Fortitude saves against disease."
)