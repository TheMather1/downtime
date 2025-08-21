package pathfinder.domain.character.stats

import java.io.Serializable

class Bonus<T: BonusType>(
    val type: T,
    var value: Int
): Serializable {
    operator fun plus(other: Bonus<T>) = when {
        type.stacks -> Bonus(type, value + other.value)
        value < other.value -> other
        else -> this
    }
}
