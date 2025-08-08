package pathfinder.web.security

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority
import pathfinder.repository.CampaignRepository

class DiscordUser(
    private val attributes: Map<String, String>,
    jda: JDA,
    private val campaignRepository: CampaignRepository
) : OAuth2User, User by jda.retrieveUserById(attributes["id"]!!).complete() {

    override fun getAttributes() = attributes

    override fun getAuthorities() = setOf<GrantedAuthority>(OAuth2UserAuthority(attributes)) +
            campaignRepository.findAllByOwnersContains(this).flatMap { setOf(
                SimpleGrantedAuthority("ROLE_OWNER_${it.id}"),
                SimpleGrantedAuthority("ROLE_ADMIN_${it.id}"),
                SimpleGrantedAuthority("ROLE_MODERATOR_${it.id}"),
                SimpleGrantedAuthority("ROLE_USER_${it.id}")
            ) } +
            campaignRepository.findAllByAdminsContains(this).flatMap { setOf(
                SimpleGrantedAuthority("ROLE_ADMIN_${it.id}"),
                SimpleGrantedAuthority("ROLE_MODERATOR_${it.id}"),
                SimpleGrantedAuthority("ROLE_USER_${it.id}")
            ) } +
            campaignRepository.findAllByModeratorsContains(this).flatMap { setOf(
                SimpleGrantedAuthority("ROLE_MODERATOR_${it.id}"),
                SimpleGrantedAuthority("ROLE_USER_${it.id}")
            )} +
            campaignRepository.findAllByUsersContains(this).map { SimpleGrantedAuthority("ROLE_USER_${it.id}") }

    override fun getMutualGuilds(): MutableList<Guild> {
        jda.guilds.forEach { it.loadMembers().get() }
        return jda.getMutualGuilds(this)
    }

    override fun equals(other: Any?): Boolean = if (other is User) other.id == id
    else super.equals(other)
}
