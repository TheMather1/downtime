package pathfinder.domain.kingdom.settlement

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.OneToMany
import pathfinder.domain.kingdom.settlement.buildings.BorderBuilding
import pathfinder.domain.kingdom.settlement.buildings.BorderBuildingType
import pathfinder.domain.support.direction.Cardinal

@Entity
@Inheritance
abstract class DistrictBorder(
    @Enumerated(EnumType.STRING)
    open var facing: Cardinal,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0
    open var type: BorderType = BorderType.LAND
        set(value) {
            if(value == BorderType.WATER && facingDistrict) throw IllegalStateException("Water border cannot face a district.")
            field = value
        }

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    private val buildings = mutableSetOf<BorderBuilding>()

    open fun getBuildings() = buildings.toSet()
    fun addBuilding(building: BorderBuildingType) = if (buildings.any { it.type == building }) throw IllegalArgumentException("Duplicate building type.")
        else buildings.add(BorderBuilding(building))
    fun removeBuilding(building: BorderBuildingType) = buildings.removeIf { it.type == building }

    fun flip() {
        facing = facing.opposite
    }

    abstract val facingDistrict: Boolean

    enum class BorderType {
        LAND, WATER;
    }
}