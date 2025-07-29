package pathfinder.web.frontend

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import pathfinder.domain.CampaignRepository

@Controller
@RequestMapping("/home")
class HomeController(override val campaignRepository: CampaignRepository): FrontendController {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping
    fun landingPortal() = "home"
}
