/* dm-menu: arrow keys move through a menu, Escape closes, shortcuts trigger items; a popup menu opens from a
   widget on click or right click. Sub-menus open by CSS on hover and focus. */
(function () {
  'use strict';
  function links(root) { return Array.prototype.filter.call(root.querySelectorAll('[role^="menuitem"]'), function (l) { return l.offsetParent !== null; }); }
  dm.enhance('menu', '[data-dm-menu]', function (root) {
    root.addEventListener('keydown', function (event) {
      var all = links(root);
      var index = all.indexOf(document.activeElement);
      if (index < 0) return;
      var next = null;
      if (event.key === 'ArrowDown' || event.key === 'ArrowRight') next = all[(index + 1) % all.length];
      else if (event.key === 'ArrowUp' || event.key === 'ArrowLeft') next = all[(index + all.length - 1) % all.length];
      else if (event.key === 'Home') next = all[0];
      else if (event.key === 'End') next = all[all.length - 1];
      else if (event.key === 'Escape') { var open = root.querySelector('.dm-menu-open'); if (open) open.classList.remove('dm-menu-open'); var popup = root.closest('details'); if (popup) popup.removeAttribute('open'); return; }
      if (next) { event.preventDefault(); next.focus(); }
    });
    Array.prototype.forEach.call(root.querySelectorAll('[data-dm-shortcut]'), function (link) {
      var parts = link.dataset.dmShortcut.toLowerCase().split('+');
      document.addEventListener('keydown', function (event) {
        var key = parts[parts.length - 1];
        var want = { ctrl: parts.indexOf('ctrl') >= 0, alt: parts.indexOf('alt') >= 0, shift: parts.indexOf('shift') >= 0 };
        if (event.key.toLowerCase() === key && event.ctrlKey === want.ctrl && event.altKey === want.alt && event.shiftKey === want.shift) {
          event.preventDefault();
          if (link.getAttribute('aria-disabled') !== 'true') link.click();
        }
      });
    });
    var forId = root.dataset.dmMenuFor;
    var popup = root.querySelector('[data-dm-popup-menu]');
    if (forId && popup) {
      var owner = document.getElementById(forId);
      if (!owner) return;
      function openAt(event) {
        event.preventDefault();
        popup.classList.add('dm-popup-floating');
        popup.style.left = event.clientX + 'px';
        popup.style.top = event.clientY + 'px';
        popup.setAttribute('open', 'open');
        var first = links(popup)[0];
        if (first) first.focus();
      }
      owner.addEventListener('contextmenu', openAt);
      owner.addEventListener('click', openAt);
      document.addEventListener('click', function (event) { if (!popup.contains(event.target) && !owner.contains(event.target)) popup.removeAttribute('open'); });
    }
  });
})();
