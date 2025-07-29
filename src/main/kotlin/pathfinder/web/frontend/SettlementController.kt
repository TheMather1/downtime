package pathfinder.web.frontend

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import pathfinder.domain.CampaignRepository
import pathfinder.domain.kingdom.settlement.Settlement
import pathfinder.web.frontend.dto.settlement.SettlementMapConfig

@Controller
@RequestMapping("/settlement/{settlement}")
class SettlementController(override val campaignRepository: CampaignRepository): FrontendController {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ModelAttribute
    fun addSettlement(
        @PathVariable settlement: Settlement
    ) = settlement

    @ModelAttribute
    fun addMap(
        @PathVariable settlement: Settlement
    ) = settlement.hex.map

    @ModelAttribute
    fun addCampaign(
        @PathVariable settlement: Settlement
    ) = settlement.hex.map.campaign

    @GetMapping
    fun indexView() = "settlements/settlement"

    @GetMapping("/map")
    fun mapView(
        settlement: Settlement,
        model: Model
    ): String {
        model.addAttribute("settlementMap", SettlementMapConfig(districts = settlement.districts))
        return "settlements/map"
    }
}
