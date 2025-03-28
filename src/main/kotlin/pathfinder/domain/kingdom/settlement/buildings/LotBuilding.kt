package pathfinder.domain.kingdom.settlement.buildings

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity
class LotBuilding(
    @Enumerated(EnumType.STRING)
    override val type: LotBuildingType,
): Building<LotBuildingType>(type)