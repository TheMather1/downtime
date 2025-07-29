package pathfinder.domain

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CampaignRepository: JpaRepository<Campaign, Long> {
    @Query("select campaign from Campaign campaign where exists (select server from campaign.servers server where server in :servers)")
    fun findAllByServers(servers: Collection<Guild>): Set<Campaign>

    fun findAllByOwnersContains(user: User): Set<Campaign>
    fun findAllByAdminsContains(user: User): Set<Campaign>
    fun findAllByModeratorsContains(user: User): Set<Campaign>
    fun findAllByUsersContains(user: User): Set<Campaign>
}
