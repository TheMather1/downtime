package pathfinder.domain.downtime.rooms

import pathfinder.domain.downtime.Capital
import pathfinder.domain.downtime.DowntimeEntity

abstract class Augmentation(
    name: String, create: Map<Capital, Int>, bonus: Int = 0, time: Int, val benefit: String? = null
) : DowntimeEntity(name, create, emptySet(), bonus, time)
