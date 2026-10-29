/* dm-color-field: the picker and the hex text follow each other. */
(function () {
  'use strict';
  dm.enhance('color', '[data-dm-color]', function (root) {
    var picker = root.querySelector('input[type="color"]');
    var text = root.querySelector('input[type="text"]');
    if (!picker || !text) return;
    picker.addEventListener('input', function () { text.value = picker.value; });
    text.addEventListener('input', function () { if (/^#[0-9a-fA-F]{6}$/.test(text.value)) picker.value = text.value.toLowerCase(); });
  });
})();
