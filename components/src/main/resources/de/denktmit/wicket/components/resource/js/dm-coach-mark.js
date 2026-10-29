/* dm-coach-mark: outlines the step's widget and places the dialog next to it. */
(function () {
  'use strict';
  var highlighted = null;
  dm.enhance('coach', '[data-dm-coach-mark]', function (root) {
    function apply() {
      if (highlighted) { highlighted.classList.remove('dm-coach-highlight'); highlighted = null; }
      var dialog = root.querySelector('[data-dm-coach-dialog]');
      if (!dialog) return;
      var target = document.getElementById(dialog.dataset.dmCoachTarget);
      if (!target) return;
      target.classList.add('dm-coach-highlight');
      target.scrollIntoView({ block: 'center', behavior: 'smooth' });
      highlighted = target;
      var r = target.getBoundingClientRect();
      dialog.style.left = Math.max(8, Math.min(window.innerWidth - dialog.offsetWidth - 8, r.left)) + 'px';
      dialog.style.top = Math.min(window.innerHeight - dialog.offsetHeight - 8, r.bottom + 12) + 'px';
    }
    apply();
    if (window.Wicket && Wicket.Event) Wicket.Event.subscribe('/ajax/call/complete', apply);
  });
})();
