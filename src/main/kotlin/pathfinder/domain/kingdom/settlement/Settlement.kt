package pathfinder.domain.kingdom.settlement

import jakarta.persistence.*
import pathfinder.domain.kingdom.terrain.Hex
import pathfinder.domain.support.coordinate.Coordinate
import pathfinder.domain.support.coordinate.CoordinateConverter
import pathfinder.domain.support.direction.Cardinal
import pathfinder.web.frontend.dto.settlement.SettlementData

@Entity
class Settlement(
    @OneToOne
    val hex: Hex,
    var name: String
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
    @Enumerated(EnumType.STRING)
    var type: SettlementType = SettlementType.THORP
    @Enumerated(EnumType.STRING)
    var alignment: SettlementAlignment = SettlementAlignment.TRUE_NEUTRAL
    @Enumerated(EnumType.STRING)
    var government: SettlementGovernment = SettlementGovernment.AUTOCRACY
    @Embedded
    val misc = MiscBonuses()
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "settlement")
    @MapKey(name = "coordinate")
    @Convert(converter = CoordinateConverter::class)
    val districts: MutableMap<Coordinate, District> = mutableMapOf(Coordinate(0,0) to District(Coordinate(0,0), this))

    val size
        get() = districts.size

    val corruption
        get() = type.modifiers + alignment.corruptionBonus + government.corruptionBonus + buildingCorruption + misc.corruption

    val crime
        get() = type.modifiers + alignment.crimeBonus + government.crimeBonus + buildingCrime + misc.crime

    val law
        get() = type.modifiers + alignment.lawBonus + government.lawBonus + buildingLaw + misc.law

    val lore
        get() = type.modifiers + alignment.loreBonus + government.loreBonus + buildingLore + misc.lore

    val productivity
        get() = type.modifiers + government.productivityBonus + buildingProductivity + misc.productivity

    val society
        get() = type.modifiers + alignment.societyBonus + government.societyBonus + buildingSociety + misc.society

    val spellcasting
        get() = type.spellcasting + government.spellcastingBonus

    val consumption
        get() = size + buildings.sumOf { it.consumptionBonus } + misc.consumption

    val economy
        get() = buildings.sumOf { it.economyBonus } + misc.economy

    val loyalty
        get() = buildings.sumOf { it.loyaltyBonus } + misc.loyalty

    val stability
        get() = buildings.sumOf { it.stabilityBonus } + misc.stability

    val buildings
        get() = districts.values.flatMap { it.buildings }.toSet()

    private val buildingCorruption
        get() = buildings.sumOf { it.corruptionBonus }

    private val buildingCrime
        get() = buildings.sumOf { it.crimeBonus }

    private val buildingLaw
        get() = buildings.sumOf { it.lawBonus }

    private val buildingLore
        get() = buildings.sumOf { it.loreBonus }

    private val buildingProductivity
        get() = buildings.sumOf { it.productivityBonus }

    private val buildingSociety
        get() = buildings.sumOf { it.societyBonus }

    fun addDistrict(coordinate: Coordinate) {
        if(districts.containsKey(coordinate)) throw IllegalArgumentException("District $coordinate is already registered.")
        val north = districts[coordinate.north]
        val east = districts[coordinate.east]
        val south = districts[coordinate.south]
        val west = districts[coordinate.west]
        districts[coordinate] = District(
            settlement = this,
            coordinate = coordinate,
            northBorder = north?.southBorder,
            eastBorder = east?.westBorder,
            southBorder = south?.northBorder,
            westBorder = west?.eastBorder
        )
    }

    fun removeDistrict(coordinate: Coordinate) {
        val district = districts.remove(coordinate) ?: throw IllegalArgumentException("District $coordinate is not registered.")
        district.northBorder.apply {
            if(facing == Cardinal.NORTH) flip()
            south == null
        }
        district.eastBorder.apply {
            if(facing == Cardinal.EAST) flip()
            west == null
        }
        district.southBorder.apply {
            if(facing == Cardinal.SOUTH) flip()
            north == null
        }
        district.westBorder.apply {
            if(facing == Cardinal.WEST) flip()
            east == null
        }
    }

    val settlementData
        get() = SettlementData(id, name)
}
