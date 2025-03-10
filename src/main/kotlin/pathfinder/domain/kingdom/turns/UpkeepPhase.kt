package pathfinder.domain.kingdom.turns

import pathfinder.diceSyntax.d
import pathfinder.domain.kingdom.Kingdom

//@Embeddable
class UpkeepPhase: Phase() {

    fun step1(kingdom: Kingdom) {
        kingdom.stabilityCheck()
    }

    fun Kingdom.stabilityCheck() {
        if (!anarchy && government.warden.performedDuty) {
            val roll = (1 d 20 + stability - unrest).toInt()
            when {
                roll < controlDC - 5 -> unrest += (1 d 4).toInt()
                roll < controlDC -> unrest += 1
                unrest < 1 -> treasury += 1
                else -> unrest -= 1
            }
        }
    }
    fun step2(kingdom: Kingdom) {
        kingdom.payConsumption()
    }

    fun Kingdom.payConsumption() {
        treasury -= consumption.coerceAtLeast(0)
    }

    fun step3(kingdom: Kingdom) {
        kingdom.restockMagicItems()
    }

    fun Kingdom.restockMagicItems() {
    }

    fun step4(kingdom: Kingdom, reduceUnrest: Boolean) {
        kingdom.modifyUnrest()
        if(reduceUnrest) kingdom.reduceUnrest()
    }

    fun Kingdom.modifyUnrest() {
        if (!government.ruler.performedDuty) unrest += 4
        unrest -= listOf(
            economy < 0,
            loyalty < 0,
            stability < 0
        ).count()
    }

    fun Kingdom.reduceUnrest() {
        if (!anarchy && government.royalEnforcer.performedDuty) {
            unrest -= 1
            if ((1 d 20 + loyalty).toInt() < controlDC) loyaltyBase -= 1
        }
    }
}