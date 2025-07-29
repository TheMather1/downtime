package pathfinder.web.frontend

import net.dv8tion.jda.api.entities.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import pathfinder.domain.CampaignRepository
import pathfinder.web.security.DiscordUser

interface FrontendController {
    val campaignRepository: CampaignRepository

    @ModelAttribute
    fun addUser(
        @AuthenticationPrincipal user: DiscordUser,
        model: Model
    ) {
        model.asUser(user)
    }

    fun Model.asUser(user: DiscordUser) {
        val userCampaigns = campaignRepository.findAllByUser(user)
        addAttribute("avatar", user.avatarUrl)
        addAttribute("username", user.name)
        addAttribute("campaigns", userCampaigns)
    }

    fun CampaignRepository.findAllByUser(user: User) = findAllByOwnersContains(user) +
            findAllByAdminsContains(user) +
            findAllByModeratorsContains(user) +
            findAllByServers(user.mutualGuilds)
}
