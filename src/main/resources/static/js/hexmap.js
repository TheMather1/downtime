const
    circumradius = 50,
    halfCircumradius = circumradius / 2,
    inradius = Math.round(0.8660254 * circumradius),
    riverWidth = 0.25,
    roadWidth = 0.15;

let campaign, horizontal;

$.fn.addMap = function(campaignId, flatTop, offsetX, offsetY, mapData, rivers, roads) {
    campaign = campaignId;
    horizontal = flatTop;
    $(this).appendSvgAttr('svg', {
        id: 'svgMap',
        tabindex: 1,
        height: '100%',
        width: '100%'
    })
        .addDefs()
        .drawHexes(mapData)
        .drawRivers(rivers, mapData)
        .drawRoads(roads);

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

$.fn.drawHexes = function(mapData) {
    // Create hex container
    $(this).appendSvgAttr('g', {
        id: 'hexContainer',
        transform: `translate(${offsetX * circumradius * 2} ${offsetY * inradius * 2})`
    }).append(
        Object.entries(mapData).map(([coordinate, hexData]) =>
            createHex(coordinate.split(':'), hexData)
        )
    );
    return $(this);
};

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
            .attr('terrain', hexData.terrainType);
    }
}

$.fn.addDefs = function () {
    $(this).appendSvg('defs')
        .stylize();
    return $(this);
}

$.fn.stylize = function() {
    return $(this).append([
        createPattern('plain', '/img/ClearSpeckled/ClearSpeckled001.png'),
        createPattern('forest', '/img/Bushes/Bushes005.png'),
        createPattern('jungle', '/img/LightJungle/LightJungle005.png'),
        createPattern('mountain', '/img/TanMountains/TanMountains004.png'),
        createPattern('marsh', '/img/Bog/Bog001.png'),
        createPattern('coastline', '/img/Sand/Sand003.png'),
        createPattern('desert', '/img/Desert/Desert003.png'),
        createPattern('hill', '/img/GreenHills/GreenHills001.png'),
        createPattern('water', '/img/Ocean/Ocean005.png')
    ]);
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

function point(x, y, centerX, centerY) {
    return Math.round(x + centerX) + ',' + Math.round(y + centerY)
}

function getHexCenter(x, y) {
    if (horizontal) {
        return {
            x: x * 1.5 * circumradius,
            y: (y * 2 + (x & 1)) * inradius
        } 
    } else return {
        x: (x * 2 + (y & 1)) * inradius,
        y: y * 1.5 * circumradius
    }
}

function getPointsHorizontal(coordinate) {
    let center = getHexCenter(coordinate[0], coordinate[1]);
    return [
        point(0 - halfCircumradius, 0 - inradius, center.x, center.y),
        point(halfCircumradius, 0 - inradius, center.x, center.y),
        point(circumradius, 0, center.x, center.y),
        point(halfCircumradius, inradius, center.x, center.y),
        point(0 - halfCircumradius, inradius, center.x, center.y),
        point(0 - circumradius, 0, center.x, center.y)
    ].join(' ');
}

function getPointsVertical(coordinate) {
    let center = getHexCenter(coordinate[0], coordinate[1]);
    return [
        point(0 - inradius, 0 - halfCircumradius, center.x, center.y),
        point(0 - inradius, halfCircumradius, center.x, center.y),
        point(0, circumradius, center.x, center.y),
        point(inradius, halfCircumradius, center.x, center.y),
        point(inradius, 0 - halfCircumradius, center.x, center.y),
        point(0, 0 - circumradius, center.x, center.y)
    ].join(' ');
}

function createHex(coordinate, hexData) {
    return createSvg('polygon').attr({
        points: horizontal ? getPointsHorizontal(coordinate) : getPointsVertical(coordinate),
        tabindex: 1
    })
        .setHexData(hexData)
        .click(function() {
            $('.mapHex.active, .newHex.active').removeClass('active');
            $(this).addClass('active');
            $('#mapSidebar').show();
            linkSettlement(hexData);
            polulateForm(hexData);
            return false;
        });

}

function linkSettlement(hexData) {
    if (hexData != null && hexData.terrainType != null) {
        let settlementData = hexData.settlementData;
        if (settlementData != null) {
            $('#settlementLink').attr({
                class: 'settlementLink',
                href: `/settlement/${settlementData.id}`
            }).text(settlementData.name).show();
            $('#newSettlementButton').hide();
        } else {
            $('#newSettlementButton').show();
            $('#settlementLink').hide();
        }
    } else {
        $('#newSettlementButton').hide();
        $('#settlementLink').hide();
    }
}

$.fn.drawRivers = function (rivers, mapData) {
    if (!rivers || rivers.size === 0) return $(this);

    const riverContainer = $(this).appendSvgAttr('g', {
        id: 'riverContainer',
        transform: `translate(${offsetX * circumradius * 2} ${offsetY * inradius * 2})`
    })

    function processRiverPath(path, start, startEdgeX, startEdgeY) {
        if (!path) return;
        let pathString = ""
        const center = getHexCenter(path.x, path.y);
        if (start) pathString += `M ${center.x} ${center.y}`
        let isWater = Object.entries(mapData).some(([coordinate, hexData]) =>
            coordinate === `${path.x}:${path.y}` && hexData.terrainType === 'WATER'
        );
        if (!isWater && path.childNodes.length === 0) pathString += ` L ${center.x} ${center.y}`
        else path.childNodes.forEach((childPath, index) => {
            // Get centers for both hexes
            const childCenter = getHexCenter(childPath.x, childPath.y);

            // Calculate control point for curve
            const edgeX = (center.x + childCenter.x) / 2;
            const edgeY = (center.y + childCenter.y) / 2;

            // Create SVG path
            if (startEdgeX === 0 || startEdgeY === 0) pathString += ` M ${center.x} ${center.y} L ${edgeX} ${edgeY}`
            else {
                if (index !== 0) {
                    pathString += ` M ${startEdgeX} ${startEdgeY}`
                }
                pathString += ` Q ${center.x} ${center.y} ${edgeX} ${edgeY}`
            }
            // Continue processing child paths
            pathString += processRiverPath(childPath, false, edgeX, edgeY);
        });
        return pathString;
    }

    // Process each river path
    rivers.forEach(path => {
        riverContainer.appendSvgAttr('path', {
            d: processRiverPath(path, true, 0, 0),
            fill: 'none',
            stroke: '#4a90e2',
            'stroke-width': circumradius * riverWidth,
            'stroke-linecap': 'round',
            'stroke-linejoin': 'round'
        });
    });

    return $(this);
}
$.fn.drawRoads = function (roads) {
    if (!roads || roads.size === 0) return $(this);

    const roadContainer = $(this).appendSvgAttr('g', {
        id: 'roadContainer',
        transform: `translate(${offsetX * circumradius * 2} ${offsetY * inradius * 2})`
    });
    let currX = 0,
        currY = 0;

    function processRoadPath(path, start, startEdgeX, startEdgeY) {
        if (!path) return;
        let pathString = ""
        const center = getHexCenter(path.x, path.y);
        if (start) {
            pathString += `M ${center.x} ${center.y}`;
            currX = center.x;
            currY = center.y;
        }
        if (path.childNodes.length === 0 && !path.ephemeral) {
            pathString += ` L ${center.x} ${center.y}`
            currX = center.x;
            currY = center.y;
        }
        else path.childNodes.forEach((childPath, index) => {
            // Get centers for both hexes
            const childCenter = getHexCenter(childPath.x, childPath.y);

            // Calculate control point for curve
            const edgeX = (center.x + childCenter.x) / 2;
            const edgeY = (center.y + childCenter.y) / 2;

            // Create SVG path
            if (startEdgeX === 0 || startEdgeY === 0) {
                if(path.childNodes.length < 2) {
                    pathString += ` M ${center.x} ${center.y} L ${edgeX} ${edgeY}`
                    currX = edgeX;
                    currY = edgeY;
                }
            } else {
                if (index !== 0 || (currX !== startEdgeX && currY !== startEdgeY)) {
                    pathString += ` M ${startEdgeX} ${startEdgeY}`
                }
                pathString += ` Q ${center.x} ${center.y} ${edgeX} ${edgeY}`
                currX = edgeX;
                currY = edgeY;
            }

            path.childNodes.forEach((childPathB, indexB) => {
                if (indexB > index) {
                    const childCenterB = getHexCenter(childPathB.x, childPathB.y);
                    const edgeBX = (center.x + childCenterB.x) / 2;
                    const edgeBY = (center.y + childCenterB.y) / 2;

                    pathString += ` M ${edgeBX} ${edgeBY} Q ${center.x} ${center.y} ${edgeX} ${edgeY}`
                    currX = edgeX;
                    currY = edgeY;
                }
            })
            // Continue processing child paths
            if(!path.emphemeral) pathString += processRoadPath(childPath, false, edgeX, edgeY);
        });
        return pathString;
    }

    // Process each road path
    roads.forEach(path => {
        roadContainer.appendSvgAttr('path', {
            d: processRoadPath(path, true, 0, 0),
            fill: 'none',
            stroke: '#daa06d',
            'stroke-width': circumradius * roadWidth,
            'stroke-linecap': 'round',
            'stroke-linejoin': 'round'
        });
    });

    return $(this);
}
