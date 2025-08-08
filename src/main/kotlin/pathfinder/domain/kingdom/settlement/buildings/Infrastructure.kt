package pathfinder.domain.kingdom.settlement.buildings

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity
@DiscriminatorValue("Infrastructure")
class Infrastructure(
    @Enumerated(EnumType.STRING)
    override val type: InfrastructureType,
): Building<InfrastructureType>(type)
