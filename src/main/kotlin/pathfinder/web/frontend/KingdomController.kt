package pathfinder.web.frontend

import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import pathfinder.repository.CampaignRepository
import pathfinder.domain.character.PathfinderCharacter
import pathfinder.domain.kingdom.Kingdom
import pathfinder.domain.kingdom.KingdomScore

@Controller
@RequestMapping("/kingdom/{kingdom}")
class KingdomController(override val campaignRepository: CampaignRepository): FrontendController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @ModelAttribute
    fun addKingdom(
        @PathVariable kingdom: Kingdom
    ) = kingdom

    @ModelAttribute
    fun addCampaign(
        @PathVariable kingdom: Kingdom
    ) = kingdom.campaign

    @GetMapping
    fun mapView() = "kingdoms/kingdom"


    @GetMapping("/government")
    fun getGovernment(
        @PathVariable("kingdom") kingdom: Kingdom,
        model: Model
    ): String {
        model.addAttribute("government", kingdom.government)
        return "kingdoms/templates/governmentForm"
    }

    @PatchMapping("/government")
    fun modifyGovernment(
        @PathVariable("kingdom") kingdom: Kingdom,
        @RequestParam("ruler", required = false) ruler: PathfinderCharacter?,
        @RequestParam("rulerFirstDuty", required = false) rulerFirstDuty: KingdomScore?,
        @RequestParam("rulerSecondDuty", required = false) rulerSecondDuty: KingdomScore?,
        @RequestParam("rulerThirdDuty", required = false) rulerThirdDuty: KingdomScore?,
        @RequestParam("rulerPerformedDuty", required = false, defaultValue = "false") rulerPerformedDuty: Boolean,
        @RequestParam("consort", required = false) consort: PathfinderCharacter?,
        @RequestParam("coRuler", required = false, defaultValue = "false") coRuler: Boolean,
        @RequestParam("consortPerformedDuty", required = false, defaultValue = "false") consortPerformedDuty: Boolean,
        @RequestParam("heir", required = false) heir: PathfinderCharacter?,
        @RequestParam("heirPerformedDuty", required = false, defaultValue = "false") heirPerformedDuty: Boolean,
        @RequestParam("councillor", required = false) councillor: PathfinderCharacter?,
        @RequestParam("councillorPerformedDuty", required = false, defaultValue = "false") councillorPerformedDuty: Boolean,
        @RequestParam("general", required = false) general: PathfinderCharacter?,
        @RequestParam("generalPerformedDuty", required = false, defaultValue = "false") generalPerformedDuty: Boolean,
        @RequestParam("grandDiplomat", required = false) grandDiplomat: PathfinderCharacter?,
        @RequestParam("grandDiplomatPerformedDuty", required = false, defaultValue = "false") grandDiplomatPerformedDuty: Boolean,
        @RequestParam("highPriest", required = false) highPriest: PathfinderCharacter?,
        @RequestParam("highPriestPerformedDuty", required = false, defaultValue = "false") highPriestPerformedDuty: Boolean,
        @RequestParam("magister", required = false) magister: PathfinderCharacter?,
        @RequestParam("magisterPerformedDuty", required = false, defaultValue = "false") magisterPerformedDuty: Boolean,
        @RequestParam("marshal", required = false) marshal: PathfinderCharacter?,
        @RequestParam("marshalPerformedDuty", required = false, defaultValue = "false") marshalPerformedDuty: Boolean,
        @RequestParam("royalEnforcer", required = false) royalEnforcer: PathfinderCharacter?,
        @RequestParam("royalEnforcerPerformedDuty", required = false, defaultValue = "false") royalEnforcerPerformedDuty: Boolean,
        @RequestParam("spymaster", required = false) spymaster: PathfinderCharacter?,
        @RequestParam("spymasterDuty", required = false) spymasterDuty: KingdomScore?,
        @RequestParam("spymasterPerformedDuty", required = false, defaultValue = "false") spymasterPerformedDuty: Boolean,
        @RequestParam("treasurer", required = false) treasurer: PathfinderCharacter?,
        @RequestParam("treasurerPerformedDuty", required = false, defaultValue = "false") treasurerPerformedDuty: Boolean,
        @RequestParam("warden", required = false) warden: PathfinderCharacter?,
        @RequestParam("wardenPerformedDuty", required = false, defaultValue = "false") wardenPerformedDuty: Boolean,
        model: Model
    ): String {
        model.addAttribute("government", kingdom.government.also {
            it.ruler.character = ruler
            it.ruler.performedDuty = rulerPerformedDuty
            it.rulerDuties.firstScore = rulerFirstDuty
            it.rulerDuties.secondScore = rulerSecondDuty
            it.rulerDuties.thirdScore = rulerThirdDuty
            it.consort.character = consort
            it.consort.coRuler = coRuler
            it.consort.performedDuty = consortPerformedDuty
            it.heir.character = heir
            it.heir.performedDuty = heirPerformedDuty
            it.councillor.character = councillor
            it.councillor.performedDuty = councillorPerformedDuty
            it.general.character = general
            it.general.performedDuty = generalPerformedDuty
            it.grandDiplomat.character = grandDiplomat
            it.grandDiplomat.performedDuty = grandDiplomatPerformedDuty
            it.highPriest.character = highPriest
            it.highPriest.performedDuty = highPriestPerformedDuty
            it.magister.character = magister
            it.magister.performedDuty = magisterPerformedDuty
            it.marshal.character = marshal
            it.marshal.performedDuty = marshalPerformedDuty
            it.royalEnforcer.character = royalEnforcer
            it.royalEnforcer.performedDuty = royalEnforcerPerformedDuty
            it.spymaster.character = spymaster
            it.spymaster.score = spymasterDuty
            it.spymaster.performedDuty = spymasterPerformedDuty
            it.treasurer.character = treasurer
            it.treasurer.performedDuty = treasurerPerformedDuty
            it.warden.character = warden
            it.warden.performedDuty = wardenPerformedDuty
        })
        return "kingdoms/templates/governmentForm"
    }

    @PutMapping("/government")
    @Transactional
    fun updateGovernment(
        @PathVariable("kingdom") kingdom: Kingdom,
        @RequestParam("ruler", required = false) ruler: PathfinderCharacter?,
        @RequestParam("rulerFirstDuty", required = false) rulerFirstDuty: KingdomScore?,
        @RequestParam("rulerSecondDuty", required = false) rulerSecondDuty: KingdomScore?,
        @RequestParam("rulerThirdDuty", required = false) rulerThirdDuty: KingdomScore?,
        @RequestParam("rulerPerformedDuty", required = false, defaultValue = "false") rulerPerformedDuty: Boolean,
        @RequestParam("consort", required = false) consort: PathfinderCharacter?,
        @RequestParam("coRuler", required = false, defaultValue = "false") coRuler: Boolean,
        @RequestParam("consortPerformedDuty", required = false, defaultValue = "false") consortPerformedDuty: Boolean,
        @RequestParam("heir", required = false) heir: PathfinderCharacter?,
        @RequestParam("heirPerformedDuty", required = false, defaultValue = "false") heirPerformedDuty: Boolean,
        @RequestParam("councillor", required = false) councillor: PathfinderCharacter?,
        @RequestParam("councillorPerformedDuty", required = false, defaultValue = "false") councillorPerformedDuty: Boolean,
        @RequestParam("general", required = false) general: PathfinderCharacter?,
        @RequestParam("generalPerformedDuty", required = false, defaultValue = "false") generalPerformedDuty: Boolean,
        @RequestParam("grandDiplomat", required = false) grandDiplomat: PathfinderCharacter?,
        @RequestParam("grandDiplomatPerformedDuty", required = false, defaultValue = "false") grandDiplomatPerformedDuty: Boolean,
        @RequestParam("highPriest", required = false) highPriest: PathfinderCharacter?,
        @RequestParam("highPriestPerformedDuty", required = false, defaultValue = "false") highPriestPerformedDuty: Boolean,
        @RequestParam("magister", required = false) magister: PathfinderCharacter?,
        @RequestParam("magisterPerformedDuty", required = false, defaultValue = "false") magisterPerformedDuty: Boolean,
        @RequestParam("marshal", required = false) marshal: PathfinderCharacter?,
        @RequestParam("marshalPerformedDuty", required = false, defaultValue = "false") marshalPerformedDuty: Boolean,
        @RequestParam("royalEnforcer", required = false) royalEnforcer: PathfinderCharacter?,
        @RequestParam("royalEnforcerPerformedDuty", required = false, defaultValue = "false") royalEnforcerPerformedDuty: Boolean,
        @RequestParam("spymaster", required = false) spymaster: PathfinderCharacter?,
        @RequestParam("spymasterDuty", required = false) spymasterDuty: KingdomScore?,
        @RequestParam("spymasterPerformedDuty", required = false, defaultValue = "false") spymasterPerformedDuty: Boolean,
        @RequestParam("treasurer", required = false) treasurer: PathfinderCharacter?,
        @RequestParam("treasurerPerformedDuty", required = false, defaultValue = "false") treasurerPerformedDuty: Boolean,
        @RequestParam("warden", required = false) warden: PathfinderCharacter?,
        @RequestParam("wardenPerformedDuty", required = false, defaultValue = "false") wardenPerformedDuty: Boolean
    ): String {
        kingdom.government.also {
            it.ruler.character = ruler
            it.ruler.performedDuty = rulerPerformedDuty
            it.rulerDuties.firstScore = rulerFirstDuty
            it.rulerDuties.secondScore = rulerSecondDuty
            it.rulerDuties.thirdScore = rulerThirdDuty
            it.consort.character = consort
            it.consort.coRuler = coRuler
            it.consort.performedDuty = consortPerformedDuty
            it.heir.character = heir
            it.heir.performedDuty = heirPerformedDuty
            it.councillor.character = councillor
            it.councillor.performedDuty = councillorPerformedDuty
            it.general.character = general
            it.general.performedDuty = generalPerformedDuty
            it.grandDiplomat.character = grandDiplomat
            it.grandDiplomat.performedDuty = grandDiplomatPerformedDuty
            it.highPriest.character = highPriest
            it.highPriest.performedDuty = highPriestPerformedDuty
            it.magister.character = magister
            it.magister.performedDuty = magisterPerformedDuty
            it.marshal.character = marshal
            it.marshal.performedDuty = marshalPerformedDuty
            it.royalEnforcer.character = royalEnforcer
            it.royalEnforcer.performedDuty = royalEnforcerPerformedDuty
            it.spymaster.character = spymaster
            it.spymaster.score = spymasterDuty
            it.spymaster.performedDuty = spymasterPerformedDuty
            it.treasurer.character = treasurer
            it.treasurer.performedDuty = treasurerPerformedDuty
            it.warden.character = warden
            it.warden.performedDuty = wardenPerformedDuty
        }
        return "kingdoms/kingdom"
    }
}
