package pathfinder.domain.kingdom

import jakarta.persistence.*
import pathfinder.domain.Campaign
import pathfinder.domain.kingdom.leadership.Government
import pathfinder.domain.kingdom.settlement.Settlement
import pathfinder.domain.kingdom.settlement.buildings.LotBuildingType
import pathfinder.domain.kingdom.settlement.buildings.LotBuildingType.FOUNDRY
import pathfinder.domain.kingdom.settlement.buildings.LotBuildingType.STOCKYARD
import pathfinder.domain.kingdom.terrain.Hex
import pathfinder.domain.kingdom.terrain.improvements.Improvement.*
import pathfinder.domain.support.jpa.ColorConverter
import java.awt.Color

@Entity
class Kingdom(
    @ManyToOne
    val campaign: Campaign,
    var name: String,
    @Convert(converter = ColorConverter::class)
    var color: Color
): Comparable<Kingdom> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
    @Enumerated(EnumType.STRING)
    var alignment: KingdomAlignment = KingdomAlignment.TRUE_NEUTRAL
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "kingdom")
    var government: Government = Government(this)
    @OneToMany(mappedBy = "owner")
    val hexes: MutableSet<Hex> = mutableSetOf()
    @get:Transient
    val settlements
        get() = hexes.mapNotNull { it.settlement }.toSet()
    @Enumerated(EnumType.STRING)
    var holidayEdict: HolidayEdict = HolidayEdict.NONE
    @Enumerated(EnumType.STRING)
    var promotionEdict: PromotionEdict = PromotionEdict.NONE
    @Enumerated(EnumType.STRING)
    var taxationEdict: TaxationEdict = TaxationEdict.NONE
        get() = if (government.treasurer.performedDuty) field else TaxationEdict.NONE
    var treasury: Int = 0
    var unrest: Int = 0
    @Column(columnDefinition = "BOOLEAN")
    var anarchy: Boolean = false

    val size
        get() = hexes.size

    val earningsBonus
        get() = hexEarnings

    var granary: Int = 0
        set(value) {
            field = value.coerceAtMost(settlements.sumOf { it.granaryCapacity })
        }

    val consumption
        get() = settlements.sumOf(Settlement::consumption) +
                hexConsumption +
                holidayEdict.consumptionPenalty +
                promotionEdict.consumptionPenalty.let {
                    if (settlements.any { it.buildings.any { it.type == LotBuildingType.CATHEDRAL } }) it / 2 else it
                }

    val economy
        get() = government.economyBonus +
                settlements.sumOf(Settlement::economy) +
                hexEconomy +
                (roadCount/4) + (highwayCount/4) +
                alignment.economyBonus +
                taxationEdict.economyBonus

    var loyaltyBase = 0
    val loyalty
        get() = loyaltyBase +
                government.loyaltyBonus +
                settlements.sumOf(Settlement::loyalty) +
                hexes.sumOf(Hex::loyaltyBonus) +
                holidayEdict.loyaltyBonus +
                alignment.loyaltyBonus +
                taxationEdict.loyaltyPenalty.let {
                    if (settlements.any { it.buildings.any { it.type == LotBuildingType.WATERFRONT } }) it/2 else it
                }

    val stability
        get() = government.stabilityBonus +
                settlements.sumOf(Settlement::stability) +
                hexes.sumOf(Hex::stabilityBonus) +
                (roadCount/8) + (highwayCount/8) +
                alignment.stabilityBonus +
                promotionEdict.stabilityBonus

    private val roadCount
        get() = hexes.count { ROAD in it.improvements }

    private val highwayCount
        get() = hexes.count { ROAD in it.improvements }

    private val hexEarnings
        get () = hexes.sumOf(Hex::earningsBonus) +
                networks.sumOf {
                    minOf(it.buildings.count { it.type == FOUNDRY }, it.improvements.count { it == MINE })
                }

    private val hexEconomy
        get () = hexes.sumOf(Hex::economyBonus) +
                networks.sumOf {
                    minOf(it.buildings.count { it.type == FOUNDRY }, it.improvements.count { it == MINE })
                }

    private val hexConsumption
        get () = hexes.sumOf(Hex::consumptionBonus) -
                hexes.count {
                    FARM in it.improvements && (it.neighbors + it).any {
                        it.settlement?.buildings?.any { it.type == STOCKYARD } == true
                    }
                }

    private val networks: List<Set<Hex>>
        get() = settlements.fold(mutableListOf<Set<Hex>>()) { networkList, settlement ->
            if (networkList.none { settlement.hex in it}) {
                val network = mutableSetOf(settlement.hex)
                if (settlement.hex.easyTransport) {
                    var candidates = settlement.hex.neighbors { it.easyTransport }
                    while (candidates.isNotEmpty()) {
                        network.addAll(candidates)
                        candidates = candidates.flatMap { candidate -> candidate.neighbors { it.easyTransport && it !in network } }.toSet()
                    }
                }
                networkList.add(network)
            }
            networkList
        }

    private val Set<Hex>.buildings
        get() = mapNotNull(Hex::settlement).flatMap(Settlement::buildings)

    private val Set<Hex>.improvements
        get() = flatMap(Hex::improvements)

    val controlDC
        get() = 20 + size + settlements.sumOf { it.districts.size }

    val hexColor
        get() = "#" + Integer.toHexString(color.rgb and 0xffffff)


    override fun compareTo(other: Kingdom): Int {
        return name.compareTo(other.name).takeUnless { it == 0 } ?: id.compareTo(other.id)
    }
}
