package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.Room

class ArtisansWorkshop : Room(
    "Artisan’s Workshop",
    mapOf(Capital.GOODS to 9, Capital.LABOR to 9),
    setOf(Capital.GOODS, Capital.INFLUENCE),
    10,
    20,
    8..16,
    benefit = "Counts as masterwork artisan’s tools for one Craft skill."
)