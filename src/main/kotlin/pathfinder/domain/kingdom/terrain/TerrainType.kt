package pathfinder.domain.kingdom.terrain

enum class TerrainType(
    val explorationDays: Int,
    val preparationMonths: Int?,
    val preparationCost: Int?,
    val farmCost: Int?,
    val roadCost: Int?,
    val movementMultiplier: Double,
    val roadMovement: Double,
    val highwayMovement: Double,
) {
    CAVERN(3, 3, 8, null, 4, 1.0, 1.0, 1.0),
    COASTLINE(-1, -1, -1, -1, -1, -1.0, -1.0, -1.0),
    DESERT(2, 1, 4, 8, 4, 0.5, 0.5, 1.0),
    FOREST(2, 2, 4, null, 2, 0.5, 1.0, 1.0),
    HILL(1, 1, 2, 4, 3, 0.5, 0.75, 1.0),
    JUNGLE(2, 4, 12, null, 4, 0.25, 0.75, 1.0),
    MARSH(3, 3, 8, null, 4, 0.5, 0.75, 1.0),
    MOUNTAIN(3, 4, 12, null, 4, 0.5, 0.75, 0.75),
    PLAIN(1, 0, 1, 2, 1, 0.75, 1.0, 1.0),
    WATER(2, null, null, null, null, 1.0, 1.0, 1.0);
}
