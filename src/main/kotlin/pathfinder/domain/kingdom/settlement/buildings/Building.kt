package pathfinder.domain.kingdom.settlement.buildings

import jakarta.persistence.*

@Suppress("EqualsOrHashCode")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class Building<T: BuildingType>(
    @Transient
    open val type: T,
    @ManyToOne
    open val customization: CustomBuildingType? = null
): BuildingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0

    override fun equals(other: Any?): Boolean {
        return (other as? Building<*>)?.id == id || other == type
    }
    override val name: String
        get() = customization?.name ?: type.name
    override val cost: Int
        get() = customization?.cost ?: type.cost
    override val economyBonus: Int
        get() = customization?.economyBonus ?: type.economyBonus
    override val loyaltyBonus: Int
        get() = customization?.loyaltyBonus ?: type.loyaltyBonus
    override val stabilityBonus: Int
        get() = customization?.stabilityBonus ?: type.stabilityBonus
    override val unrestBonus: Int
        get() = customization?.unrestBonus ?: type.unrestBonus
    override val consumptionBonus: Int
        get() = customization?.consumptionBonus ?: type.consumptionBonus
    override val corruptionBonus: Int
        get() = customization?.corruptionBonus ?: type.corruptionBonus
    override val crimeBonus: Int
        get() = customization?.crimeBonus ?: type.crimeBonus
    override val lawBonus: Int
        get() = customization?.lawBonus ?: type.lawBonus
    override val loreBonus: Int
        get() = customization?.loreBonus ?: type.loreBonus
    override val productivityBonus: Int
        get() = customization?.productivityBonus ?: type.productivityBonus
    override val societyBonus: Int
        get() = customization?.societyBonus ?: type.societyBonus
    override val baseValueBonus: Int
        get() = customization?.baseValueBonus ?: type.baseValueBonus

    override val displayName: String
        get() = customization?.displayName ?: type.displayName
}
