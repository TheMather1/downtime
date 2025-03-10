package pathfinder.domain.downtime

import jakarta.persistence.Convert
import jakarta.persistence.Embedded
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import net.dv8tion.jda.api.entities.User
import pathfinder.domain.support.jpa.DiscordUserConverter

class Character(
    val name: String,
    @Convert(converter = DiscordUserConverter::class)
    val player: User?
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Embedded
    val assets: Assets = Assets()
}
