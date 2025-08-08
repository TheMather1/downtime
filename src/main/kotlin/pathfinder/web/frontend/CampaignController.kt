package pathfinder.web.frontend

import jakarta.annotation.security.RolesAllowed
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pathfinder.domain.Campaign
import pathfinder.repository.CampaignRepository
import pathfinder.web.security.DiscordUser
import java.time.LocalDateTime
import java.util.*

@Controller
@RolesAllowed
@RequestMapping("/campaign/{campaign}")
class CampaignController(override val campaignRepository: CampaignRepository): FrontendController {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ModelAttribute
    fun addCampaign(@PathVariable campaign: Campaign) = campaign

    @GetMapping
    @PreAuthorize("#campaign.isUser(authentication.principal)")
    fun campaignPortal(
        @PathVariable campaign: Campaign
    ) = "campaign"

    @GetMapping("/maps")
    @PreAuthorize("#campaign.isUser(authentication.principal)")
    fun maps(
        @PathVariable campaign: Campaign,
    ) = "maps/index"

    @GetMapping("/kingdoms")
    @PreAuthorize("#campaign.isUser(authentication.principal)")
    fun kingdoms(
        @PathVariable campaign: Campaign,
    ) = "kingdoms/index"

    @GetMapping("/settlements")
    @PreAuthorize("#campaign.isUser(authentication.principal)")
    fun settlements(
        @PathVariable campaign: Campaign,
    ) = "settlements/index"

    @Transactional
    @PostMapping("/invite")
    @PreAuthorize("#campaign.isModerator(authentication.principal)")
    fun invite(
        @PathVariable campaign: Campaign,
        @RequestParam("exp") exp: LocalDateTime
    ) {
        val key = UUID.randomUUID().toString()
        campaign.inviteLinks[key] = exp
    }

    @Transactional
    @PreAuthorize("#campaign.isUser(authentication.principal)")
    @GetMapping("/invite")
    fun invite(
        @AuthenticationPrincipal user: DiscordUser,
        @PathVariable campaign: Campaign,
        @RequestParam("key") key: String
    ) = if (key in campaign.inviteLinks) {
        if(user !in campaign.members) campaign.users.add(user)
        "campaign"
    } else throw ResponseStatusException(HttpStatus.FORBIDDEN, "Invite link is invalid or expired.")

    @Transactional
    @PreAuthorize("#campaign.isUser(authentication.principal)")
    @GetMapping("/leave")
    fun leave(
        @AuthenticationPrincipal user: DiscordUser,
        @PathVariable campaign: Campaign
    ) {
        campaign.users.remove(user)
        if(campaign.members.isEmpty()) {
            campaignRepository.delete(campaign)
        }
    }
}
