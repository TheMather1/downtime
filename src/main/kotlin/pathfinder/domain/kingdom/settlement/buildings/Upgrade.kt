package pathfinder.domain.kingdom.settlement.buildings

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity
@DiscriminatorValue("Upgrade")
class Upgrade(
    @Enumerated(EnumType.STRING)
    override val type: UpgradeType,
): Building<UpgradeType>(type)
