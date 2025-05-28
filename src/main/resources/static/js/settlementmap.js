const
    lotDiameter = 100,
    streetBreadth = 50,
    borderBreadth = 50,
    districtDiameter = (lotDiameter * 6) + (streetBreadth * 2);

let campaign, map;

$.fn.addMap = function(campaignId, flatTop, offsetX, offsetY, mapData) {
    campaign = campaignId;
    horizontal = flatTop;
    $(this).appendSvgAttr('svg', {
        id: 'svgMap',
        tabindex: 1,
        height: '100%',
        width: '100%'
    })
        .appendSvg('defs')
            .stylize()
            .decorate()
        .parent()
        .appendSvgAttr('g', {
            id: '#settlementContainer',
            transform: `translate(${offsetX * circumradius * 2} ${offsetY * inradius * 2})`
        })
        .populateMap(mapData);

    svgPanZoom('#svgMap', {
        dblClickZoomEnabled: false,
        minZoom: 0.1
    });

    $(document).on('keydown', function(event) {
        if (event.key === 'Escape') {
            $('#mapSidebar').hide();
            $('polygon.active').removeClass('active')
        }
    });
}

$.fn.renderStreets = function() {
    for (let i = 1; i < 3; i++) {
        $(this).appendSvgAttr('rect', {
            class: 'street horizontal',
            x: 0,
            y: i * (lotDiameter * 2 + streetBreadth) - streetBreadth,
            width: districtDiameter,
            height: streetBreadth
        });

        $(this).appendSvgAttr('rect', {
            class: 'street vertical',
            x: i * (lotDiameter * 2 + streetBreadth) - streetBreadth,
            y: 0,
            width: streetBreadth,
            height: districtDiameter
        });
    }
    return $(this);
}

$.fn.renderEmptyLot = function (lotCoord) {
    const [lx, ly] = lotCoord.split(',').map(Number),
        lotX = (lx * (lotDiameter * 2 + streetBreadth)) + (lx % 2 * lotDiameter),
        lotY = (ly * (lotDiameter * 2 + streetBreadth)) + (ly % 2 * lotDiameter);
    return $(this).appendSvgAttr('rect', {
        class: 'lot',
        x: lotX,
        y: lotY,
        width: lotDiameter,
        height: lotDiameter
    });
}

function getBuildingBounds(lotCoords) {
    // Get the extent of a building across lots
    const minX = Math.min(...lotCoords.map(c => c.x)),
        maxX = Math.max(...lotCoords.map(c => c.x)),
        minY = Math.min(...lotCoords.map(c => c.y)),
        maxY = Math.max(...lotCoords.map(c => c.y)),

        // Calculate grid positions
        startGridX = Math.floor(minX / 2),
        endGridX = Math.floor(maxX / 2),
        startGridY = Math.floor(minY / 2),
        endGridY = Math.floor(maxY / 2);

    // Calculate actual positions including streets
    return {
        x: (startGridX * (lotDiameter * 2 + streetBreadth)) + (minX % 2 * lotDiameter),
        y: (startGridY * (lotDiameter * 2 + streetBreadth)) + (minY % 2 * lotDiameter),
        width: (maxX - minX + 1) * lotDiameter +
            (endGridX > startGridX ? (endGridX - startGridX) * streetBreadth : 0),
        height: (maxY - minY + 1) * lotDiameter +
            (endGridY > startGridY ? (endGridY - startGridY) * streetBreadth : 0)
    };
}

$.fn.renderLots = function (lots) {
    const buildings = new Map();
    Object.entries(lots).map(([lotCoord, lotInfo]) => {
        if (lotInfo.building) {
            const buildingId = lotInfo.building.id;
            if (!buildings.has(buildingId)) {
                buildings.set(buildingId, {
                    type: lotInfo.building.type,
                    lots: []
                });
            }
            const [lx, ly] = lotCoord.split(',').map(Number);
            buildings.get(buildingId).lots.push({x: lx, y: ly});
        } else {
            $(this).renderEmptyLot(lotCoord);
        }
    });
    buildings.forEach((building, id) => {
        const bounds = getBuildingBounds(building.lots);
        $(this).appendSvgAttr('rect', {
            class: `lot building ${building.type.toLowerCase()}`,
            x: bounds.x,
            y: bounds.y,
            width: bounds.width,
            height: bounds.height,
            'data-building-id': id
        });
    });
    return $(this);
}

function getDistrictCoordinates(coord) {
    const [x, y] = coord.split(',').map(Number);
    return [
        (x * (districtDiameter + borderBreadth)),
        (y * (districtDiameter + borderBreadth))
    ];
}

$.fn.renderBorders = function (borders) {
    Object.entries(borders).forEach(([locationStr, border]) => {
        const [coordStr, orientation] = locationStr.split('|'),
            [x, y] = coordStr.split(',').map(Number),
            isVerticalBorder = orientation === 'VERTICAL',

            borderX = x * (districtDiameter + borderBreadth),
            borderY = y * (districtDiameter + borderBreadth);

        $(this).appendSvgAttr('line', {
            class: `border border-${border.type.toLowerCase()}`,
            'stroke-width': borderBreadth,
            x1: isVerticalBorder ? borderX + districtDiameter : borderX,
            y1: isVerticalBorder ? borderY : borderY + districtDiameter,
            x2: isVerticalBorder ? borderX + districtDiameter : borderX + districtDiameter,
            y2: isVerticalBorder ? borderY + districtDiameter : borderY + districtDiameter
        });
    });
}

$.fn.renderDistricts = function (districts) {
    Object.entries(districts).map(([coord, district]) => {
        const [districtX, districtY] = getDistrictCoordinates(coord);

        $(this).appendSvgAttr('g', {
            class: 'district',
            transform: `translate(${districtX},${districtY})`
        }).renderStreets()
            .renderLots(district.lots);
    });
    return $(this);
}

$.fn.populateMap = function(settlementMap) {
    $(this).appendSvgAttr('g', { class: 'districts-container' })
        .renderDistricts(settlementMap.districts)
        .renderBorders(settlementMap.borders);
    return $(this);
}

function polulateForm(hexData) {
    $('#hexForm').show();
    $('.hexQ').val(hexData.q);
    $('.hexR').val(hexData.r);
    $('#hexTerrain').val(hexData.terrainType);

    $('#terrainFeatures li, #terrainFeatures input, #terrainFeatures label').prop('checked', false).prop('disabled', true).prop('hidden', true);
    hexData.eligibleFeatures.forEach(feature => {
        $(`#${feature}, li:has(#${feature}), label[for=${feature}]`).prop('disabled', false).prop('hidden', false);
    });
    hexData.features.forEach(feature => {
        $(`#${feature}`).prop('checked', true);
    });

    $('#terrainImprovements li, #terrainImprovements input, #terrainImprovements label').prop('checked', false).prop('disabled', true).prop('hidden', true);
    $('#terrainImprovements > select').val('').prop('disabled', true).prop('hidden', true);
    $('#mainImprovement > option').prop('disabled', true).prop('hidden', true);
    hexData.eligibleImprovements.forEach( improvement => {
        if (improvement === 'FARM' || improvement === 'MINE' || improvement === 'QUARRY' || improvement === 'SAWMILL') {
            $(`#mainImprovement, #${improvement}, #mainImprovement > option[value=""], label[for="mainImprovement"]`).prop('disabled', false).prop('hidden', false);
        } else if (improvement === 'ROAD' || improvement === 'HIGHWAY') {
            $('#road, label[for="road"]').prop('disabled', false).prop('hidden', false);
        } else {
            $(`#${improvement}, li:has(#${improvement}), label[for=${improvement}]`).prop('disabled', false).prop('hidden', false);
        }
    })
    hexData.improvements.forEach(improvement => {
        if (improvement === 'FARM' || improvement === 'MINE' || improvement === 'QUARRY' || improvement === 'SAWMILL') {
            $('#mainImprovement').val(improvement);
        } else if (improvement === 'ROAD' || improvement === 'HIGHWAY') {
            $('#road').val(improvement);
        } else {
            $(`#${improvement}`).prop('checked', true);
        }
    });

    $('#visibleText').text(hexData.visibleText);
    $('#hiddenText').text(hexData.hiddenText);
}

$.fn.setHexData = function(hexData) {
    if (hexData.terrainType == null) {
        return $(this).attr('class', 'newHex');
    } else {
        return $(this).attr('class', 'mapHex')
            .attr('terrain', hexData.terrainType)
            .setRoads(hexData);
    }
}

$.fn.setRoads = function (hexData) {
    if (hexData.roadDirections !== 0) {
        $(this).attr('filter', `url(#road${hexData.roadDirections})`)
    }
    if (hexData.improvements.includes('HIGHWAY')) {
        $(this).attr('roadType', 'HIGHWAY');
    } else if (hexData.improvements.includes('ROAD')) {
        $(this).attr('roadType', 'ROAD');
    }
    return $(this);
}

function createSvg(tag) {
    return $(document.createElementNS('http://www.w3.org/2000/svg', tag || 'svg'));
}

function createSvgAttr(tag, attr) {
    return createSvg(tag).attr(attr);
}

function createPattern(id, url) {
    return createSvgAttr('pattern', {
        id: id,
        height: '100%',
        width: '100%',
        overflow: 'visible'
    })
        .appendSvgAttr('image', {
            height: horizontal ? inradius * 2 + 6 : circumradius * 2 + 2,
            width: horizontal ? circumradius * 2 + 2 : inradius * 2 + 6 ,
            x: horizontal ? -1 : -2.5,
            y: horizontal ? -2.5 : -1,
            href: url
        })
        .parent();
}

function createFilter(type, index) {
    return createSvgAttr('filter', {
        id: `${type.toLowerCase()}${index}`
    })
        .appendSvgAttr('feImage', {
            href: `/img/${type}/${type}${index}.png`,
            result: 'overlay'
        })
        .parent()
        .appendSvgAttr('feComposite', {
            operator: 'atop',
            in: 'overlay',
            in2: 'SourceGraphic'
        })
        .parent();
}

$.fn.appendSvg = function(tag) {
    return createSvg(tag).appendTo($(this));
}
$.fn.appendSvgAttr = function(tag, attr) {
    return $(this).appendSvg(tag).attr(attr);
}

$.fn.setId = function(id) {
    return $(this).attr('id', id);
}

$.fn.appendSvgWithId = function(tag, id) {
    return $(this).appendSvg(tag).setId(id);
}
