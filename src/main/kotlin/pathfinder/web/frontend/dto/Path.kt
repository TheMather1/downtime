package pathfinder.web.frontend.dto

data class Path(val x: Int, val y: Int, val childNodes: MutableSet<Path> = mutableSetOf()) {
    val length: Int
        get() = (childNodes.maxOfOrNull { it.length } ?: 0) + 1

    fun forEach(action: (Path) -> Unit) {
        action(this)
        childNodes.forEach { it.forEach(action) }
    }
}
