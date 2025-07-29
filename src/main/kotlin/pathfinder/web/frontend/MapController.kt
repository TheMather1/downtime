package pathfinder.web.frontend

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import pathfinder.domain.CampaignRepository
import pathfinder.domain.kingdom.terrain.KingdomMap
import pathfinder.domain.kingdom.terrain.TerrainType
import pathfinder.domain.support.coordinate.HexCoordinate
import pathfinder.web.frontend.dto.MapConfig

@Controller
@RequestMapping("/map/{map}")
class MapController(override val campaignRepository: CampaignRepository): FrontendController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @ModelAttribute
    fun addMap(
        @PathVariable map: KingdomMap
    ) = map

    @ModelAttribute
    fun addCampaign(
        @PathVariable map: KingdomMap
    ) = map.campaign

    @ModelAttribute("config")
    fun addConfig(
        @PathVariable(required = false) map: KingdomMap,
        @RequestParam("z", required = false, defaultValue = "0") z: Int,
    ) = MapConfig(
        offsetX = map.offsetX,
        offsetY = map.offsetY,
        z = z,
    )

    @GetMapping
    fun mapView() = "maps/map"

    @GetMapping("/template")
    fun templateView() = "maps/mapTemplate"

    @GetMapping("/hex")
    fun getHexForm(
        @RequestParam("q") q: Int,
        @RequestParam("r") r: Int,
        @RequestParam("z") z: Int,
        @RequestParam("terrain", required = false) terrain: TerrainType?,
        @PathVariable map: KingdomMap,
        model: Model
    ): String {
        val coordinate = HexCoordinate(q, r, z)
        model.addAttribute("coordinate", coordinate)
        val hex = map.get(coordinate)
        model.addAttribute("hex", hex)
        model.addAttribute("terrain", terrain ?: hex?.terrain)
        return "maps/formTemplate"
    }
}
