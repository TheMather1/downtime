package pathfinder.domain.kingdom.turns

import jakarta.persistence.*
import pathfinder.diceSyntax.d
import pathfinder.domain.kingdom.Kingdom
import pathfinder.domain.kingdom.leadership.AbsentLeaderException
import pathfinder.domain.kingdom.leadership.Government
import pathfinder.domain.kingdom.terrain.Hex
import pathfinder.domain.kingdom.terrain.HexAlreadyClaimedException
import pathfinder.domain.kingdom.terrain.features.TerrainFeature
import pathfinder.domain.kingdom.terrain.improvements.Improvement

//@Embeddable
class EdictPhase(kingdom: Kingdom): Phase() {
//    @Embedded
//    val newGovernment = kingdom.government.clone().apply {
//        roles.forEach {
//            it.performedDuty = false
//            it.forcedVacancy = false
//        }
//    }

//    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
//    val buildings = mutableSetOf<PlannedBuilding>()

    fun step1(kingdom: Kingdom, newGovernment: Government) {
        kingdom.assignLeadership(newGovernment)
    }

    fun Kingdom.assignLeadership(newGovernment: Government) {
        var unrestMod = 0
        if(government.ruler != newGovernment.ruler) {
            unrestMod += 2
            if(!loyaltyCheck(-4)) newGovernment.ruler.forcedVacancy = true
        }

        government.nonRulerRoles.forEach {
            newGovernment.getRole(it.roleName)?.let { role ->
                if(it.character != null  && it.character != role.character) {
                    if(it.character !in newGovernment.characters) {
                        unrestMod += 1
                        if(!loyaltyCheck()) role.forcedVacancy = true
                    } else {
                        if(!loyaltyCheck(4)) role.forcedVacancy = true
                    }
                }
            }
        }

        unrest += unrestMod
        government = newGovernment
    }

    fun step2(kingdom: Kingdom, claimHexes: Set<Hex>, abandonHexes: Set<Hex>) {
        kingdom.claimHexes(claimHexes)
        kingdom.abandonHexes(abandonHexes)
    }

    fun Kingdom.claimHexes(claimHexes: Set<Hex>) {
        if (government.ruler.performedDuty) {
            claimHexes.filter { it.owner != null }.let {
                if (it.isNotEmpty()) throw HexAlreadyClaimedException(*it.toTypedArray())
            }
            hexes += claimHexes
        }
    }

    fun Kingdom.abandonHexes(abandonHexes: Set<Hex>) {
        hexes -= abandonHexes
    }

    fun step3(kingdom: Kingdom, terrainImprovements: Set<PlannedImprovement>) {
        kingdom.buildImprovements(terrainImprovements)
    }

    fun Kingdom.buildImprovements(terrainImprovements: Set<PlannedImprovement>) {
        if (!government.ruler.performedDuty && terrainImprovements.any {
            it.improvement == Improvement.FARM || it.improvement == Improvement.ROAD || it.improvement == Improvement.HIGHWAY
        }) throw AbsentLeaderException("Cannot build farms or roads if the ruler is absent.", government.ruler)
        terrainImprovements.forEach {
            it.apply {
                val buildBridge = TerrainFeature.RIVER in hex.terrainFeatures
                        && TerrainFeature.BRIDGE !in hex.terrainFeatures
                        && (improvement == Improvement.ROAD || improvement == Improvement.HIGHWAY)
                val highwayUpgrade = improvement == Improvement.HIGHWAY && Improvement.ROAD in hex.improvements
                var cost = if (highwayUpgrade) Improvement.ROAD.cost(hex) else improvement.cost(hex)
                if (buildBridge) cost += hex.terrain.roadCost!!
                if(treasury > cost) {
                    treasury -= cost
                    hex.improvements += improvement
                    if (buildBridge) hex.terrainFeatures += TerrainFeature.BRIDGE
                    if (highwayUpgrade) hex.improvements -= Improvement.ROAD
                }
            }
        }
    }

//    fun step4(kingdom: Kingdom, buildings: Set<PlannedBuilding>) {
//        TODO("Create and improve settlements")
//    }

    fun Kingdom.loyaltyCheck(modifier: Int = 0) = (1 d 20 + loyalty - modifier).toInt() > controlDC

    @Entity
    class PlannedImprovement(
        @ManyToOne
        val hex: Hex,
        @Enumerated(EnumType.STRING)
        val improvement: Improvement,
    ) {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id = 0
    }

//    @Entity
//    class PlannedBuilding(
//        @ManyToOne
//        val district: District,
//        @Embedded
//        val buildingCoordinate: BuildingCoordinate,
//        @ManyToOne
//        val customBuilding: CustomBuilding? = null,
//        @Enumerated(EnumType.STRING)
//        val defaultBuilding: StandardBuilding? = null
//    )
}