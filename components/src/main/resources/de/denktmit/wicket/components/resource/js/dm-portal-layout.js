/* dm-portal-layout: panels drag between columns; the new order goes to the server as "a,b|c" per column. */
(function () {
  'use strict';
  dm.enhance('portal', '[data-dm-portal]', function (portal) {
    var callback = new Function('order', 'return (' + portal.dataset.dmPortalCallback + ')(order);');
    var dragged = null;
    function order() {
      return Array.prototype.map.call(portal.querySelectorAll('[data-dm-portal-column]'), function (column) {
        return Array.prototype.map.call(column.querySelectorAll('[data-dm-portal-panel]'), function (p) { return p.dataset.dmPortalPanel; }).join(',');
      }).join('|');
    }
    Array.prototype.forEach.call(portal.querySelectorAll('[data-dm-portal-panel]'), function (panel) {
      panel.addEventListener('dragstart', function (event) {
        dragged = panel;
        panel.classList.add('dm-portal-dragging');
        event.dataTransfer.effectAllowed = 'move';
        event.dataTransfer.setData('text/plain', panel.dataset.dmPortalPanel);
      });
      panel.addEventListener('dragend', function () {
        panel.classList.remove('dm-portal-dragging');
        dragged = null;
      });
    });
    Array.prototype.forEach.call(portal.querySelectorAll('[data-dm-portal-column]'), function (column) {
      column.addEventListener('dragover', function (event) {
        if (!dragged) return;
        event.preventDefault();
        column.classList.add('dm-portal-over');
        var after = Array.prototype.find.call(column.querySelectorAll('[data-dm-portal-panel]'), function (p) {
          return p !== dragged && event.clientY < p.getBoundingClientRect().top + p.offsetHeight / 2;
        });
        if (after) column.insertBefore(dragged, after); else column.appendChild(dragged);
      });
      column.addEventListener('dragleave', function () { column.classList.remove('dm-portal-over'); });
      column.addEventListener('drop', function (event) {
        event.preventDefault();
        column.classList.remove('dm-portal-over');
        callback(order());
      });
    });
  });
})();
