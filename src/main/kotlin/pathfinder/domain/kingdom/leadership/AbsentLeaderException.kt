package pathfinder.domain.kingdom.leadership

class AbsentLeaderException(message: String, val leader: LeadershipRole) : IllegalStateException(message)