/* dm-chart-chartjs: renders the chart model with Chart.js, reports a clicked point, exports a PNG. */
(function () {
  'use strict';
  var palette = ['#0b5f8a', '#2a8a5a', '#b26a00', '#c0392b', '#6c5ce7', '#00a8b5'];
  dm.enhance('chart', '[data-dm-chart]', function (root) {
    if (!window.Chart) return;
    var model = JSON.parse(root.dataset.dmChartModel);
    var call = new Function('series', 'point', 'return (' + root.dataset.dmChartCall + ')(series, point);');
    var holder = root.querySelector('[data-dm-chart-canvas]');
    var tools = root.querySelector('[data-dm-chart-tools]');
    holder.hidden = false;
    if (tools) tools.hidden = false;
    var canvas = holder.querySelector('canvas');
    canvas.setAttribute('role', 'img');
    canvas.setAttribute('aria-label', root.dataset.dmChartTitle);
    var type = { bar: 'bar', line: 'line', area: 'line', pie: 'pie', scatter: 'scatter', gauge: 'doughnut' }[model.type];
    var datasets = model.series.map(function (s, i) {
      var color = s.color || palette[i % palette.length];
      var d = { label: s.name, data: model.type === 'scatter' ? s.points : s.values, borderColor: color, backgroundColor: model.type === 'pie' || model.type === 'gauge' ? model.labels.map(function (_, j) { return palette[j % palette.length]; }) : color };
      if (model.type === 'area') { d.fill = true; d.backgroundColor = color + '55'; }
      return d;
    });
    if (model.type === 'gauge' && model.series.length) {
      var value = model.series[0].values[0] || 0;
      datasets = [{ data: [value, Math.max(0, model.max - value)], backgroundColor: [palette[0], '#d5dddf'], borderWidth: 0 }];
    }
    var options = { responsive: true, maintainAspectRatio: false, plugins: { legend: { display: model.legend && model.type !== 'gauge' }, tooltip: { enabled: model.tooltips } }, onClick: function (event, elements) { if (elements.length) call(elements[0].datasetIndex, elements[0].index); } };
    if (model.type === 'gauge') { options.rotation = -90; options.circumference = 180; options.cutout = '70%'; }
    if (['bar', 'line', 'area', 'scatter'].indexOf(model.type) >= 0) {
      options.scales = { x: { title: { display: !!model.xTitle, text: model.xTitle } }, y: { title: { display: !!model.yTitle, text: model.yTitle }, beginAtZero: true } };
    }
    var chart = new Chart(canvas, { type: type, data: { labels: model.type === 'gauge' ? ['value', 'rest'] : model.labels, datasets: datasets }, options: options });
    var exportLink = root.querySelector('[data-dm-chart-export]');
    if (exportLink) exportLink.addEventListener('click', function () { exportLink.href = chart.toBase64Image(); });
  });
})();
