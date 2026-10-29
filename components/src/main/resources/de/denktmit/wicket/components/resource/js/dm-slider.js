/* dm-slider: the output follows the value; a range slider keeps its two values in order. */
(function () {
  'use strict';
  function whole(v) { var n = Number(v); return n === Math.floor(n) ? String(n) : v; }
  dm.enhance('slider', '[data-dm-slider]', function (root) {
    var input = root.querySelector('input[type="range"]');
    var output = root.querySelector('output');
    if (!input || !output) return;
    input.addEventListener('input', function () { output.value = whole(input.value); });
  });
  dm.enhance('range-slider', '[data-dm-range-slider]', function (root) {
    var low = root.querySelector('.dm-range-low');
    var high = root.querySelector('.dm-range-high');
    var output = root.querySelector('output');
    if (!low || !high) return;
    function sync(changed) {
      if (Number(low.value) > Number(high.value)) {
        if (changed === low) high.value = low.value; else low.value = high.value;
      }
      if (output) output.value = whole(low.value) + ' – ' + whole(high.value);
    }
    low.addEventListener('input', function () { sync(low); });
    high.addEventListener('input', function () { sync(high); });
  });
})();
