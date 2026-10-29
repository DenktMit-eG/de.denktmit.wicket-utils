/* dm-file-drop: files dropped on the zone go into the native input; the chosen files are listed before the upload. */
(function () {
  'use strict';
  dm.enhance('file-drop', '[data-dm-file-drop]', function (root) {
    var zone = root.querySelector('[data-dm-file-drop-zone]');
    var input = root.querySelector('input[type="file"]');
    var chosen = root.querySelector('[data-dm-file-drop-chosen]');
    if (!zone || !input) return;
    function list() {
      if (!chosen) return;
      chosen.innerHTML = '';
      Array.prototype.forEach.call(input.files, function (file) {
        var item = document.createElement('li');
        item.textContent = file.name + ' (' + Math.round(file.size / 1024) + ' kB)';
        chosen.appendChild(item);
      });
    }
    input.addEventListener('change', list);
    ['dragenter', 'dragover'].forEach(function (type) {
      zone.addEventListener(type, function (event) { event.preventDefault(); zone.classList.add('dm-file-drop-over'); });
    });
    ['dragleave', 'drop'].forEach(function (type) {
      zone.addEventListener(type, function () { zone.classList.remove('dm-file-drop-over'); });
    });
    zone.addEventListener('drop', function (event) {
      event.preventDefault();
      if (!event.dataTransfer || !event.dataTransfer.files.length) return;
      if (input.multiple) {
        input.files = event.dataTransfer.files;
      } else {
        var one = new DataTransfer();
        one.items.add(event.dataTransfer.files[0]);
        input.files = one.files;
      }
      input.dispatchEvent(new Event('change', { bubbles: true }));
    });
  });
})();
