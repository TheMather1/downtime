package pathfinder.domain.kingdom.terrain.improvements

import pathfinder.domain.kingdom.terrain.Hex
import pathfinder.domain.kingdom.terrain.TerrainType.*
import pathfinder.domain.kingdom.terrain.features.TerrainFeature

enum class Improvement(
    internal val economyBonus: Int = 0,
    internal val loyaltyBonus: Int = 0,
    internal val stabilityBonus: Int = 0,
    internal val earningsBonus: Int = 0,
    internal val consumptionBonus: Int = 0
) {
    AQUEDUCT(
        loyaltyBonus = 1,
        stabilityBonus = 1
    ) {
        override fun cost(hex: Hex) = hex.terrain.roadCost ?: throw RuntimeException()

        override fun eligible(hex: Hex) = when (hex.terrain) {
            WATER -> false
            HILL, MOUNTAIN -> true
            else -> hex.neighbors.any { AQUEDUCT in it.improvements }
        }
    },
    CANAL {
        private val eligibleTerrains = setOf(DESERT, HILL, PLAIN)
        override fun cost(hex: Hex) = (hex.terrain.roadCost ?: throw RuntimeException()) * 2

        override fun eligible(hex: Hex) = hex.run {
            TerrainFeature.RIVER !in hex.terrainFeatures && terrain in eligibleTerrains
        }
    },
    FARM(consumptionBonus = -2) {
        override fun cost(hex: Hex) = hex.terrain.farmCost ?: throw RuntimeException()
        override fun consumptionBonus(hex: Hex) = if(TerrainFeature.RESOURCE in hex.terrainFeatures) consumptionBonus - 1 else consumptionBonus

        override fun eligible(hex: Hex) = when (hex.terrain) {
            HILL, PLAIN -> hex.coastal || hex.hasWater || hex.neighbors.any { it.hasWater } || hex.neighbors.count { FARM in it.improvements } >= 2
            DESERT -> hex.coastal || hex.hasWater
            else -> false
        }

    },
    FISHERY(consumptionBonus = -1) {
        private val eligbleTerrains = setOf(WATER, COASTLINE, MARSH)
        override fun consumptionBonus(hex: Hex) = if(TerrainFeature.RESOURCE in hex.terrainFeatures) consumptionBonus - 1 else consumptionBonus
        override fun cost(hex: Hex) = 4

        override fun eligible(hex: Hex) = hex.terrain in eligbleTerrains
                || hex.hasWater
    },
    FORT(stabilityBonus = 2) {
        override fun cost(hex: Hex) = 24

        override fun eligible(hex: Hex) = hex.rawTerrain != WATER
    },
    HIGHWAY {
        override fun cost(hex: Hex) = hex.terrain.roadCost?.times(2) ?: throw RuntimeException()

        override fun eligible(hex: Hex) = hex.owner?.let {
            it.size >= 26 && ROAD.eligible(hex)
        } == true

    },
    MINE(
        economyBonus = 1,
        earningsBonus = 1
    ) {
        private val eligibleTerrains = setOf(CAVERN, DESERT, HILL, MOUNTAIN)
        override fun economyBonus(hex: Hex) = if(TerrainFeature.RESOURCE in hex.terrainFeatures) economyBonus + 1 else economyBonus
        override fun earningsBonus(hex: Hex) = if(TerrainFeature.RESOURCE in hex.terrainFeatures) earningsBonus + 1 else earningsBonus
        override fun cost(hex: Hex) = 6

        override fun eligible(hex: Hex) = hex.terrain in eligibleTerrains
    },
    QUARRY(
        stabilityBonus = 1,
        earningsBonus = 1
    ) {
        private val eligibleTerrains = setOf(CAVERN, HILL, MOUNTAIN)
        override fun stabilityBonus(hex: Hex) = if(TerrainFeature.RESOURCE in hex.terrainFeatures) stabilityBonus + 1 else stabilityBonus
        override fun earningsBonus(hex: Hex) = if(TerrainFeature.RESOURCE in hex.terrainFeatures) earningsBonus + 1 else earningsBonus
        override fun cost(hex: Hex) = 6

        override fun eligible(hex: Hex) = hex.terrain in eligibleTerrains
    },
    ROAD {
        override fun cost(hex: Hex) = hex.terrain.roadCost ?: throw RuntimeException()

        override fun eligible(hex: Hex) = hex.terrain != WATER
    },
    SAWMILL(
        stabilityBonus = 1,
        earningsBonus = 1
    ) {
        private val eligibleTerrains = setOf(FOREST, JUNGLE)
        override fun stabilityBonus(hex: Hex) = if(TerrainFeature.RESOURCE in hex.terrainFeatures) stabilityBonus + 1 else stabilityBonus
        override fun earningsBonus(hex: Hex) = if(TerrainFeature.RESOURCE in hex.terrainFeatures) earningsBonus + 1 else earningsBonus
        override fun cost(hex: Hex) = 3

        override fun eligible(hex: Hex) = hex.terrain in eligibleTerrains
    },
    WATCHTOWER(stabilityBonus = 1) {
        override fun cost(hex: Hex) = 12

        override fun eligible(hex: Hex) = hex.rawTerrain != WATER
    };

    open fun economyBonus(hex: Hex) = economyBonus
    open fun loyaltyBonus(hex: Hex) = loyaltyBonus
    open fun stabilityBonus(hex: Hex) = stabilityBonus
    open fun earningsBonus(hex: Hex) = earningsBonus
    open fun consumptionBonus(hex: Hex) = consumptionBonus

    abstract fun cost(hex: Hex): Int
    abstract fun eligible(hex: Hex): Boolean
}