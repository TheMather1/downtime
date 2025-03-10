package pathfinder.web.frontend

import net.dv8tion.jda.api.JDA
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import pathfinder.domain.CampaignRepository

@Controller
@RequestMapping("/home")
class HomeController(jda: JDA, campaignRepository: CampaignRepository): FrontendController(jda, campaignRepository) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping
    fun landingPortal() = "home"
}
