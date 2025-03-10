package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.Room

class BattleRing : Room(
    "Battle Ring",
    mapOf(Capital.GOODS to 18, Capital.INFLUENCE to 4, Capital.LABOR to 16),
    setOf(Capital.INFLUENCE),
    15,
    40,
    40..100,
    benefit = "Contestant gains a bonus on Intimidate and performance combat checks"
)