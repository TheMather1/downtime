package pathfinder.web.backend

import net.dv8tion.jda.api.JDA
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pathfinder.domain.Campaign
import pathfinder.domain.CampaignRepository
import pathfinder.domain.kingdom.settlement.Settlement
import pathfinder.domain.kingdom.terrain.KingdomMap
import pathfinder.domain.kingdom.terrain.KingdomMapRepository
import pathfinder.domain.kingdom.terrain.TerrainType
import pathfinder.domain.kingdom.terrain.features.TerrainFeature
import pathfinder.domain.kingdom.terrain.improvements.Improvement
import pathfinder.domain.support.coordinate.HexCoordinate
import pathfinder.web.security.DiscordUser

@Controller
@RequestMapping("/api")
class BackendController(
    private val jda: JDA,
    private val campaignRepository: CampaignRepository,
    private val kingdomMapRepository: KingdomMapRepository
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

    @PostMapping("/campaign/{campaign}/map")
    @Transactional
    fun createMap(
        @PathVariable("campaign") campaign: Campaign,
        @RequestParam("name") name: String
    ): String {
        logger.debug("Received map name $name")
        val map = kingdomMapRepository.saveAndFlush(KingdomMap(campaign, name)) //Required to populate id
        return "redirect:/map/${map.id}"
    }

    @PostMapping("/map/{map}")
    @Transactional
    fun updateHex(
        @PathVariable("map") map: KingdomMap,
        @RequestParam("hexQ") q: Int,
        @RequestParam("hexR") r: Int,
        @RequestParam("terrain") terrainType: TerrainType,
        @RequestParam("river", required = false, defaultValue = "false") river: Boolean,
        @RequestParam("road", required = false) road: Improvement?
    ): String {
        val coordinate = HexCoordinate(q, r, 0)
        map.update(coordinate) { hex ->
            hex.rawTerrain = terrainType
            if (river) hex.terrainFeatures.add(TerrainFeature.RIVER)
            else hex.terrainFeatures.remove(TerrainFeature.RIVER)
            when(road) {
                null -> {
                    hex.improvements.remove(Improvement.ROAD)
                    hex.improvements.remove(Improvement.HIGHWAY)
                }
                Improvement.ROAD -> {
                    hex.improvements.remove(Improvement.HIGHWAY)
                    hex.improvements.add(Improvement.ROAD)
                }
                Improvement.HIGHWAY -> {
                    hex.improvements.remove(Improvement.ROAD)
                    hex.improvements.add(Improvement.HIGHWAY)
                }
                else -> throw ResponseStatusException(HttpStatus.BAD_REQUEST)
            }
            if (road != null && river) hex.terrainFeatures.add(TerrainFeature.BRIDGE)
            else hex.terrainFeatures.remove(TerrainFeature.BRIDGE)
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
        val settlement = kingdomMapRepository.saveAndFlush(map).get(coordinate)!!.settlement!!
        return "redirect:/settlement/${settlement.id}"
    }
}