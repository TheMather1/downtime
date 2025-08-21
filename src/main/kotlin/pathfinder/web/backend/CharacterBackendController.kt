package pathfinder.web.backend

import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import pathfinder.domain.Campaign
import pathfinder.domain.Role
import pathfinder.domain.character.PathfinderCharacter
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
        model.addAttribute("users", campaign.members.filter { it.idLong == user.idLong != campaign.isAdmin(user)})
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

    fun <E> MutableSet<E>.replaceAll(values: Set<E>) {
        removeAll { it !in values }
        addAll(values)
    }
}
