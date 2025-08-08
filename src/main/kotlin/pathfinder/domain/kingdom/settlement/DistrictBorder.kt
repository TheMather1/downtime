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
    var cityWall
        get() = buildings.any { it.type == BorderBuildingType.CITY_WALL }
        set(value) {
            if(value && !cityWall) buildings.add(BorderBuilding(BorderBuildingType.CITY_WALL))
            else if(!value && cityWall) buildings.removeIf { it.type == BorderBuildingType.CITY_WALL }
        }
    var moat
        get() = buildings.any { it.type == BorderBuildingType.MOAT }
        set(value) {
            if(value && !moat) buildings.add(BorderBuilding(BorderBuildingType.MOAT))
            else if(!value && moat) buildings.removeIf { it.type == BorderBuildingType.MOAT }
        }
    var streetlamps
        get() = buildings.any { it.type == BorderBuildingType.MAGICAL_STREETLAMPS }
        set(value) {
            if(value && !streetlamps) buildings.add(BorderBuilding(BorderBuildingType.MAGICAL_STREETLAMPS))
            else if(!value && streetlamps) buildings.removeIf { it.type == BorderBuildingType.MAGICAL_STREETLAMPS }
        }
    var watergate
        get() = buildings.any { it.type == BorderBuildingType.WATERGATE }
        set(value) {
            if(value && !watergate) buildings.add(BorderBuilding(BorderBuildingType.WATERGATE))
            else if(!value && watergate) buildings.removeIf { it.type == BorderBuildingType.WATERGATE }
        }

    val hasWater
        get() = type == BorderType.WATER || buildings.any { it.type == BorderBuildingType.MOAT }

    fun flip() {
        facing = facing.opposite as Cardinal
    }

    abstract val facingDistrict: Boolean

    enum class BorderType {
        LAND, WATER;
    }
}
