package pathfinder.web.backend

import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import pathfinder.domain.Campaign
import pathfinder.domain.kingdom.Kingdom
import pathfinder.domain.kingdom.terrain.KingdomMap
import pathfinder.repository.CampaignRepository
import pathfinder.repository.KingdomMapRepository
import pathfinder.repository.KingdomRepository
import pathfinder.service.generation.KingdomMapGenerator
import pathfinder.web.security.DiscordUser
import java.awt.Color

@Controller
@RequestMapping("/api/campaign")
class CampaignBackendController(
    private val campaignRepository: CampaignRepository,
    private val kingdomRepository: KingdomRepository,
    private val kingdomMapRepository: KingdomMapRepository,
    private val kingdomMapGenerator: KingdomMapGenerator
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping
    @Transactional
    fun createCampaign(
        @RequestParam("name") name: String,
        @AuthenticationPrincipal user: DiscordUser
    ): String {
        logger.debug("Received campaign name $name")
        val campaign = campaignRepository.saveAndFlush(Campaign(name, user)) //Required to populate id
        return "redirect:/campaign/" + campaign.id
    }

    @PostMapping("/{campaignId}/kingdom")
    @Transactional
    fun createKingdom(
        @PathVariable("campaignId") campaign: Campaign,
        @RequestParam("name") name: String,
        @RequestParam("mapColor") color: String
    ): String {
        logger.debug("Received kingdom name $name with map color $color")
        val kingdom = kingdomRepository.saveAndFlush(Kingdom(campaign, name, Color.decode(color)))
        return "redirect:/kingdom/${kingdom.id}"
    }

    @PostMapping("/{campaignId}/map")
    @Transactional
    fun createMap(
        @PathVariable("campaignId") campaign: Campaign,
        @RequestParam("name") name: String,
        @RequestParam("height", required = false) mapHeight: Int?,
        @RequestParam("width", required = false) mapWidth: Int?
    ): String {
        logger.debug("Received map name $name")
        val map = kingdomMapRepository.saveAndFlush( //Required to populate id
            if (mapHeight != null && mapWidth != null)
                kingdomMapGenerator.generateMap(campaign, name, mapHeight, mapWidth )
            else KingdomMap(campaign, name)
        )
        return "redirect:/map/${map.id}"
    }

    fun <E> MutableSet<E>.replaceAll(values: Set<E>) {
        removeAll { it !in values }
        addAll(values)
    }
}
