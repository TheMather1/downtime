package pathfinder.domain.support.direction

import jakarta.persistence.Inheritance

@Inheritance
sealed interface Direction<T: Direction<T>> {
    val orientation: Orientation

    val opposite: Direction<T>

    val displayName: String

    fun towards(cardinal: Cardinal): Boolean
}
