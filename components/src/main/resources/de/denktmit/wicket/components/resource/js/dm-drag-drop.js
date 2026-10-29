/* dm-drag-drop: sources drag under a key, targets of the same group report the dropped key to the server. */
(function () {
  'use strict';
  var dragged = null;
  dm.enhance('drag-source', '[data-dm-drag-key]', function (source) {
    source.addEventListener('dragstart', function (event) {
      dragged = { key: source.dataset.dmDragKey, group: source.dataset.dmDragGroup };
      event.dataTransfer.effectAllowed = 'move';
      event.dataTransfer.setData('text/plain', dragged.key);
      source.classList.add('dm-dragging');
    });
    source.addEventListener('dragend', function () { source.classList.remove('dm-dragging'); dragged = null; });
  });
  dm.enhance('drop-target', '[data-dm-drop-group]', function (target) {
    var call = new Function('key', 'return (' + target.dataset.dmDropCall + ')(key);');
    target.addEventListener('dragover', function (event) {
      if (dragged && dragged.group === target.dataset.dmDropGroup) { event.preventDefault(); target.classList.add('dm-drop-over'); }
    });
    target.addEventListener('dragleave', function () { target.classList.remove('dm-drop-over'); });
    target.addEventListener('drop', function (event) {
      event.preventDefault();
      target.classList.remove('dm-drop-over');
      if (dragged && dragged.group === target.dataset.dmDropGroup) call(dragged.key);
    });
  });
})();
