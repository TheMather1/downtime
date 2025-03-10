package pathfinder.domain.character.stats

import java.io.Serializable

//@Embeddable
class Bonus(
    val type: BonusType,
    var value: Int
): Serializable