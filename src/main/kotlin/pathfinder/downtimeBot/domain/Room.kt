package pathfinder.downtimeBot.domain

import pathfinder.downtimeBot.domain.rooms.Augmentation

abstract class Room(
    name: String,
    create: Map<Capital, Int>,
    earnings: Set<Capital>,
    bonus: Int,
    time: Int,
    val size: IntRange,
    private val producesExisting: Boolean = false,
    val upgradesFrom: Class<Room>? = null,
    val benefit: String? = null
) : DowntimeEntity(
    name, create, earnings, bonus, time
) {
    val augmentations = emptySet<Augmentation>()

    override val bonus = super.bonus + augmentations.sumOf { it.bonus }

    var earningDistribution = mutableMapOf<Capital, Int>()
        set(value) {
            assert(producesExisting || earnings.containsAll(value.keys))
            assert(value.values.sum() <= bonus)
            field = value
        }

    fun bonusFor(building: Building, capital: Capital?) = when {
        capital == null -> bonus - earningDistribution.values.sum()
        capital in earnings || (producesExisting && building produces capital) -> earningDistribution[capital]
        else -> null
    }
}
