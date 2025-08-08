package pathfinder.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pathfinder.domain.kingdom.settlement.DistrictBorder

@Repository
interface DistrictBorderRepository: JpaRepository<DistrictBorder, Long>
