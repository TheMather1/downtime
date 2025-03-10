package pathfinder.domain.kingdom.settlement.buildings

import jakarta.persistence.*
import pathfinder.domain.kingdom.settlement.District
import pathfinder.domain.support.coordinate.Coordinate
import pathfinder.domain.support.coordinate.CoordinateConverter

@Entity
class Lot(
    @Convert(converter = CoordinateConverter::class)
    val coordinate: Coordinate,
    @ManyToOne
    val district: District
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
    @ManyToOne(cascade = [(CascadeType.ALL)])
    var building: LotBuilding? = null

    @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true)
    val upgrades: MutableSet<Upgrade> = mutableSetOf()
}