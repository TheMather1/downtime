package pathfinder.domain.kingdom.leadership

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.OneToOne
import jakarta.persistence.Transient
import pathfinder.domain.character.PathfinderCharacter
import pathfinder.domain.kingdom.Kingdom
import pathfinder.domain.kingdom.KingdomScore

@MappedSuperclass
sealed class LeadershipRole: Cloneable {
    @ManyToOne
    @JoinColumn(insertable = false, updatable = false)
    var character: PathfinderCharacter? = null
    @get:Transient
    abstract val roleName: RoleName
    var performedDuty: Boolean = false
    var forcedVacancy: Boolean = false

    @Embeddable
    open class Ruler: LeadershipRole() {

        @Transient
        override val roleName = RoleName.RULER

        fun economyBonus(duties: RulerDuties) = if (performedDuty && duties.selected(KingdomScore.ECONOMY)) character?.abilityScores?.charisma?.bonus ?: 0
            else 0

        fun loyaltyBonus(duties: RulerDuties) = if (performedDuty && duties.selected(KingdomScore.LOYALTY)) character?.abilityScores?.charisma?.bonus ?: 0
            else 0

        fun  stabilityBonus(duties: RulerDuties) = if (performedDuty && duties.selected(KingdomScore.STABILITY)) character?.abilityScores?.charisma?.bonus ?: 0
            else 0
    }

    @Embeddable
    class Consort: LeadershipRole() {

        @Transient
        override val roleName = RoleName.CONSORT
        @Column(name = "consort_co_ruler")
        var coRuler: Boolean = false

        fun economyBonus(duties: RulerDuties) = if (coRuler && performedDuty && duties.selected(KingdomScore.ECONOMY)) character?.abilityScores?.charisma?.bonus ?: 0
            else 0

        fun loyaltyBonus(duties: RulerDuties) = when {
                !coRuler -> character?.abilityScores?.charisma?.bonus?.div(2) ?: 0
                performedDuty && duties.selected(KingdomScore.LOYALTY) -> character?.abilityScores?.charisma?.bonus ?: 0
                else -> 0
            }

        fun stabilityBonus(duties: RulerDuties) = if (performedDuty && duties.selected(KingdomScore.STABILITY)) character?.abilityScores?.charisma?.bonus ?: 0
            else 0

    }

    @Embeddable
    class Heir: LeadershipRole() {

        @Transient
        override val roleName = RoleName.HEIR
        val loyaltyBonus
            get() = if(performedDuty) character?.abilityScores?.charisma?.bonus?.div(2) ?: 0
            else 0
    }
    @Embeddable
    class Councillor: LeadershipRole() {

        @Transient
        override val roleName = RoleName.COUNCILLOR
        val loyaltyBonus
            get() = if(performedDuty) character?.abilityScores?.run {
                maxOf(charisma.bonus, wisdom.bonus)
            } ?: -2
            else -2
    }
    @Embeddable
    class General: LeadershipRole() {

        @Transient
        override val roleName = RoleName.GENERAL
        val stabilityBonus
            get() = if(performedDuty) character?.abilityScores?.run {
                maxOf(charisma.bonus, strength.bonus)
            } ?: 0
            else 0

        val loyaltyBonus
            get() = if(!performedDuty || character == null) -4 else 0
    }
    @Embeddable
    class GrandDiplomat: LeadershipRole() {

        @Transient
        override val roleName = RoleName.GRAND_DIPLOMAT
        val stabilityBonus
            get() = if(performedDuty) character?.abilityScores?.run {
                maxOf(charisma.bonus, intelligence.bonus)
            } ?: -2
            else -2
    }
    @Embeddable
    class HighPriest: LeadershipRole() {

        @Transient
        override val roleName = RoleName.HIGH_PRIEST
        val stabilityBonus
            get() = if(performedDuty) character?.abilityScores?.run {
                maxOf(charisma.bonus, wisdom.bonus)
            } ?: -2
            else -2

        val loyaltyBonus
            get() = if(!performedDuty || character == null) -2 else 0
    }
    @Embeddable
    class Magister: LeadershipRole() {

        @Transient
        override val roleName = RoleName.MAGISTER
        val economyBonus
            get() = if(performedDuty) character?.abilityScores?.run {
                maxOf(charisma.bonus, intelligence.bonus)
            } ?: -4
            else -4
    }
    @Embeddable
    class Marshal: LeadershipRole() {

        @Transient
        override val roleName = RoleName.MARSHAL
        val economyBonus
            get() = if(performedDuty) character?.abilityScores?.run {
                maxOf(dexterity.bonus, wisdom.bonus)
            } ?: -4
            else -4
    }
    @Embeddable
    class RoyalEnforcer: LeadershipRole() {

        @Transient
        override val roleName = RoleName.ROYAL_ENFORCER
        val loyaltyBonus
            get() = if(performedDuty) character?.abilityScores?.run {
                maxOf(dexterity.bonus, strength.bonus)
            } ?: 0
            else 0
    }
    @Embeddable
    class Spymaster: LeadershipRole() {

        @Column(name = "spymaster_score")
        var score: KingdomScore? = null
        @Transient
        override val roleName = RoleName.SPYMASTER
        val economyBonus
            get() = when {
                !performedDuty || character == null -> -4
                score == KingdomScore.ECONOMY -> character!!.abilityScores.run {
                    maxOf(dexterity.bonus, intelligence.bonus)
                }

                else -> 0
            }
        val loyaltyBonus
            get() = if(performedDuty && score == KingdomScore.LOYALTY) character?.abilityScores?.run {
                maxOf(dexterity.bonus, intelligence.bonus)
            } ?: 0
            else 0
        val stabilityBonus
            get() = if(performedDuty && score == KingdomScore.STABILITY) character?.abilityScores?.run {
                maxOf(dexterity.bonus, intelligence.bonus)
            } ?: 0
            else 0
    }
    @Embeddable
    class Treasurer: LeadershipRole() {

        @Transient
        override val roleName = RoleName.TREASURER
        val economyBonus
            get() = if(performedDuty) character?.abilityScores?.run {
                maxOf(intelligence.bonus, wisdom.bonus)
            } ?: -4
            else -4
    }
    @Embeddable
    class Warden: LeadershipRole() {

        @Transient
        override val roleName = RoleName.WARDEN
        val loyalty
            get() = if(performedDuty) character?.abilityScores?.run {
                maxOf(constitution.bonus, strength.bonus)
            } ?: -2
            else -2

        val stabilityBonus
            get() = if(performedDuty) 0 else 2
    }
    @Entity
    class Viceroy(
        @ManyToOne
        val government: Government,
        @OneToOne
        val videroyOf: Kingdom
    ): LeadershipRole() {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0
        @Transient
        override val roleName = RoleName.VICEROY
        val economyBonus
            get() = if(performedDuty) character?.abilityScores?.run {
                maxOf(intelligence.bonus, wisdom.bonus)
            } ?: 0
            else 0
    }

    enum class RoleName {
        RULER,
        CONSORT,
        HEIR,
        COUNCILLOR,
        GENERAL,
        GRAND_DIPLOMAT,
        HIGH_PRIEST,
        MAGISTER,
        MARSHAL,
        ROYAL_ENFORCER,
        SPYMASTER,
        TREASURER,
        WARDEN,
        VICEROY;
    }
}
