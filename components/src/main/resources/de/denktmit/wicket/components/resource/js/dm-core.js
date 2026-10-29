/* dm-core: the enhancement loop every widget script registers with. Script adds comfort and never structure:
   a widget renders, submits and navigates without it. */
(function () {
  'use strict';
  if (window.dm) return;
  var enhancers = [];
  function run(root) {
    root = root || document;
    enhancers.forEach(function (e) {
      var nodes = root.querySelectorAll ? root.querySelectorAll(e.selector) : [];
      Array.prototype.forEach.call(nodes, function (node) {
        if (node.dataset.dmEnhanced && node.dataset.dmEnhanced.indexOf(e.name) >= 0) return;
        node.dataset.dmEnhanced = (node.dataset.dmEnhanced ? node.dataset.dmEnhanced + ' ' : '') + e.name;
        e.fn(node);
      });
    });
  }
  window.dm = {
    /** Registers fn for every element matching selector, now and after every Ajax update. */
    enhance: function (name, selector, fn) {
      enhancers.push({ name: name, selector: selector, fn: fn });
      if (document.readyState !== 'loading') run(document);
    },
    /** Runs all enhancers again, for markup added by other means. */
    refresh: run,
    /** Dispatches a bubbling custom event with detail on element. */
    emit: function (element, type, detail) {
      element.dispatchEvent(new CustomEvent(type, { bubbles: true, detail: detail }));
    }
  };
  document.addEventListener('DOMContentLoaded', function () { run(document); });
  if (window.Wicket && Wicket.Event) {
    Wicket.Event.subscribe('/ajax/call/complete', function () { run(document); });
  } else {
    document.addEventListener('DOMContentLoaded', function () {
      if (window.Wicket && Wicket.Event) Wicket.Event.subscribe('/ajax/call/complete', function () { run(document); });
    });
  }
})();
