/* dm-drawer: opens as a modal, a click outside the panel or Escape triggers the close link. */
(function () {
  'use strict';
  dm.enhance('drawer', '[data-dm-drawer]', function (dialog) {
    var close = dialog.querySelector('[data-dm-drawer-close]');
    var panel = dialog.querySelector('[data-dm-drawer-panel]');
    if (dialog.hasAttribute('open') && typeof dialog.showModal === 'function') {
      dialog.removeAttribute('open');
      dialog.showModal();
    }
    dialog.addEventListener('cancel', function (event) {
      event.preventDefault();
      if (close) close.click();
    });
    dialog.addEventListener('click', function (event) {
      if (panel && !panel.contains(event.target) && close) close.click();
    });
  });
})();
