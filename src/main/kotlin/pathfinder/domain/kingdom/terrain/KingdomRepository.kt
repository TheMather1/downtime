package pathfinder.domain.kingdom.terrain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pathfinder.domain.kingdom.Kingdom

@Repository
interface KingdomRepository: JpaRepository<Kingdom, Long>
