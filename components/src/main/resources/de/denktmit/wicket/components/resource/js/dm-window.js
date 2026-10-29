/* dm-window: a modal window opens with showModal (backdrop, focus trap, Escape), the header drags the window.
   The server renders the open state and the close link reports the close; Escape triggers that link. */
(function () {
  'use strict';
  dm.enhance('window', '[data-dm-window]', function (dialog) {
    if (dialog.tagName !== 'DIALOG') return;
    var modal = dialog.dataset.dmWindow === 'modal';
    var close = dialog.querySelector('[data-dm-window-close]');
    if (modal && dialog.hasAttribute('open') && typeof dialog.showModal === 'function') {
      dialog.removeAttribute('open');
      dialog.showModal();
    }
    dialog.addEventListener('cancel', function (event) {
      event.preventDefault();
      if (close) close.click();
    });
    var handle = dialog.querySelector('[data-dm-window-handle]');
    if (handle && dialog.hasAttribute('data-dm-window-draggable')) {
      var start = null;
      handle.addEventListener('pointerdown', function (event) {
        if (event.target.closest('a, button')) return;
        var rect = dialog.getBoundingClientRect();
        start = { x: event.clientX - rect.left, y: event.clientY - rect.top };
        handle.setPointerCapture(event.pointerId);
      });
      handle.addEventListener('pointermove', function (event) {
        if (!start) return;
        dialog.style.margin = '0';
        dialog.style.left = Math.max(0, event.clientX - start.x) + 'px';
        dialog.style.top = Math.max(0, event.clientY - start.y) + 'px';
        dialog.style.right = 'auto';
        dialog.style.bottom = 'auto';
      });
      handle.addEventListener('pointerup', function () { start = null; });
    }
  });
})();
