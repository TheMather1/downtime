package pathfinder.domain.kingdom.terrain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pathfinder.domain.kingdom.settlement.Settlement

@Repository
interface SettlementRepository: JpaRepository<Settlement, Long>
