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
    var bridge
        get() = upgrades.any { it.type == UpgradeType.BRIDGE }
        set(value) {
            if (value && !bridge) upgrades.add(Upgrade(UpgradeType.BRIDGE))
            else if (!value) upgrades.removeIf { it.type == UpgradeType.BRIDGE }
        }
    var cistern
        get() = upgrades.any { it.type == UpgradeType.CISTERN }
        set(value) {
            if (value && cisternEligible && !cistern) upgrades.add(Upgrade(UpgradeType.CISTERN))
            else if (!value) upgrades.removeIf { it.type == UpgradeType.CISTERN }
        }
    var everflowingSpring
        get() = upgrades.any { it.type == UpgradeType.EVERFLOWING_SPRING }
        set(value) {
            if (value && everflowingSpringEligible && !everflowingSpring) upgrades.add(Upgrade(UpgradeType.EVERFLOWING_SPRING))
            else if (!value) upgrades.removeIf { it.type == UpgradeType.EVERFLOWING_SPRING }
        }
    var streetlamps
        get() = upgrades.any { it.type == UpgradeType.MAGICAL_STREETLAMPS }
        set(value) {
            if (value && !streetlamps) upgrades.add(Upgrade(UpgradeType.MAGICAL_STREETLAMPS))
            else if (!value) upgrades.removeIf { it.type == UpgradeType.MAGICAL_STREETLAMPS }
        }
    val bridgeEligible
        get() = UpgradeType.BRIDGE.eligible(this, neighbors)
    val cisternEligible
        get() = UpgradeType.CISTERN.eligible(this, neighbors)
    val everflowingSpringEligible
        get() = UpgradeType.EVERFLOWING_SPRING.eligible(this, neighbors)

    val north: Lot?
    get() = if (coordinate.y == 0) district.northBorder.north?.getBuildingMap()?.get(coordinate.plusY(5)) else district.getBuildingMap()[coordinate.north]
    val south: Lot?
    get() = if (coordinate.y == 5) district.southBorder.south?.getBuildingMap()?.get(coordinate.minusY(5)) else district.getBuildingMap()[coordinate.south]
    val east: Lot?
    get() = if (coordinate.x == 5) district.eastBorder.east?.getBuildingMap()?.get(coordinate.minusX(5)) else district.getBuildingMap()[coordinate.east]
    val west: Lot?
    get() = if (coordinate.x == 0) district.westBorder.west?.getBuildingMap()?.get(coordinate.plusX(5)) else district.getBuildingMap()[coordinate.west]

    val neighbors: Set<Lot>
        get() = setOfNotNull(north, south, east, west)
}
