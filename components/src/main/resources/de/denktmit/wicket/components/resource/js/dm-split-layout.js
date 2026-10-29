/* dm-split-layout: the divider drags, the collapse controls fold a pane. Without script the first pane's own
   resize handle does the resizing. */
(function () {
  'use strict';
  dm.enhance('split', '[data-dm-split]', function (split) {
    var horizontal = split.dataset.dmSplit === 'horizontal';
    var first = split.querySelector('[data-dm-split-pane="first"]');
    var second = split.querySelector('[data-dm-split-pane="second"]');
    var divider = split.querySelector('[data-dm-split-divider]');
    if (!first || !second || !divider) return;
    Array.prototype.forEach.call(split.querySelectorAll('[data-dm-split-collapse]'), function (button) {
      button.hidden = false;
      button.addEventListener('click', function (event) {
        event.stopPropagation();
        var pane = button.dataset.dmSplitCollapse === 'first' ? first : second;
        pane.hidden = !pane.hidden;
        button.setAttribute('aria-expanded', pane.hidden ? 'false' : 'true');
      });
    });
    var dragging = false;
    divider.addEventListener('pointerdown', function (event) {
      if (event.target !== divider) return;
      dragging = true;
      divider.setPointerCapture(event.pointerId);
    });
    divider.addEventListener('pointermove', function (event) {
      if (!dragging) return;
      var rect = split.getBoundingClientRect();
      var size = horizontal ? event.clientX - rect.left : event.clientY - rect.top;
      var total = horizontal ? rect.width : rect.height;
      size = Math.max(40, Math.min(total - 40, size));
      split.style.setProperty('--first-size', size + 'px');
    });
    divider.addEventListener('pointerup', function () { dragging = false; });
    divider.addEventListener('keydown', function (event) {
      var step = event.key === 'ArrowLeft' || event.key === 'ArrowUp' ? -16 : (event.key === 'ArrowRight' || event.key === 'ArrowDown' ? 16 : 0);
      if (!step) return;
      event.preventDefault();
      var current = horizontal ? first.getBoundingClientRect().width : first.getBoundingClientRect().height;
      split.style.setProperty('--first-size', Math.max(40, current + step) + 'px');
    });
  });
})();
