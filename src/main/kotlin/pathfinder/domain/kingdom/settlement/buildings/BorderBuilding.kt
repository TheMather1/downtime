package pathfinder.domain.kingdom.settlement.buildings

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity
@DiscriminatorValue("BorderBuilding")
class BorderBuilding(
    @Enumerated(EnumType.STRING)
    override val type: BorderBuildingType,
): Building<BorderBuildingType>(type)
