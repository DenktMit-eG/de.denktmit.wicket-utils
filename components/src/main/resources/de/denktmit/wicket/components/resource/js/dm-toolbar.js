/* dm-toolbar: items that no longer fit move into the "more" menu; without script the row wraps. */
(function () {
  'use strict';
  dm.enhance('toolbar', '[data-dm-toolbar]', function (toolbar) {
    var row = toolbar.querySelector('[data-dm-toolbar-items]');
    var overflow = toolbar.querySelector('[data-dm-toolbar-overflow]');
    var menu = toolbar.querySelector('[data-dm-toolbar-overflow-items]');
    if (!row || !overflow || !menu) return;
    var all = Array.prototype.slice.call(row.children);
    function layout() {
      all.forEach(function (item) { row.appendChild(item); });
      overflow.hidden = true;
      var limit = toolbar.clientWidth - 48;
      var moved = [];
      for (var i = all.length - 1; i >= 0 && row.scrollWidth > limit; i--) {
        moved.unshift(all[i]);
        menu.insertBefore(all[i], menu.firstChild);
      }
      overflow.hidden = moved.length === 0;
    }
    layout();
    if (window.ResizeObserver) new ResizeObserver(layout).observe(toolbar); else window.addEventListener('resize', layout);
  });
})();
