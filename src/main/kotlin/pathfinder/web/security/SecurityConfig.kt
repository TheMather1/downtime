package pathfinder.web.security

import net.dv8tion.jda.api.JDA
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import pathfinder.repository.CampaignRepository

@Configuration
@EnableWebSecurity
class SecurityConfig(
    val jda: JDA,
    val publisher: DefaultAuthenticationEventPublisher,
    val campaignRepository: CampaignRepository
) {

    private val restTemplate = RestTemplate()

    @Bean
    fun configure(http: HttpSecurity, corsConfigurationSource: UrlBasedCorsConfigurationSource): SecurityFilterChain {
        publisher.setAdditionalExceptionMappings(mapOf(OAuth2AuthenticationException::class.java to FailureEvent::class.java))
        return http.cors {
            it.configurationSource(corsConfigurationSource)
        }.csrf {
            it.disable()
        }.authorizeHttpRequests {
            it.requestMatchers("/styles/**", "/js/**", "/img/**", "/actuator/**", "/").permitAll()
//            it.requestMatchers("/home/**", "/campaign/**", "/map/**", "/settlement/**", "/kingdom/**", "/api/**")
                .anyRequest().authenticated()
        }.oauth2Login {
            it.tokenEndpoint {
            }.userInfoEndpoint {
                it.userService(::loadUser)
            }
        }.logout {
            it.logoutUrl("/login").logoutSuccessUrl("/")
        }.build()
    }

    fun loadUser(userRequest: OAuth2UserRequest) = restTemplate.exchange<Map<String, String>>(
        userRequest.clientRegistration.providerDetails.userInfoEndpoint.uri,
        HttpMethod.GET,
        HttpEntity<Any>(HttpHeaders().apply {
            setBearerAuth(userRequest.accessToken.tokenValue)
            set(HttpHeaders.USER_AGENT, "KingdomBot")
        })
    ).body?.let { DiscordUser(it, jda, campaignRepository) }

    @Bean
    fun corsConfigurationSource() = UrlBasedCorsConfigurationSource().apply {
        registerCorsConfiguration("/**", CorsConfiguration().apply {
            allowedOrigins = listOf("https://discord.com")
            allowedHeaders = listOf("*")
            maxAge = 3600L
            allowedMethods = listOf("GET","POST", "OPTIONS")
            allowCredentials = true
        })
    }

    class FailureEvent(authentication: Authentication, exception: AuthenticationException)
        : AbstractAuthenticationFailureEvent(authentication, exception) {
        private val logger = LoggerFactory.getLogger(javaClass)
            init {
                logger.error("Encountered OAuth2AuthenticationException.", exception)
            }
        }
}
