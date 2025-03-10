package pathfinder.web.frontend

import net.dv8tion.jda.api.JDA
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import pathfinder.domain.CampaignRepository
import pathfinder.domain.kingdom.settlement.Settlement

@Controller
@RequestMapping("/settlement/{settlement}")
class SettlementController(jda: JDA, campaignRepository: CampaignRepository): FrontendController(jda, campaignRepository) {

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
    fun mapView() = "settlements/settlement"
}
