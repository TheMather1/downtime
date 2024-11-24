package pathfinder.downtimeBot.domain.rooms

import pathfinder.downtimeBot.domain.Capital
import pathfinder.downtimeBot.domain.DowntimeEntity

abstract class Augmentation(
    name: String, create: Map<Capital, Int>, bonus: Int = 0, time: Int, val benefit: String? = null
) : DowntimeEntity(name, create, emptySet(), bonus, time)
