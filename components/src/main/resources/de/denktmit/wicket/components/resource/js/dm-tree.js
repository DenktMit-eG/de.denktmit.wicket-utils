/* dm-tree: indeterminate check boxes and node drag and drop reported to the server. */
(function () {
  'use strict';
  dm.enhance('tree', '[data-dm-tree]', function (tree) {
    Array.prototype.forEach.call(tree.querySelectorAll('input[data-dm-indeterminate]'), function (box) { box.indeterminate = true; });
    var callbackSource = tree.dataset.dmTreeDrop;
    if (!callbackSource) return;
    var callback = new Function('dragged', 'target', 'return (' + callbackSource + ')(dragged, target);');
    var dragged = null;
    Array.prototype.forEach.call(tree.querySelectorAll('[data-dm-tree-node][draggable="true"]'), function (node) {
      node.addEventListener('dragstart', function (event) { dragged = node.dataset.dmTreeNode; event.dataTransfer.effectAllowed = 'move'; event.stopPropagation(); });
      node.addEventListener('dragover', function (event) { if (dragged && dragged !== node.dataset.dmTreeNode) { event.preventDefault(); node.classList.add('dm-tree-drop-over'); } event.stopPropagation(); });
      node.addEventListener('dragleave', function () { node.classList.remove('dm-tree-drop-over'); });
      node.addEventListener('drop', function (event) {
        event.preventDefault();
        event.stopPropagation();
        node.classList.remove('dm-tree-drop-over');
        if (dragged && dragged !== node.dataset.dmTreeNode) callback(dragged, node.dataset.dmTreeNode);
        dragged = null;
      });
    });
  });
})();
