package pathfinder.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pathfinder.domain.character.PathfinderCharacter

@Repository
interface CharacterRepository: JpaRepository<PathfinderCharacter, Long>
