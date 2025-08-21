package pathfinder.web.frontend

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import pathfinder.domain.character.PathfinderCharacter
import pathfinder.repository.CampaignRepository

@Controller
@RequestMapping("/character/{characterId}")
class CharacterController(override val campaignRepository: CampaignRepository): FrontendController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @ModelAttribute("character")
    fun addCharacter(
        @PathVariable("characterId") character: PathfinderCharacter
    ) = character

    @ModelAttribute("campaign")
    fun addCampaign(
        @PathVariable("characterId") character: PathfinderCharacter
    ) = character.campaign

    @GetMapping
    fun characterView(
        @PathVariable("characterId") character: PathfinderCharacter,
    ) = "characters/character"

    @GetMapping("/abilityScores")
    fun abilityScores() = "characters/abilityScores"

    @GetMapping("/speed")
    fun speed() = "characters/speed"

    @GetMapping("/armorClass")
    fun armorClass() = "characters/armorClass"

    @GetMapping("/saves")
    fun saves() = "characters/saves"

    @GetMapping("/skills")
    fun skills() = "characters/skills"
}
