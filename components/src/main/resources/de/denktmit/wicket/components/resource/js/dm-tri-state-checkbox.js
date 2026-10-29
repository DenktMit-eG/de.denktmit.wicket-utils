/* dm-tri-state-checkbox: the box cycles unset -> checked -> unchecked and records the state in the hidden field. */
(function () {
  'use strict';
  dm.enhance('tristate', '[data-dm-tristate]', function (root) {
    var box = root.querySelector('input[type="checkbox"]');
    var state = root.querySelector('input[type="hidden"]');
    if (!box || !state) return;
    var current = box.hasAttribute('data-dm-indeterminate') ? null : box.checked;
    function apply() {
      box.indeterminate = current === null;
      box.checked = current === true;
      box.setAttribute('aria-checked', current === null ? 'mixed' : String(current));
      state.value = current === null ? 'null' : String(current);
    }
    apply();
    box.addEventListener('click', function (event) {
      event.preventDefault();
      current = current === null ? true : (current === true ? false : null);
      apply();
      box.dispatchEvent(new Event('change', { bubbles: true }));
    });
  });
})();
