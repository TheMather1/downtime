package pathfinder.domain

import jakarta.persistence.*
import jakarta.transaction.Transactional
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.User
import pathfinder.domain.character.PathfinderCharacter
import pathfinder.domain.kingdom.Kingdom
import pathfinder.domain.kingdom.settlement.Settlement
import pathfinder.domain.kingdom.terrain.KingdomMap
import pathfinder.domain.support.jpa.DiscordGuildConverter
import pathfinder.domain.support.jpa.DiscordUserConverter
import java.time.LocalDateTime

@Entity
open class Campaign(
    var name: String,
    creator: User
): Comparable<Campaign> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Convert(converter = DiscordUserConverter::class)
    @ElementCollection(fetch = FetchType.EAGER)
    val owners = mutableSetOf<User>(creator)

    @Convert(converter = DiscordUserConverter::class)
    @ElementCollection(fetch = FetchType.EAGER)
    val admins = mutableSetOf<User>()

    @Convert(converter = DiscordUserConverter::class)
    @ElementCollection(fetch = FetchType.EAGER)
    val moderators = mutableSetOf<User>()

    @Convert(converter = DiscordUserConverter::class)
    @ElementCollection(fetch = FetchType.EAGER)
    val users = mutableSetOf<User>()

    @Convert(converter = DiscordGuildConverter::class)
    @ElementCollection(fetch = FetchType.EAGER)
    val servers = mutableSetOf<Guild>()

    @OneToMany(mappedBy = "campaign", cascade = [CascadeType.ALL], orphanRemoval = true)
    val maps = mutableSetOf<KingdomMap>()

    @OneToMany(mappedBy = "campaign", cascade = [CascadeType.ALL], orphanRemoval = true)
    val kingdoms = mutableSetOf<Kingdom>()

    @OneToMany(mappedBy = "campaign", cascade = [CascadeType.ALL], orphanRemoval = true)
    val characters = mutableSetOf<PathfinderCharacter>()

    @Embedded
    val optionalRules = OptionalRules()

    @ElementCollection
    val inviteLinks = mutableMapOf<String, LocalDateTime>()

    @Transactional
    @PostLoad
    open fun removeExpiredInviteLinks() {
        inviteLinks.forEach { (key, exp) -> if(exp < LocalDateTime.now()) inviteLinks.remove(key) }
    }

    val members: Set<User>
        get() = owners + admins + moderators + users

    val settlements: Set<Settlement>
        get() = maps.flatMap(KingdomMap::settlements).toSet()

    fun getRole(user: User) = when {
        owners.contains(user) -> Role.OWNER
        admins.contains(user) -> Role.ADMIN
        moderators.contains(user) -> Role.MODERATOR
        users.contains(user) -> Role.USER
        else -> null
    }

    fun hasRole(user: User, role: Role) = getRole(user)?.let {
        it >= role
    } ?: false

    fun getUser(id: Long) = members.find { it.idLong == id }

    fun isUser(user: User) = hasRole(user, Role.USER)
    fun isAdmin(user: User) = hasRole(user, Role.ADMIN)
    fun isModerator(user: User) = hasRole(user, Role.MODERATOR)
    fun isOwner(user: User) = hasRole(user, Role.OWNER)

    fun assignRole(user: User, role: Role) {
        when (role) {
            Role.OWNER -> {
                admins.remove(user) || moderators.remove(user) || users.remove(user)
                owners.add(user)
            }
            Role.ADMIN -> {
                owners.remove(user) || moderators.remove(user) || users.remove(user)
                admins.add(user)
            }
            Role.MODERATOR -> {
                owners.remove(user) || admins.remove(user) || users.remove(user)
                moderators.add(user)
            }
            Role.USER -> {
                owners.remove(user) || admins.remove(user) || moderators.remove(user)
                users.add(user)
            }
        }
    }

    override fun compareTo(other: Campaign): Int {
        return name.compareTo(other.name).takeUnless { it == 0 } ?: id.compareTo(other.id)
    }
}
