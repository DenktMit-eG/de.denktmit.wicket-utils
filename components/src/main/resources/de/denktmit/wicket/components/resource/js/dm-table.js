/* dm-table: a click on a row toggles its selection, headers resize by their handle and reorder by drag. */
(function () {
  'use strict';
  dm.enhance('table', '[data-dm-table]', function (root) {
    var table = root.querySelector('table');
    if (!table) return;
    root.addEventListener('click', function (event) {
      var row = event.target.closest('[data-dm-table-row]');
      if (!row || event.target.closest('a, button, input, [contenteditable]')) return;
      var select = row.querySelector('[data-dm-table-select]');
      if (select) select.click();
    });
    Array.prototype.forEach.call(root.querySelectorAll('[data-dm-table-resize]'), function (handle) {
      var th = handle.parentNode;
      var startX = 0, startWidth = 0;
      handle.addEventListener('pointerdown', function (event) {
        startX = event.clientX; startWidth = th.offsetWidth;
        handle.setPointerCapture(event.pointerId);
        event.preventDefault();
      });
      handle.addEventListener('pointermove', function (event) {
        if (!handle.hasPointerCapture || !handle.hasPointerCapture(event.pointerId)) return;
        th.style.width = Math.max(40, startWidth + event.clientX - startX) + 'px';
      });
    });
    var headers = root.querySelectorAll('[data-dm-table-header]');
    var dragged = null;
    function cellsOf(th) {
      var index = Array.prototype.indexOf.call(th.parentNode.children, th);
      return Array.prototype.map.call(table.querySelectorAll('tbody tr.dm-table-row, tfoot tr'), function (tr) { return tr.children[index]; });
    }
    Array.prototype.forEach.call(headers, function (th) {
      th.draggable = true;
      th.addEventListener('dragstart', function () { dragged = th; });
      th.addEventListener('dragover', function (event) { if (dragged && dragged !== th) event.preventDefault(); });
      th.addEventListener('drop', function (event) {
        event.preventDefault();
        if (!dragged || dragged === th) return;
        var from = cellsOf(dragged), to = cellsOf(th);
        var before = Array.prototype.indexOf.call(th.parentNode.children, dragged) < Array.prototype.indexOf.call(th.parentNode.children, th);
        th.parentNode.insertBefore(dragged, before ? th.nextSibling : th);
        from.forEach(function (cell, i) { if (cell && to[i]) to[i].parentNode.insertBefore(cell, before ? to[i].nextSibling : to[i]); });
        dragged = null;
      });
    });
  });
})();
