package pathfinder.web.backend

import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import pathfinder.domain.Campaign
import pathfinder.domain.CampaignRepository
import pathfinder.domain.kingdom.Kingdom
import pathfinder.domain.kingdom.settlement.Settlement
import pathfinder.domain.kingdom.terrain.KingdomMap
import pathfinder.domain.kingdom.terrain.KingdomMapRepository
import pathfinder.domain.kingdom.terrain.KingdomRepository
import pathfinder.domain.kingdom.terrain.TerrainType
import pathfinder.domain.kingdom.terrain.features.TerrainFeature
import pathfinder.domain.kingdom.terrain.improvements.Improvement
import pathfinder.domain.support.coordinate.HexCoordinate
import pathfinder.service.generation.KingdomMapGenerator
import pathfinder.web.security.DiscordUser
import java.awt.Color

@Controller
@RequestMapping("/api")
class BackendController(
    private val campaignRepository: CampaignRepository,
    private val kingdomRepository: KingdomRepository,
    private val kingdomMapRepository: KingdomMapRepository,
    private val kingdomMapGenerator: KingdomMapGenerator
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("/campaign")
    @Transactional
    fun createCampaign(
        @RequestParam("name") name: String,
        @AuthenticationPrincipal user: DiscordUser
    ): String {
        logger.debug("Received campaign name $name")
        val campaign = campaignRepository.saveAndFlush(Campaign(name, user)) //Required to populate id
        return "redirect:/campaign/" + campaign.id
    }

    @PostMapping("/campaign/{campaign}/kingdom")
    @Transactional
    fun createKingdom(
        @PathVariable("campaign") campaign: Campaign,
        @RequestParam("name") name: String,
        @RequestParam("mapColor") color: String
    ): String {
        logger.debug("Received kingdom name $name with map color $color")
        val kingdom = kingdomRepository.saveAndFlush(Kingdom(campaign, name, Color.decode(color)))
        return "redirect:/kingdom/${kingdom.id}"
    }

    @PostMapping("/campaign/{campaign}/map")
    @Transactional
    fun createMap(
        @PathVariable("campaign") campaign: Campaign,
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

    @PostMapping("/map/{map}")
    @Transactional
    fun updateHex(
        @PathVariable("map") map: KingdomMap,
        @RequestParam("hexQ") q: Int,
        @RequestParam("hexR") r: Int,
        @RequestParam("owner") owner: Kingdom?,
        @RequestParam("terrain") terrainType: TerrainType,
        @RequestParam("feature", required = false, defaultValue = "") features: Set<TerrainFeature>,
        @RequestParam("improvement", required = false, defaultValue = "") improvements: Set<Improvement>,
        @RequestParam("visibleText") visibleText: String,
        @RequestParam("hiddenText") hiddenText: String,
    ): String {
        map.update(HexCoordinate(q, r, 0)) { hex ->
            hex.rawTerrain = terrainType
            hex.owner = owner
            hex.terrainFeatures.replaceAll(features)
            hex.improvements.replaceAll(improvements)
            if((Improvement.ROAD in improvements || Improvement.HIGHWAY in improvements)
                && (TerrainFeature.RIVER in features || Improvement.CANAL in improvements))
                hex.terrainFeatures.add(TerrainFeature.BRIDGE)
            hex.freetextVisible = visibleText
            hex.freetextHidden = hiddenText
        }
        return "redirect:/map/${map.id}"
    }

    @GetMapping("/map/{map}")
    @Transactional
    fun getMap(
        @PathVariable("map") map: KingdomMap,
        @AuthenticationPrincipal user: DiscordUser
    ) = map

    @PostMapping("/map/{map}/settlement")
    fun createSettlement(
        @PathVariable("map") map: KingdomMap,
        @RequestParam("hexQ") q: Int,
        @RequestParam("hexR") r: Int,
        @RequestParam("name") name: String
    ): String {
        val coordinate = HexCoordinate(q, r, 0)
        map.update(coordinate) {
            it.settlement = Settlement(it, name)
        }
        val settlement = kingdomMapRepository.saveAndFlush(map)[coordinate]!!.settlement!!
        return "redirect:/settlement/${settlement.id}"
    }

    fun <E> MutableSet<E>.replaceAll(values: Set<E>) {
        removeAll { it !in values }
        addAll(values)
    }
}
