const
    circumradius = 50,
    halfCircumradius = circumradius / 2,
    inradius = Math.round(0.8660254 * circumradius),
    riverWidth = 0.25,
    roadWidth = 0.15
    lakeRadius = 25;

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
        .drawRoads(roads)
        .drawSettlements(mapData);

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

function point(x, y) {
    return Math.round(x) + ',' + Math.round(y)
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

function getPointsHorizontal() {
    return [
        point(0 - halfCircumradius, 0 - inradius),
        point(halfCircumradius, 0 - inradius),
        point(circumradius, 0),
        point(halfCircumradius, inradius),
        point(0 - halfCircumradius, inradius),
        point(0 - circumradius, 0)
    ].join(' ');
}

function getPointsVertical() {
    return [
        point(0 - inradius, 0 - halfCircumradius),
        point(0 - inradius, halfCircumradius),
        point(0, circumradius),
        point(inradius, halfCircumradius),
        point(inradius, 0 - halfCircumradius),
        point(0, 0 - circumradius)
    ].join(' ');
}

function createHex(coordinate, hexData) {
    let hex = createSvg('g')
        .click(function() {
            $('.mapHex.active, .newHex.active').removeClass('active');
            $(`.mapHex[x=${coordinate[0]}][y=${coordinate[1]}]`).addClass('active');
            $('#mapSidebar').show();
            linkSettlement(hexData);
            polulateForm(hexData);
            return false;
        }),
        center = getHexCenter(coordinate[0], coordinate[1]);
    hex.appendSvgAttr('polygon', {
        x: coordinate[0],
        y: coordinate[1],
        points: horizontal ? getPointsHorizontal() : getPointsVertical(),
        tabindex: 1,
        transform: `translate(${center.x} ${center.y})`,
    }).setHexData(hexData);
    hexData.features.forEach(feature => {
        if (feature === 'LAKE') hex.drawLake(coordinate);
    });
    return hex;
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
        'pointer-events': 'none',
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
        'pointer-events': 'none',
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

$.fn.drawLake = function (coordinate) {
    // Generate 8 points around a circle to create a natural lake shape
    const points = [];
    const position = getHexCenter(coordinate[0], coordinate[1]);

    for (let i = 0; i < 8; i++) {
        const angle = (i / 8) * Math.PI * 2;
        // Add random variation to the radius for a more natural shape
        const radius = lakeRadius + (Math.random() * 15 - 7.5);
        points.push({
            x: Math.cos(angle) * radius + position.x,
            y: Math.sin(angle) * radius + position.y
        });
    }

    // Create the SVG path string using cubic bezier curves
    let pathData = `M ${points[0].x} ${points[0].y}`;

    for (let i = 0; i < points.length; i++) {
        const current = points[i];
        const next = points[(i + 1) % points.length];

        // Create smooth control points for the curve
        const controlPoint1 = {
            x: current.x + (next.x - current.x) * 0.5 + (Math.random() * 10 - 5),
            y: current.y + (next.y - current.y) * 0.5 + (Math.random() * 10 - 5)
        };

        pathData += ` S ${controlPoint1.x} ${controlPoint1.y}, ${next.x} ${next.y}`;
    }

    pathData += ' Z';

    $(this).appendSvgAttr('path', {
        d: pathData,
        fill: '#4a90e2',
        stroke: '#9c8566',
        'stroke-width': '1.5',
        'stroke-opacity': '0.8',
        'fill-opacity': '0.9'
    });
    return $(this);
}

$.fn.drawSettlements = function (mapData) {
    let container = $(this).appendSvgAttr('g', {
        id: 'settlementContainer',
        'pointer-events': 'none',
        transform: `translate(${offsetX * circumradius * 2} ${offsetY * inradius * 2})`
    })
    Object.entries(mapData).forEach(([coordinate, hexData]) => {
        if(hexData.settlementData != null) {
            let rawCoord = coordinate.split(':'),
                coords = getHexCenter(rawCoord[0], rawCoord[1]),
                settlement = container.appendSvgAttr('g', {
                    transform: `translate(${coords.x} ${coords.y})`
                })
            settlement.appendSvgAttr('image', {
                href: `/img/Settlement/${hexData.settlementData.size}.svg`,
                width: circumradius,
                height: circumradius,
                transform: `translate(-${(horizontal ? circumradius : inradius)/2} -${(horizontal ? inradius : circumradius)/2})`
            })
            settlement.appendSvgAttr('text', {
                'text-anchor': 'middle'
            }).text(hexData.settlementData.name)
        }
    })
    return $(this);
}
