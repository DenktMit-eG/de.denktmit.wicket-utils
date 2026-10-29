/* dm-band-box: the popup closes on a click outside or Escape. */
(function () {
  'use strict';
  dm.enhance('band-box', '[data-dm-band-box]', function (root) {
    var popup = root.querySelector('[data-dm-band-box-popup]');
    if (!popup) return;
    document.addEventListener('click', function (event) { if (!root.contains(event.target)) popup.removeAttribute('open'); });
    root.addEventListener('keydown', function (event) { if (event.key === 'Escape') popup.removeAttribute('open'); });
  });
})();
