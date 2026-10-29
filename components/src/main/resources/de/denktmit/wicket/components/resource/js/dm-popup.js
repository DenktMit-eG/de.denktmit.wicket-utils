/* dm-popup: anchored to another widget, opens on click or hover, closes on a click outside or Escape. */
(function () {
  'use strict';
  dm.enhance('popup', '[data-dm-popup]', function (root) {
    var details = root.querySelector('[data-dm-popup-details]');
    if (!details) return;
    document.addEventListener('click', function (event) { if (!root.contains(event.target) && !(anchor && anchor.contains(event.target))) details.removeAttribute('open'); });
    root.addEventListener('keydown', function (event) { if (event.key === 'Escape') details.removeAttribute('open'); });
    var anchor = root.dataset.dmPopupFor ? document.getElementById(root.dataset.dmPopupFor) : null;
    if (!anchor) return;
    function place() {
      var r = anchor.getBoundingClientRect();
      root.classList.add('dm-popup-floating');
      root.style.left = r.left + 'px';
      root.style.top = (r.bottom + 4) + 'px';
    }
    function open() { place(); details.setAttribute('open', 'open'); }
    if (root.dataset.dmPopup === 'hover') {
      anchor.addEventListener('mouseenter', open);
      anchor.addEventListener('focus', open);
      var timer;
      anchor.addEventListener('mouseleave', function () { timer = setTimeout(function () { if (!root.matches(':hover')) details.removeAttribute('open'); }, 200); });
      root.addEventListener('mouseleave', function () { details.removeAttribute('open'); });
      root.addEventListener('mouseenter', function () { clearTimeout(timer); });
    } else {
      anchor.addEventListener('click', function (event) { event.preventDefault(); if (details.hasAttribute('open')) details.removeAttribute('open'); else open(); });
    }
  });
})();
