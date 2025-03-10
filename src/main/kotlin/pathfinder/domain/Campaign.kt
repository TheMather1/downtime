package pathfinder.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Convert
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.User
import pathfinder.domain.kingdom.Kingdom
import pathfinder.domain.kingdom.terrain.KingdomMap
import pathfinder.domain.support.jpa.DiscordGuildConverter
import pathfinder.domain.support.jpa.DiscordUserConverter

@Entity
class Campaign(
    var name: String,
    creator: User
): Comparable<Campaign> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Convert(converter = DiscordUserConverter::class)
    @ElementCollection
    val owners = mutableSetOf<User>(creator)

    @Convert(converter = DiscordUserConverter::class)
    @ElementCollection
    val admins = mutableSetOf<User>()

    @Convert(converter = DiscordUserConverter::class)
    @ElementCollection
    val moderators = mutableSetOf<User>()

    @Convert(converter = DiscordGuildConverter::class)
    @ElementCollection
    val servers = mutableSetOf<Guild>()

    @OneToMany(mappedBy = "campaign", cascade = [CascadeType.ALL], orphanRemoval = true)
    val maps = mutableSetOf<KingdomMap>()

    @OneToMany(mappedBy = "campaign", cascade = [CascadeType.ALL], orphanRemoval = true)
    val kingdoms = mutableSetOf<Kingdom>()

    override fun compareTo(other: Campaign): Int {
        return name.compareTo(other.name).takeUnless { it == 0 } ?: id.compareTo(other.id)
    }
}