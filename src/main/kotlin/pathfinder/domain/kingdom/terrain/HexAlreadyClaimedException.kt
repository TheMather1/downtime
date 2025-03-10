package pathfinder.domain.kingdom.terrain

class HexAlreadyClaimedException(vararg val hex: Hex): IllegalStateException("${hex.map(Hex::coordinate)} has already been claimed!")