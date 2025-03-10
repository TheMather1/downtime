package pathfinder.domain.downtime

abstract class DowntimeEntity(
    val name: String,
    val create: Map<Capital, Int>,
    val earnings: Set<Capital>,
    open val bonus: Int,
    val time: Int
) {
    val value = create.map { (capital, count) -> capital.value * count }.sum()
}
