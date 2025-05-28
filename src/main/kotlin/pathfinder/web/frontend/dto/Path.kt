package pathfinder.web.frontend.dto

data class Path(val x: Int, val y: Int, val childNodes: MutableSet<Path> = mutableSetOf(), val ephemeral: Boolean = false) {
    val length: Int
        get() = (childNodes.maxOfOrNull { it.length } ?: 0) + 1

    fun forEach(action: (Path) -> Unit) {
        action(this)
        childNodes.forEach { it.forEach(action) }
    }

    fun find(x: Int, y: Int): Path? = if (x == this.x && y == this.y) this else childNodes.filterNot { it.ephemeral }.firstNotNullOfOrNull { it.find(x, y) }
    fun find(predicate: (Path) -> Boolean): Path? = if (predicate(this)) this else childNodes.filterNot { it.ephemeral }.firstNotNullOfOrNull { it.find(predicate) }
}
