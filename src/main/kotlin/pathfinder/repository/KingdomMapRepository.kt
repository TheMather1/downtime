package pathfinder.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pathfinder.domain.kingdom.terrain.KingdomMap

@Repository
interface KingdomMapRepository: JpaRepository<KingdomMap, Long>
