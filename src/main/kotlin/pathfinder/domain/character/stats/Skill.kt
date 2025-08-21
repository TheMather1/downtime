package pathfinder.domain.character.stats

import jakarta.persistence.MappedSuperclass

@MappedSuperclass
sealed class Skill: Stat() {
    override var base = 0
}
