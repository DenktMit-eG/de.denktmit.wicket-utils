/* dm-large-list: the "load more" link is clicked when it scrolls into view. */
(function () {
  'use strict';
  dm.enhance('large-list', '[data-dm-large-list]', function (root) {
    if (!window.IntersectionObserver) return;
    var observer = new IntersectionObserver(function (entries) {
      entries.forEach(function (entry) { if (entry.isIntersecting) entry.target.click(); });
    });
    function watch() {
      var more = root.querySelector('[data-dm-large-list-more]');
      if (more) observer.observe(more);
    }
    watch();
    if (window.Wicket && Wicket.Event) Wicket.Event.subscribe('/ajax/call/complete', function () { watch(); });
  });
})();
