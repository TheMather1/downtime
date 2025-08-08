package pathfinder.web.frontend.dto.svg

open class TerrainRectElement(
    x: Double,
    y: Double,
    width: Double,
    height: Double,
    val coordX: Int,
    val coordY: Int,
    val terrainType: String
): SvgRectElement(x, y, width, height) {
    val coordinateString = """{"x": "$coordX", "y": "$coordY"}"""
}
