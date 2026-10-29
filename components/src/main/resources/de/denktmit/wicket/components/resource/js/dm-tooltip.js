/* dm-tooltip: a styled tooltip after a delay on hover and focus; the title attribute stays for script-less use. */
(function () {
  'use strict';
  var tip = null;
  function show(element) {
    hide();
    tip = document.createElement('div');
    tip.className = 'dm-tooltip';
    tip.setAttribute('role', 'tooltip');
    tip.id = 'dm-tooltip-' + Math.random().toString(36).slice(2);
    tip.textContent = element.dataset.dmTooltip;
    document.body.appendChild(tip);
    var r = element.getBoundingClientRect();
    tip.style.left = Math.max(4, Math.min(window.innerWidth - tip.offsetWidth - 4, r.left)) + 'px';
    tip.style.top = (r.bottom + 6) + 'px';
    element.setAttribute('aria-describedby', tip.id);
  }
  function hide() { if (tip) { tip.remove(); tip = null; } }
  dm.enhance('tooltip', '[data-dm-tooltip]', function (element) {
    var timer;
    element.removeAttribute('title');
    function arm() { clearTimeout(timer); timer = setTimeout(function () { show(element); }, Number(element.dataset.dmTooltipDelay || 400)); }
    function disarm() { clearTimeout(timer); hide(); element.removeAttribute('aria-describedby'); }
    element.addEventListener('mouseenter', arm);
    element.addEventListener('focus', arm);
    element.addEventListener('mouseleave', disarm);
    element.addEventListener('blur', disarm);
    element.addEventListener('keydown', function (event) { if (event.key === 'Escape') disarm(); });
  });
})();
