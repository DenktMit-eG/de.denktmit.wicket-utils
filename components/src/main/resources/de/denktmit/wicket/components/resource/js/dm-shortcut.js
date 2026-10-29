/* dm-shortcut: a key combination on the page or inside a widget fires the server action. */
(function () {
  'use strict';
  function matches(event, keys) {
    var parts = keys.toLowerCase().split('+');
    var key = parts[parts.length - 1];
    var want = { ctrl: parts.indexOf('ctrl') >= 0, alt: parts.indexOf('alt') >= 0, shift: parts.indexOf('shift') >= 0, meta: parts.indexOf('meta') >= 0 };
    return event.key.toLowerCase() === key && event.ctrlKey === want.ctrl && event.altKey === want.alt && event.shiftKey === want.shift && event.metaKey === want.meta;
  }
  dm.enhance('shortcut', '[data-dm-shortcut-keys]', function (element) {
    var scope = element.tagName === 'HTML' || element.tagName === 'BODY' ? document : element;
    var call = new Function('return (' + element.dataset.dmShortcutCall + ')();');
    scope.addEventListener('keydown', function (event) {
      if (!matches(event, element.dataset.dmShortcutKeys)) return;
      if (event.target.closest('input, textarea, select, [contenteditable]') && !event.ctrlKey && !event.altKey && !event.metaKey) return;
      event.preventDefault();
      call();
    });
  });
})();
