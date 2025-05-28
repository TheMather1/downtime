package pathfinder.web.frontend

import net.dv8tion.jda.api.JDA
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import pathfinder.domain.CampaignRepository
import pathfinder.domain.kingdom.terrain.KingdomMap

@Controller
@RequestMapping("/map/{map}")
class MapController(jda: JDA, campaignRepository: CampaignRepository): FrontendController(jda, campaignRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @ModelAttribute
    fun addMap(
        @PathVariable map: KingdomMap
    ) = map

    @ModelAttribute
    fun addCampaign(
        @PathVariable map: KingdomMap
    ) = map.campaign

    @GetMapping
    fun mapView() = "maps/map"
}
