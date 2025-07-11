package pathfinder.domain.kingdom.leadership

import jakarta.persistence.AttributeOverride
import jakarta.persistence.AttributeOverrides
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
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

    @Embedded
    @AttributeOverrides(
        value = [
            AttributeOverride(name = "character", column = Column(name = "ruler_character_id")),
            AttributeOverride(name = "performedDuty", column = Column(name = "ruler_performed_duty")),
            AttributeOverride(name = "forcedVacancy", column = Column(name = "ruler_forced_vacancy"))
        ]
    )
    val ruler: LeadershipRole.Ruler = LeadershipRole.Ruler(),

    @Embedded
    @AttributeOverrides(
        value = [
            AttributeOverride(name = "character", column = Column(name = "consort_character_id")),
            AttributeOverride(name = "performedDuty", column = Column(name = "consort_performed_duty")),
            AttributeOverride(name = "forcedVacancy", column = Column(name = "consort_forced_vacancy"))
        ]
    )
    val consort: LeadershipRole.Consort = LeadershipRole.Consort(),

    @Embedded
    @AttributeOverrides(
        value = [
            AttributeOverride(name = "character", column = Column(name = "heir_character_id")),
            AttributeOverride(name = "performedDuty", column = Column(name = "heir_performed_duty")),
            AttributeOverride(name = "forcedVacancy", column = Column(name = "heir_forced_vacancy"))
        ]
    )
    val heir: LeadershipRole.Heir = LeadershipRole.Heir(),

    @Embedded
    @AttributeOverrides(
        value = [
            AttributeOverride(name = "character", column = Column(name = "councillor_character_id")),
            AttributeOverride(name = "performedDuty", column = Column(name = "councillor_performed_duty")),
            AttributeOverride(name = "forcedVacancy", column = Column(name = "councillor_forced_vacancy"))
        ]
    )
    val councillor: LeadershipRole.Councillor = LeadershipRole.Councillor(),

    @Embedded
    @AttributeOverrides(
        value = [
            AttributeOverride(name = "character", column = Column(name = "general_character_id")),
            AttributeOverride(name = "performedDuty", column = Column(name = "general_performed_duty")),
            AttributeOverride(name = "forcedVacancy", column = Column(name = "general_forced_vacancy"))
        ]
    )
    val general: LeadershipRole.General = LeadershipRole.General(),

    @Embedded
    @AttributeOverrides(
        value = [
            AttributeOverride(name = "character", column = Column(name = "grand_diplomat_character_id")),
            AttributeOverride(name = "performedDuty", column = Column(name = "grand_diplomat_performed_duty")),
            AttributeOverride(name = "forcedVacancy", column = Column(name = "grand_diplomat_forced_vacancy"))
        ]
    )
    val grandDiplomat: LeadershipRole.GrandDiplomat = LeadershipRole.GrandDiplomat(),

    @Embedded
    @AttributeOverrides(
        value = [
            AttributeOverride(name = "character", column = Column(name = "high_priest_character_id")),
            AttributeOverride(name = "performedDuty", column = Column(name = "high_priest_performed_duty")),
            AttributeOverride(name = "forcedVacancy", column = Column(name = "high_priest_forced_vacancy"))
        ]
    )
    val highPriest: LeadershipRole.HighPriest = LeadershipRole.HighPriest(),

    @Embedded
    @AttributeOverrides(
        value = [
            AttributeOverride(name = "character", column = Column(name = "magister_character_id")),
            AttributeOverride(name = "performedDuty", column = Column(name = "magister_performed_duty")),
            AttributeOverride(name = "forcedVacancy", column = Column(name = "magister_forced_vacancy"))
        ]
    )
    val magister: LeadershipRole.Magister = LeadershipRole.Magister(),

    @Embedded
    @AttributeOverrides(
        value = [
            AttributeOverride(name = "character", column = Column(name = "marshal_character_id")),
            AttributeOverride(name = "performedDuty", column = Column(name = "marshal_performed_duty")),
            AttributeOverride(name = "forcedVacancy", column = Column(name = "marshal_forced_vacancy"))
        ]
    )
    val marshal: LeadershipRole.Marshal = LeadershipRole.Marshal(),

    @Embedded
    @AttributeOverrides(
        value = [
            AttributeOverride(name = "character", column = Column(name = "royal_enforcer_character_id")),
            AttributeOverride(name = "performedDuty", column = Column(name = "royal_enforcer_performed_duty")),
            AttributeOverride(name = "forcedVacancy", column = Column(name = "royal_enforcer_forced_vacancy"))
        ]
    )
    val royalEnforcer: LeadershipRole.RoyalEnforcer = LeadershipRole.RoyalEnforcer(),

    @Embedded
    @AttributeOverrides(
        value = [
            AttributeOverride(name = "character", column = Column(name = "spymaster_character_id")),
            AttributeOverride(name = "performedDuty", column = Column(name = "spymaster_performed_duty")),
            AttributeOverride(name = "forcedVacancy", column = Column(name = "spymaster_forced_vacancy"))
        ]
    )
    val spymaster: LeadershipRole.Spymaster = LeadershipRole.Spymaster(),

    @Embedded
    @AttributeOverrides(
        value = [
            AttributeOverride(name = "character", column = Column(name = "treasurer_character_id")),
            AttributeOverride(name = "performedDuty", column = Column(name = "treasurer_performed_duty")),
            AttributeOverride(name = "forcedVacancy", column = Column(name = "treasurer_forced_vacancy"))
        ]
    )
    val treasurer: LeadershipRole.Treasurer = LeadershipRole.Treasurer(),

    @Embedded
    @AttributeOverrides(
        value = [
            AttributeOverride(name = "character", column = Column(name = "warden_character_id")),
            AttributeOverride(name = "performedDuty", column = Column(name = "warden_performed_duty")),
            AttributeOverride(name = "forcedVacancy", column = Column(name = "warden_forced_vacancy"))
        ]
    )
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
