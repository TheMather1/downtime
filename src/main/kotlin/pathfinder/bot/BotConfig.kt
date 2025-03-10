package pathfinder.bot

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("pathfinder.weather.bot")
class BotConfig {
    private val logger = LoggerFactory.getLogger(javaClass)

    lateinit var token: String

    @Bean
    fun bot() =
        JDABuilder.createDefault(token, GatewayIntent.GUILD_MEMBERS)
            .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
            .setRequestTimeoutRetry(false).setMaxReconnectDelay(32)
            .build().apply {
                awaitReady()
                unavailableGuilds.forEach {
                    logger.warn("Unavailable guild: $it")
                }
                presence.activity = Activity.watching("the skies")
            }
}