package pathfinder.web.backend

import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import pathfinder.domain.Campaign
import pathfinder.domain.Role
import pathfinder.domain.character.PathfinderCharacter
import pathfinder.domain.character.stats.Bonus
import pathfinder.domain.character.stats.BonusType
import pathfinder.repository.CharacterRepository
import pathfinder.web.security.DiscordUser

@Controller
@RequestMapping("/api/character")
class CharacterBackendController(
    val characterRepository: CharacterRepository
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/form/{campaignId}")
    fun getForm(
        @PathVariable("campaignId") campaign: Campaign,
        @RequestParam("character", required = false) character: PathfinderCharacter?,
        @AuthenticationPrincipal user: DiscordUser,
        model: Model
    ): String {
        model.addAttribute("campaign", campaign)
        model.addAttribute("users", campaign.members.filter { campaign.isAdmin(user) || it.idLong == user.idLong })
        return "characters/templates/characterForm"
    }

    @GetMapping("{character}/form")
    fun getForm(
        @PathVariable("character") character: PathfinderCharacter,
        @AuthenticationPrincipal user: DiscordUser,
        model: Model
    ): String {
        model.addAttribute("campaign", character.campaign)
        model.addAttribute("users", character.campaign.members.filter { character.campaign.isAdmin(user) || it.idLong == user.idLong })
        model.addAttribute("character", character)
        return "characters/templates/characterForm"
    }

    @PostMapping("/{character}")
    @Transactional
    fun editCharacter(
        @PathVariable("character", required = false) oldCharacter: PathfinderCharacter?,
        @RequestParam("campaign") campaign: Campaign,
        @RequestParam("player", required = false) ownerId: Long?,
        @RequestParam("name") name: String,
        @RequestParam("race") race: String,
        @RequestParam("baseStrength") baseStrength: Int,
        @RequestParam("baseDexterity") baseDexterity: Int,
        @RequestParam("baseConstitution") baseConstitution: Int,
        @RequestParam("baseIntelligence") baseIntelligence: Int,
        @RequestParam("baseWisdom") baseWisdom: Int,
        @RequestParam("baseCharisma") baseCharisma: Int,
        @AuthenticationPrincipal user: DiscordUser
    ): String {
        var character = oldCharacter ?: PathfinderCharacter(name, race, ownerId, campaign)
        assert(campaign.hasRole(user, Role.MODERATOR) || character.ownerId == user.idLong)
        character.ownerId = ownerId
        character.name = name
        character.race = race
        character.abilityScores.apply {
            strength.base = baseStrength
            dexterity.base = baseDexterity
            constitution.base = baseConstitution
            intelligence.base = baseIntelligence
            wisdom.base = baseWisdom
            charisma.base = baseCharisma
        }
        if (oldCharacter == null) character = characterRepository.save(character)
        return "redirect:/character/${character.id}"
    }

    @DeleteMapping("/{character}")
    @Transactional
    fun deleteCharacter(@PathVariable("character") character: PathfinderCharacter): String {
        logger.debug("Received request to delete character ${character.id}")
        characterRepository.delete(character)
        return "redirect:/campaign/${character.campaign.id}/characters"
    }

    @PostMapping("/{character}/bonus")
    @Transactional
    fun addBonus(
        @PathVariable("character") character: PathfinderCharacter,
        @RequestParam("type") type: BonusType,
        @RequestParam("value") value: Int,
        @RequestParam("stat") stat: String,
        @RequestHeader("referer") referer: String,
    ): String {
        character.getStat(stat)?.takeIf { type.isApplicableTo(it) }?.bonuses?.add(Bonus(type, value))
        return "redirect:$referer"
    }

    @DeleteMapping("/{character}/bonus")
    @Transactional
    fun deleteBonus(
        @PathVariable("character") character: PathfinderCharacter,
        @RequestParam("type") type: BonusType,
        @RequestParam("value") value: Int,
        @RequestParam("stat") stat: String,
        @RequestHeader("referer") referer: String,
        response: HttpServletResponse,
    ) {
        character.getStat(stat)?.bonuses?.apply {
            removeAt(indexOfFirst { it.type == type && it.value == value })
        }
        response.addHeader("HX-Refresh", "true")
    }

    fun <E> MutableSet<E>.replaceAll(values: Set<E>) {
        removeAll { it !in values }
        addAll(values)
    }
}
