/* dm-confirm: a click on an element with a question runs only after a yes; the capture phase stops it before any
   other listener, Ajax or plain. */
(function () {
  'use strict';
  document.addEventListener('click', function (event) {
    var element = event.target.closest('[data-dm-confirm]');
    if (!element) return;
    if (!window.confirm(element.dataset.dmConfirm)) {
      event.preventDefault();
      event.stopImmediatePropagation();
    }
  }, true);
})();
