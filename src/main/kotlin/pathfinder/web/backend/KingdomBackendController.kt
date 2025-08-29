package pathfinder.web.backend

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import pathfinder.domain.kingdom.Kingdom
import pathfinder.repository.KingdomRepository

@Controller
@RequestMapping("/api/kingdom")
class KingdomBackendController(
    private val kingdomRepository: KingdomRepository,
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @DeleteMapping("/{kingdomId}")
    @Transactional
    fun deleteCampaign(@PathVariable("kingdomId") kingdom: Kingdom): String {
        logger.debug("Received request to delete kingdom ${kingdom.id}")
        kingdomRepository.delete(kingdom)
        return "redirect:/campaign/${kingdom.campaign.id}/kingdoms"

    }

    fun <E> MutableSet<E>.replaceAll(values: Set<E>) {
        removeAll { it !in values }
        addAll(values)
    }
}
