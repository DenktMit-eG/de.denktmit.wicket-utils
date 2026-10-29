/* dm-map-leaflet: renders the map model with Leaflet and reports viewport, clicks, markers and drawn points. */
(function () {
  'use strict';
  dm.enhance('map', '[data-dm-map]', function (root) {
    if (!window.L) return;
    var model = JSON.parse(root.dataset.dmMapModel);
    var call = new Function('event', 'lat', 'lng', 'zoom', 'key', 'points', 'return (' + root.dataset.dmMapCall + ')(event, lat, lng, zoom, key, points);');
    var canvas = root.querySelector('[data-dm-map-canvas]');
    canvas.hidden = false;
    var map = L.map(canvas).setView(model.center, model.zoom);
    var layers = {};
    model.layers.forEach(function (layer, i) {
      layers[layer.name] = L.tileLayer(layer.url, { attribution: layer.attribution });
      if (i === 0) layers[layer.name].addTo(map);
    });
    if (model.layers.length > 1) L.control.layers(layers).addTo(map);
    model.markers.forEach(function (m) {
      L.marker([m.lat, m.lng], { title: m.title }).addTo(map).on('click', function () { call('marker', m.lat, m.lng, map.getZoom(), m.key, ''); });
    });
    model.shapes.forEach(function (s) {
      if (s.kind === 'circle') L.circle(s.points[0], { radius: s.radius, color: s.color }).addTo(map);
      else if (s.kind === 'polygon') L.polygon(s.points, { color: s.color }).addTo(map);
      else L.polyline(s.points, { color: s.color }).addTo(map);
    });
    map.on('moveend', function () { var c = map.getCenter(); call('viewport', c.lat, c.lng, map.getZoom(), '', ''); });
    var drawn = [];
    var line = null;
    map.on('click', function (e) {
      if (model.drawable) {
        drawn.push([e.latlng.lat, e.latlng.lng]);
        if (line) line.remove();
        line = L.polyline(drawn, { color: '#c0392b' }).addTo(map);
        call('draw', e.latlng.lat, e.latlng.lng, map.getZoom(), '', drawn.map(function (p) { return p.join(','); }).join(';'));
      } else {
        call('click', e.latlng.lat, e.latlng.lng, map.getZoom(), '', '');
      }
    });
  });
})();
