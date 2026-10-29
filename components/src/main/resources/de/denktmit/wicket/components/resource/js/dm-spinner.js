/* dm-spinner: the up and down controls step the native number input. */
(function () {
  'use strict';
  dm.enhance('spinner', '[data-dm-spinner]', function (spinner) {
    var input = spinner.querySelector('input[type="number"]');
    if (!input) return;
    function step(direction) {
      if (input.disabled || input.readOnly) return;
      if (direction > 0) input.stepUp(); else input.stepDown();
      input.dispatchEvent(new Event('change', { bubbles: true }));
    }
    var up = spinner.querySelector('[data-dm-spinner-up]');
    var down = spinner.querySelector('[data-dm-spinner-down]');
    if (up) up.addEventListener('click', function () { step(1); });
    if (down) down.addEventListener('click', function () { step(-1); });
  });
})();
