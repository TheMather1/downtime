package pathfinder.domain.character

import jakarta.persistence.*
import pathfinder.domain.character.stats.Save.*

@Embeddable
class Saves {
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "abilityScore", column = Column(name = "fort_score")),
        AttributeOverride(name = "base", column = Column(name = "fort_base")),
        AttributeOverride(name = "bonuses", column = Column(name = "fort_bonus"))
    )
    val fortitude = Fortitude()
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "abilityScore", column = Column(name = "ref_score")),
        AttributeOverride(name = "base", column = Column(name = "ref_base")),
        AttributeOverride(name = "bonuses", column = Column(name = "ref_bonus"))
    )
    val reflex = Reflex()
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "abilityScore", column = Column(name = "will_score")),
        AttributeOverride(name = "base", column = Column(name = "will_base")),
        AttributeOverride(name = "bonuses", column = Column(name = "will_bonus"))
    )
    val willpower = Willpower()
}
