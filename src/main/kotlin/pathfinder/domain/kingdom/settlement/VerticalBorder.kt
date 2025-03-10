package pathfinder.domain.kingdom.settlement

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.OneToOne
import pathfinder.domain.support.direction.Cardinal

@Entity
class VerticalBorder(facing: Cardinal): DistrictBorder(facing) {
    constructor(facing: Cardinal, west: District? = null, east: District? = null): this(facing) {
        this.west = west
        this.east = east
    }

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    var west: District? = null
        set(value) {
            if (facing == Cardinal.WEST && type == BorderType.WATER) throw IllegalStateException("Water border cannot face a district.")
            field = value
        }
    @OneToOne
    var east: District? = null
        set(value) {
            if (facing == Cardinal.EAST && type == BorderType.WATER) throw IllegalStateException("Water border cannot face a district.")
            field = value
        }

    override val facingDistrict: Boolean
        get() = null != if (facing == Cardinal.WEST) west else east
}