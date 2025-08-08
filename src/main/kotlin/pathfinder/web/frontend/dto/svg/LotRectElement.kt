package pathfinder.web.frontend.dto.svg

class LotRectElement(
    x: Double,
    y: Double,
    width: Double,
    height: Double,
    districtId: Long,
    val lotX: Int,
    val lotY: Int,
    val lotId: Long
): DistrictRectElement(x, y, width, height, districtId) {
    override fun toString() = "{lotId: $lotId, districtId: $districtId, x: $lotX, y: $lotY}"
}
