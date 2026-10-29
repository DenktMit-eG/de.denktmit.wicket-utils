/* dm-feedback: a message can be dismissed. */
(function () {
  'use strict';
  dm.enhance('feedback', '[data-dm-feedback] .alert', function (alert) {
    var close = alert.querySelector('[data-dm-feedback-close]');
    if (!close) return;
    close.hidden = false;
    close.addEventListener('click', function () { alert.remove(); });
  });
})();
