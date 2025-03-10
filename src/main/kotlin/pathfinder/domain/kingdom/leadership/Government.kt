package pathfinder.domain.kingdom.leadership

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import pathfinder.domain.kingdom.Kingdom

@Entity
data class Government(
    @OneToOne
    val kingdom: Kingdom,
    val ruler: LeadershipRole.Ruler = LeadershipRole.Ruler(),
    val consort: LeadershipRole.Consort = LeadershipRole.Consort(),
    val heir: LeadershipRole.Heir = LeadershipRole.Heir(),
    val councillor: LeadershipRole.Councillor = LeadershipRole.Councillor(),
    val general: LeadershipRole.General = LeadershipRole.General(),
    val grandDiplomat: LeadershipRole.GrandDiplomat = LeadershipRole.GrandDiplomat(),
    val highPriest: LeadershipRole.HighPriest = LeadershipRole.HighPriest(),
    val magister: LeadershipRole.Magister = LeadershipRole.Magister(),
    val marshal: LeadershipRole.Marshal = LeadershipRole.Marshal(),
    val royalEnforcer: LeadershipRole.RoyalEnforcer = LeadershipRole.RoyalEnforcer(),
    val spymaster: LeadershipRole.Spymaster = LeadershipRole.Spymaster(),
    val treasurer: LeadershipRole.Treasurer = LeadershipRole.Treasurer(),
    val warden: LeadershipRole.Warden = LeadershipRole.Warden(),
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "government")
    val viceroys: MutableSet<LeadershipRole.Viceroy> = mutableSetOf(),
    val rulerDuties: RulerDuties = RulerDuties()
): Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    val economyBonus
        get() = ruler.economyBonus(rulerDuties) +
                consort.economyBonus(rulerDuties) +
                magister.economyBonus +
                marshal.economyBonus +
                spymaster.economyBonus +
                treasurer.economyBonus +
                viceroys.sumOf(LeadershipRole.Viceroy::economyBonus)
    val loyaltyBonus
        get() = ruler.loyaltyBonus(rulerDuties) +
                consort.loyaltyBonus(rulerDuties) +
                councillor.loyaltyBonus +
                general.loyaltyBonus +
                heir.loyaltyBonus +
                highPriest.loyaltyBonus +
                royalEnforcer.loyaltyBonus +
                spymaster.loyaltyBonus +
                warden.loyalty
    val stabilityBonus
        get() = ruler.stabilityBonus(rulerDuties) +
                consort.stabilityBonus(rulerDuties) +
                general.stabilityBonus +
                grandDiplomat.stabilityBonus +
                highPriest.stabilityBonus +
                spymaster.stabilityBonus

    val nonRulerRoles
        get() = setOf(
            consort,
            heir,
            councillor,
            general,
            grandDiplomat,
            highPriest,
            magister,
            marshal,
            royalEnforcer,
            spymaster,
            treasurer,
            warden,
        ) + viceroys

    fun getRole(roleName: LeadershipRole.RoleName) = when (roleName) {
        LeadershipRole.RoleName.RULER -> ruler
        LeadershipRole.RoleName.CONSORT -> consort
        LeadershipRole.RoleName.HEIR -> heir
        LeadershipRole.RoleName.COUNCILLOR -> councillor
        LeadershipRole.RoleName.GENERAL -> general
        LeadershipRole.RoleName.GRAND_DIPLOMAT -> grandDiplomat
        LeadershipRole.RoleName.HIGH_PRIEST -> highPriest
        LeadershipRole.RoleName.MAGISTER -> magister
        LeadershipRole.RoleName.MARSHAL -> marshal
        LeadershipRole.RoleName.ROYAL_ENFORCER -> royalEnforcer
        LeadershipRole.RoleName.SPYMASTER -> spymaster
        LeadershipRole.RoleName.TREASURER -> treasurer
        LeadershipRole.RoleName.WARDEN -> warden
        LeadershipRole.RoleName.VICEROY -> null
    }

    val roles
        get() = nonRulerRoles + ruler

    val characters
        get() = roles.mapNotNull { it.character }

    val nonRulers
        get() = nonRulerRoles.mapNotNull { it.character }

    public override fun clone() = super.clone() as Government
}