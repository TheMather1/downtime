package pathfinder.web.frontend

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class LandingPageController {

    @GetMapping
    fun landingPage() = "index"
}
