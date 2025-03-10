package pathfinder.domain.kingdom.turns

import jakarta.persistence.*

@Embeddable
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class Phase() {
    var completed: Boolean = false
}