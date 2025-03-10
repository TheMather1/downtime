package pathfinder.domain.kingdom.settlement.buildings

import jakarta.persistence.*

@Suppress("EqualsOrHashCode")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class Building<T: BuildingType>(
//    @Enumerated(EnumType.STRING)
    open val type: T,
    @Embedded
    open val customization: CustomBuildingType? = null
): BuildingType by customization ?: type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0

    override fun equals(other: Any?): Boolean {
        return (other as? Building<*>)?.id == id || other == type
    }
}