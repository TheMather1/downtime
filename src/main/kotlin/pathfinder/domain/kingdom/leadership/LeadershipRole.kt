package pathfinder.domain.kingdom.leadership

import jakarta.persistence.*
import pathfinder.domain.character.PathfinderCharacter
import pathfinder.domain.character.stats.Skill
import pathfinder.domain.kingdom.Kingdom
import pathfinder.domain.kingdom.KingdomScore

@MappedSuperclass
sealed class LeadershipRole: Cloneable {
    @ManyToOne
    var character: PathfinderCharacter? = null
    @get:Transient
    abstract val roleName: RoleName
    @get:Transient
    abstract val scoreBonus: Int?
    @get:Transient
    abstract val skill: Skill?
    @get:Transient
    val skillBonus
        get() = skill?.takeIf { character?.campaign?.optionalRules?.leadershipRoleSkills == true }?.ranks?.div(5) ?: 0
    @get:Transient
    val leadershipBonus
        get() = if (character?.leadershipFeat == true) 1 else 0
    @get:Transient
    val bonus
        get() = scoreBonus?.plus(skillBonus)?.plus(leadershipBonus)?.takeIf { performedDuty }
    var performedDuty: Boolean = false
    var forcedVacancy: Boolean = false

    @Embeddable
    open class Ruler: LeadershipRole() {

        @Transient
        override val roleName = RoleName.RULER

        @get:Transient
        override val skill
            get() = character?.skills?.knowledgeNobility

        @get:Transient
        override val scoreBonus
            get() = character?.abilityScores?.charisma?.modifier ?: 0

        fun economyBonus(duties: RulerDuties) = bonus.takeIf { duties.selected(KingdomScore.ECONOMY) }
            ?: 0

        fun loyaltyBonus(duties: RulerDuties) = bonus.takeIf { duties.selected(KingdomScore.LOYALTY) }
            ?: 0

        fun stabilityBonus(duties: RulerDuties) = bonus.takeIf { duties.selected(KingdomScore.STABILITY) }
            ?: 0
    }

    @Embeddable
    class Consort: LeadershipRole() {

        @Transient
        override val roleName = RoleName.CONSORT
        @Column(name = "consort_co_ruler")
        var coRuler: Boolean = false

        @get:Transient
        override val skill
            get() = character?.skills?.knowledgeNobility

        @get:Transient
        override val scoreBonus
            get() = if (coRuler) character?.abilityScores?.charisma?.modifier
                else character?.abilityScores?.charisma?.modifier?.div(2)

        fun economyBonus(duties: RulerDuties) = bonus.takeIf {
            coRuler && duties.selected(KingdomScore.ECONOMY)
        } ?: 0

        fun loyaltyBonus(duties: RulerDuties) = bonus.takeIf {
            !coRuler || duties.selected(KingdomScore.LOYALTY)
        } ?: 0

        fun stabilityBonus(duties: RulerDuties) = bonus.takeIf {
            coRuler && duties.selected(KingdomScore.STABILITY)
        } ?: 0

    }

    @Embeddable
    class Heir: LeadershipRole() {
        @Transient
        override val roleName = RoleName.HEIR


        @get:Transient
        override val skill
            get() = character?.skills?.knowledgeNobility

        @get:Transient
        override val scoreBonus
            get() = character?.abilityScores?.charisma?.modifier?.div(2)

        @get:Transient
        val loyaltyBonus
            get() = bonus ?: 0
    }
    @Embeddable
    class Councillor: LeadershipRole() {

        @Transient
        override val roleName = RoleName.COUNCILLOR

        @get:Transient
        override val skill
            get() = character?.skills?.knowledgeLocal

        @get:Transient
        override val scoreBonus
            get() = character?.abilityScores?.run {
                maxOf(charisma.modifier, wisdom.modifier)
            }
        @get:Transient
        val loyaltyBonus
            get() = bonus ?: -2
    }
    @Embeddable
    class General: LeadershipRole() {
        @Transient
        override val roleName = RoleName.GENERAL


        @get:Transient
        override val skill
            get() = character?.skills?.profession?.firstOrNull { it.type.equals("Soldier", ignoreCase = true) }

        @get:Transient
        override val scoreBonus
            get() = character?.abilityScores?.run {
                maxOf(charisma.modifier, strength.modifier)
            }

        @get:Transient
        val stabilityBonus
            get() = bonus ?: 0

        @get:Transient
        val loyaltyBonus
            get() = if(!performedDuty || character == null) -4 else 0
    }
    @Embeddable
    class GrandDiplomat: LeadershipRole() {

        @Transient
        override val roleName = RoleName.GRAND_DIPLOMAT

        @get:Transient
        override val skill
            get() = character?.skills?.diplomacy

        @get:Transient
        override val scoreBonus
            get() = character?.abilityScores?.run {
                maxOf(charisma.modifier, intelligence.modifier)
            }

        @get:Transient
        val stabilityBonus
            get() = bonus ?: -2
    }
    @Embeddable
    class HighPriest: LeadershipRole() {

        @Transient
        override val roleName = RoleName.HIGH_PRIEST

        @get:Transient
        override val skill
            get() = character?.skills?.knowledgeReligion

        @get:Transient
        override val scoreBonus
            get() = character?.abilityScores?.run {
                maxOf(charisma.modifier, wisdom.modifier)
            }

        @get:Transient
        val stabilityBonus
            get() = bonus ?: -2

        @get:Transient
        val loyaltyBonus
            get() = if(!performedDuty || character == null) -2 else 0
    }
    @Embeddable
    class Magister: LeadershipRole() {

        @Transient
        override val roleName = RoleName.MAGISTER

        @get:Transient
        override val skill
            get() = character?.skills?.knowledgeArcana

        @get:Transient
        override val scoreBonus
            get() = character?.abilityScores?.run {
                maxOf(charisma.modifier, intelligence.modifier)
            }

        @get:Transient
        val economyBonus
            get() = bonus ?: -4
    }
    @Embeddable
    class Marshal: LeadershipRole() {

        @Transient
        override val roleName = RoleName.MARSHAL

        @get:Transient
        override val skill
            get() = character?.skills?.survival

        @get:Transient
        override val scoreBonus
            get() = character?.abilityScores?.run {
                maxOf(dexterity.modifier, wisdom.modifier)
            }

        @get:Transient
        val economyBonus
            get() = bonus ?: -4
    }
    @Embeddable
    class RoyalEnforcer: LeadershipRole() {

        @Transient
        override val roleName = RoleName.ROYAL_ENFORCER


        @get:Transient
        override val skill
            get() = character?.skills?.intimidate

        @get:Transient
        override val scoreBonus
            get() = character?.abilityScores?.run {
                maxOf(dexterity.modifier, strength.modifier)
            }

        @get:Transient
        val loyaltyBonus
            get() = bonus ?: 0
    }
    @Embeddable
    class Spymaster: LeadershipRole() {

        @Transient
        override val roleName = RoleName.SPYMASTER

        @Column(name = "spymaster_score")
        var score: KingdomScore? = null

        @get:Transient
        override val skill
            get() = character?.skills?.senseMotive

        @get:Transient
        override val scoreBonus
            get() = character?.abilityScores?.run {
                maxOf(dexterity.modifier, intelligence.modifier)
            }

        @get:Transient
        val economyBonus
            get() = if (!performedDuty || character == null) -4
        else bonus.takeIf {
            score == KingdomScore.ECONOMY
        } ?: 0

        @get:Transient
        val loyaltyBonus
            get() = bonus.takeIf {
                score == KingdomScore.LOYALTY
            } ?: 0

        @get:Transient
        val stabilityBonus
            get() = bonus.takeIf {
                score == KingdomScore.STABILITY
            } ?: 0
    }
    @Embeddable
    class Treasurer: LeadershipRole() {

        @Transient
        override val roleName = RoleName.TREASURER

        @get:Transient
        override val skill
            get() = character?.skills?.profession?.firstOrNull { it.type.equals("Merchant", ignoreCase = true) }

        @get:Transient
        override val scoreBonus
            get() = character?.abilityScores?.run {
                maxOf(intelligence.modifier, wisdom.modifier)
            }

        @get:Transient
        val economyBonus
            get() = bonus ?: -4
    }
    @Embeddable
    class Warden: LeadershipRole() {

        @Transient
        override val roleName = RoleName.WARDEN

        @get:Transient
        override val skill
            get() = character?.skills?.knowledgeEngineering

        @get:Transient
        override val scoreBonus
            get() = character?.abilityScores?.run {
                maxOf(constitution.modifier, strength.modifier)
            }

        @get:Transient
        val loyalty
            get() = bonus ?: -2

        @get:Transient
        val stabilityBonus
            get() = if(performedDuty) 0 else -2
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
        @OneToOne(fetch = FetchType.LAZY)
        val videroyOf: Kingdom
    ): LeadershipRole() {
        @Transient
        override val roleName = RoleName.VICEROY

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0

        @get:Transient
        override val skill
            get() = character?.skills?.knowledgeGeography

        @get:Transient
        override val scoreBonus
            get() = character?.abilityScores?.run {
                maxOf(intelligence.modifier, wisdom.modifier)
            }

        @get:Transient
        val economyBonus
            get() = bonus ?: 0
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
