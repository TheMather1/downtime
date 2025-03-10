package pathfinder.domain.kingdom.settlement

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.OneToOne
import pathfinder.domain.support.direction.Cardinal

@Entity
class HorizontalBorder(facing: Cardinal): DistrictBorder(facing) {
    constructor(facing: Cardinal, north: District? = null, south: District? = null): this(facing) {
        this.north = north
        this.south = south
    }

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    var north: District? = null
        set(value) {
            if (facing == Cardinal.NORTH && type == BorderType.WATER) throw IllegalStateException("Water border cannot face a district.")
            field = value
        }
    @OneToOne
    var south: District? = null
        set(value) {
            if (facing == Cardinal.SOUTH && type == BorderType.WATER) throw IllegalStateException("Water border cannot face a district.")
            field = value
        }

    override val facingDistrict: Boolean
        get() = null != if (facing == Cardinal.NORTH) north else south
}