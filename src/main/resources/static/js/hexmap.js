let campaign;

$.fn.addMap = function(campaignId, offsetX, offsetY, mapData) {
    campaign = campaignId;
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
            id: '#hexContainer',
            transform: `translate(${offsetX * 100} ${offsetY * 100 * 0.8660254})`
        })
        .populateMap(mapData);

    svgPanZoom('#svgMap', {
        dblClickZoomEnabled: false,
        minZoom: 0.1
    })

    $(document).on('keydown', function(event) {
        if (event.key === 'Escape') {
            $('#mapSidebar').hide();
            $('#mapView > svg > polygon.selected').removeClass('selected')
        }
    });
}

$.fn.populateMap = function(mapData) {
    return $(this).append(
        Object.entries(mapData).map(([coordinate, hexData]) =>
            createHex(coordinate.split(':'), hexData)
        )
    );
}

function polulateForm(hexData) {
    $('#hexForm').show();
    $('.hexQ').val(hexData.q);
    $('.hexR').val(hexData.r);
    $('#hexTerrain').val(hexData.terrainType);
    let road = $('#road').prop('disabled', hexData.terrainType === 'WATER');
    if (hexData.improvements.includes('ROAD')) {
        road.val('ROAD');
    } else if (hexData.improvements.includes('HIGHWAY')) {
        road.val('HIGHWAY');
    } else {
        road.val('')
    }
    $('#river').prop('disabled', hexData.terrainType === 'WATER')
        .prop('checked', hexData.features.includes('RIVER'));
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

$.fn.decorate = function() {
    return $(this).append(Array.from({length: 63}, (_, index) =>
        createFilter('Road', index + 1)
    ));
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
            height: 90,
            width: 102,
            x: -1,
            y: -1,
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

function getPointsHorizontal(circumradius, coordinate) {
    let halfCircumradius = circumradius / 2,
        inradius = 0.8660254 * circumradius,
        centerX = (coordinate[0] * 1.5 * circumradius),
        centerY = (coordinate[1] * 2 + (coordinate[0] & 1)) * inradius,
        point = function (x, y) {
            return Math.round(x + centerX) + ',' + Math.round(y + centerY);
        };
    return [
        point(0 - halfCircumradius, 0 - inradius),
        point(halfCircumradius, 0 - inradius),
        point(circumradius, 0),
        point(halfCircumradius, inradius),
        point(0 - halfCircumradius, inradius),
        point(0 - circumradius, 0)
    ].join(' ');
}

function getPointsVertical(circumradius, coordinate) {
    let halfCircumradius = circumradius / 2,
        inradius = 0.8660254 * circumradius,
        centerY = (coordinate[1] * 1.5 * circumradius),
        centerX = (coordinate[0] * 2 + (coordinate[1] & 1)) * inradius,
        point = function (x, y) {
            return Math.round(x + centerX) + ',' + Math.round(y + centerY);
        };
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
    return createSvg('polygon')
        .attr({
            points: getPointsHorizontal(50, coordinate),
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
                text: settlementData.name,
                class: 'settlementLink',
                href: `/settlement/${settlementData.id}`
            }).show();
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