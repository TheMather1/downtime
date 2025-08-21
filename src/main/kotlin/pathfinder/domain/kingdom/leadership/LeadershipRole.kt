package pathfinder.domain.kingdom.leadership

import jakarta.persistence.*
import pathfinder.domain.character.PathfinderCharacter
import pathfinder.domain.kingdom.Kingdom
import pathfinder.domain.kingdom.KingdomScore

@MappedSuperclass
sealed class LeadershipRole: Cloneable {
    @ManyToOne
    var character: PathfinderCharacter? = null
    @get:Transient
    abstract val roleName: RoleName
    var performedDuty: Boolean = false
    var forcedVacancy: Boolean = false

    @Embeddable
    open class Ruler: LeadershipRole() {

        @Transient
        override val roleName = RoleName.RULER

        val bonus
            get() = character?.abilityScores?.charisma?.bonus ?: 0

        fun economyBonus(duties: RulerDuties) = if (performedDuty && duties.selected(KingdomScore.ECONOMY)) bonus
            else 0

        fun loyaltyBonus(duties: RulerDuties) = if (performedDuty && duties.selected(KingdomScore.LOYALTY)) bonus
            else 0

        fun stabilityBonus(duties: RulerDuties) = if (performedDuty && duties.selected(KingdomScore.STABILITY)) bonus
            else 0
    }

    @Embeddable
    class Consort: LeadershipRole() {

        @Transient
        override val roleName = RoleName.CONSORT
        @Column(name = "consort_co_ruler")
        var coRuler: Boolean = false

        val bonus
            get() = if (coRuler) character?.abilityScores?.charisma?.bonus ?: 0
                else character?.abilityScores?.charisma?.bonus?.div(2) ?: 0

        fun economyBonus(duties: RulerDuties) = if (coRuler && performedDuty && duties.selected(KingdomScore.ECONOMY)) bonus
            else 0

        fun loyaltyBonus(duties: RulerDuties) = when {
                !performedDuty -> 0
                coRuler && duties.selected(KingdomScore.LOYALTY) -> bonus
                else -> bonus
            }

        fun stabilityBonus(duties: RulerDuties) = if (performedDuty && duties.selected(KingdomScore.STABILITY)) bonus
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
    @AssociationOverride(
        name = "character",
        foreignKey = ForeignKey(
            foreignKeyDefinition = "FOREIGN KEY (character_id) REFERENCES pathfinder_character(id) ON DELETE SET NULL"
        )
    )
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
