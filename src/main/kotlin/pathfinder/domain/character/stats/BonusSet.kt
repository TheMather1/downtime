package pathfinder.domain.character.stats

class BonusSet(
    val bonuses: MutableList<Bonus<BonusType>> = mutableListOf()
): MutableList<Bonus<BonusType>> by bonuses {

    val map
        get() = bonuses.groupBy(Bonus<BonusType>::type)

    inline fun <reified T: BonusType>get(): List<Bonus<T>> = bonuses.filter { it.type is T } as List<Bonus<T>>

    inline fun <reified T: BonusType>sum() = get<T>().sum<T>()
    fun sum() = map.map { (_, value) ->
        value.sum()
    }.sumOf { it.value }

    inline fun <reified T: BonusType>List<Bonus<T>>.sum() = this.reduce { acc, bonus -> acc + bonus }
}
