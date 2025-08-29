package pathfinder.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class OptionalRules {

    @Column(nullable = false)
    var leadershipRoleSkills = false
}
