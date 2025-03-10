package pathfinder.web.security

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority

class DiscordUser(
    private val attributes: Map<String, String>,
    jda: JDA
) : OAuth2User, User by jda.retrieveUserById(attributes["id"]!!).complete() {

    override fun getAttributes() = attributes

    override fun getAuthorities() = setOf<GrantedAuthority>(OAuth2UserAuthority(attributes))

    override fun getMutualGuilds(): MutableList<Guild> {
        jda.guilds.forEach { it.loadMembers().get() }
        return jda.getMutualGuilds(this)
    }
}