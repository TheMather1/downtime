package pathfinder.web.backend

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import pathfinder.domain.kingdom.settlement.District
import pathfinder.domain.kingdom.settlement.DistrictBorder
import pathfinder.domain.kingdom.settlement.HorizontalBorder
import pathfinder.domain.kingdom.settlement.Settlement
import pathfinder.domain.kingdom.settlement.VerticalBorder
import pathfinder.domain.kingdom.settlement.buildings.Lot
import pathfinder.domain.kingdom.settlement.buildings.LotBuilding
import pathfinder.domain.kingdom.settlement.buildings.LotBuildingType
import pathfinder.domain.support.coordinate.Coordinate
import pathfinder.domain.support.direction.Cardinal
import pathfinder.repository.KingdomMapRepository

@Controller
@RequestMapping("/api/settlement")
class SettlementBackendController(
    private val kingdomMapRepository: KingdomMapRepository
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ModelAttribute
    fun addSettlement(
        @PathVariable settlement: Settlement
    ) = settlement

    @ModelAttribute
    fun addCampaign(
        @PathVariable settlement: Settlement
    ) = settlement.hex.map.campaign

    @GetMapping("/{settlement}/building")
    @Transactional
    fun getBuildingOptions(
        @PathVariable("settlement") settlement: Settlement,
        @RequestParam("lot") lots: Set<Lot>,
        model: Model
    ): String {
        lots.assertValidSize()
        val buildings = lots.mapNotNull { it.building }
        assert(buildings.size <= 1)
        val options = LotBuildingType.entries.filter {
            it.eligible(lots)
        }
        model.addAttribute("building", buildings.firstOrNull()?.type)
        model.addAttribute("buildingOptions", options)
        model.addAttribute("settlement", settlement)
        model.addAttribute("lots", lots)
        return "settlements/buildingForm"
    }

    @PostMapping("/{settlement}/building")
    @Transactional
    fun setBuilding(
        @PathVariable("settlement") settlement: Settlement,
        @RequestParam("lot") lots: Set<Lot>,
        @RequestParam("buildingType", required = false) buildingType: LotBuildingType?
    ): String {
        lots.assertValidSize()
        assert(buildingType?.validPosition(lots) ?: true)
        val building = buildingType?.let { LotBuilding(it) }
        lots.forEach { it.building = building }
        return "redirect:/settlement/${settlement.id}/map"
    }

    @GetMapping("/{settlement}/lot/{lot}")
    @Transactional
    fun getLot(
        @PathVariable("settlement") settlement: Settlement,
        @PathVariable("lot") @ModelAttribute lot: Lot
    ): String {
        assert(lot.building != null)
        return "settlements/lotForm"
    }

    @PostMapping("/{settlement}/lot/{lot}")
    @Transactional
    fun setLotUpgrades(
        @PathVariable("settlement") settlement: Settlement,
        @PathVariable("lot") @ModelAttribute lot: Lot,
        @RequestParam("bridge", required = false, defaultValue = "false") bridge: Boolean,
        @RequestParam("cistern", required = false, defaultValue = "false") cistern: Boolean,
        @RequestParam("everflowingSprings", required = false, defaultValue = "false") everflowingSprings: Boolean,
        @RequestParam("streetlamps", required = false, defaultValue = "false") streetlamps: Boolean,
    ): String {
        assert(lot.building != null)
        lot.bridge = bridge
        lot.cistern = cistern
        lot.everflowingSpring = everflowingSprings
        lot.streetlamps = streetlamps
        return "redirect:/settlement/${settlement.id}/map"
    }

    @GetMapping("/{settlement}/border/{border}")
    @Transactional
    fun getBorder(
        @PathVariable("border") @ModelAttribute border: DistrictBorder
    ): String {
        return "/settlements/borderForm"
    }

    @PostMapping("/{settlement}/border/{border}")
    @Transactional
    fun setBorder(
        @PathVariable("settlement") settlement: Settlement,
        @PathVariable("border") border: DistrictBorder,
        @RequestParam("cityWall", required = false, defaultValue = "false") cityWall: Boolean,
        @RequestParam("moat", required = false, defaultValue = "false") moat: Boolean,
        @RequestParam("streetlamps", required = false, defaultValue = "false") streetlamps: Boolean,
        @RequestParam("facing", required = false) facing: Cardinal?,
        @RequestParam("type", required = false, defaultValue = "LAND") type: DistrictBorder.BorderType
    ): String {
        border.cityWall = cityWall
        border.moat = moat
        border.watergate = cityWall && if (border is VerticalBorder) {
            border.east?.waterway(Cardinal.WEST) ?: false || border.west?.waterway(Cardinal.EAST) ?: false
        } else {
            border as HorizontalBorder
            border.north?.waterway(Cardinal.SOUTH) ?: false || border.south?.waterway(Cardinal.NORTH) ?: false
        }
        border.streetlamps = streetlamps
        if (facing != null) {
            if(border.facing != facing) border.flip()
        } else {
            border.type = type
            val faces = when (border.facing) {
                Cardinal.NORTH -> (border as HorizontalBorder).south!!.coordinate.north
                Cardinal.EAST -> (border as VerticalBorder).west!!.coordinate.east
                Cardinal.SOUTH -> (border as HorizontalBorder).north!!.coordinate.south
                Cardinal.WEST -> (border as VerticalBorder).east!!.coordinate.west
            }
            if (border.facing != Cardinal.NORTH) settlement.districts[faces.south]?.northBorder?.type = type
            if (border.facing != Cardinal.EAST) settlement.districts[faces.west]?.eastBorder?.type = type
            if (border.facing != Cardinal.SOUTH) settlement.districts[faces.north]?.southBorder?.type = type
            if (border.facing != Cardinal.WEST) settlement.districts[faces.east]?.westBorder?.type = type
        }
        return "redirect:/settlement/${settlement.id}/map"
    }

    @PostMapping("/{settlement}/district/")
    @Transactional
    fun newDistrict(
        @PathVariable("settlement") settlement: Settlement,
        @RequestParam("x") x: Int,
        @RequestParam("y") y: Int,
    ): String {
        assert(settlement.districts.none { it.key.x == x && it.key.y == y })
        assert(settlement.districts.any {
            it.key.x == x && (it.key.y == y-1 || it.key.y == y+1) ||
                    it.key.y == y && (it.key.x == x-1 || it.key.x == x+1)
        })
        val coordinate = Coordinate(x, y)
        val district = settlement.addDistrict(coordinate)
        return "redirect:/settlement/${district.settlement.id}/map"
    }

    @GetMapping("/{settlement}/district/{district}")
    @Transactional
    fun getDistrict(
        @PathVariable("district") @ModelAttribute district: District
    ): String {
        return "/settlements/districtForm"
    }

    @PostMapping("/{settlement}/district/{district}")
    @Transactional
    fun getDistrict(
        @PathVariable("district") district: District,
        @RequestParam("paved", required = false, defaultValue = "false") paved: Boolean,
        @RequestParam("sewer", required = false, defaultValue = "false") sewer: Boolean,
        @RequestParam("streetlamps", required = false, defaultValue = "false") streetlamps: Boolean,
    ): String {
        district.paved = paved
        district.sewer = sewer
        district.streetlamps = streetlamps
        return "redirect:/settlement/${district.settlement.id}/map"
    }

    fun Set<Lot>.assertValidSize() {
        assert(size == 1 || size == 2 || size == 4)
        assert(all { it.district == first().district })
        assert(maxOf { it.coordinate.x } - minOf { it.coordinate.x } in 0..1)
        assert(maxOf { it.coordinate.y } - minOf { it.coordinate.y } in 0..1)
    }

    fun <E> MutableSet<E>.replaceAll(values: Set<E>) {
        removeAll { it !in values }
        addAll(values)
    }
}
