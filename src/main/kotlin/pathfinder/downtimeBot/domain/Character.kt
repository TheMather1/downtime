package pathfinder.downtimeBot.domain

class Character(val name: String, val playerId: Long?, var funds: Double = 0.0, val capital: MutableMap<Capital, Int> = mutableMapOf())
