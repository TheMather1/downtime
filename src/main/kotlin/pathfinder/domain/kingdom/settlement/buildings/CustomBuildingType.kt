package pathfinder.domain.kingdom.settlement.buildings

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class CustomBuildingType(
    override var name: String,
    override var corruptionBonus: Int = 0,
    override var crimeBonus: Int = 0,
    override var lawBonus: Int = 0,
    override var loreBonus: Int = 0,
    override var productivityBonus: Int = 0,
    override var societyBonus: Int = 0,
    override var economyBonus: Int = 0,
    override var loyaltyBonus: Int = 0,
    override var stabilityBonus: Int = 0,
    override var unrestBonus: Int = 0,
    override var consumptionBonus: Int = 0,
    override val baseValueBonus: Int = 0
) : BuildingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
    override val cost = 0

    override val displayName: String
        get() = name
}
