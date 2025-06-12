package pathfinder.web.frontend

import net.dv8tion.jda.api.JDA
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import pathfinder.domain.Campaign
import pathfinder.domain.CampaignRepository

@Controller
@RequestMapping("/campaign/{campaign}")
class CampaignController(jda: JDA, campaignRepository: CampaignRepository): FrontendController(jda, campaignRepository) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ModelAttribute
    fun addCampaign(@PathVariable campaign: Campaign) = campaign

    @GetMapping
    fun campaignPortal() = "campaign"

    @GetMapping("/maps")
    fun maps() = "maps/index"

    @GetMapping("/settlements")
    fun settlements() = "settlements/settlement"
}
