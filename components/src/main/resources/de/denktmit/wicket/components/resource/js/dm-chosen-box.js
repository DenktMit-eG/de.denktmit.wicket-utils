/* dm-chosen-box: chips and a suggesting input over the native multiple select, which stays the form's value. */
(function () {
  'use strict';
  dm.enhance('chosen', '[data-dm-chosen]', function (root) {
    var select = root.querySelector('select');
    if (!select) return;
    var box = document.createElement('div');
    box.className = 'dm-chosen-chips';
    var input = document.createElement('input');
    input.type = 'text';
    input.className = 'dm-chosen-input';
    input.placeholder = root.dataset.dmChosenPlaceholder || '';
    input.setAttribute('role', 'combobox');
    input.setAttribute('aria-autocomplete', 'list');
    input.setAttribute('aria-expanded', 'false');
    var list = document.createElement('ul');
    list.className = 'dm-chosen-suggestions';
    list.setAttribute('role', 'listbox');
    list.hidden = true;
    root.insertBefore(box, select);
    box.appendChild(input);
    box.appendChild(list);
    function options() { return Array.prototype.slice.call(select.options); }
    function render() {
      Array.prototype.forEach.call(box.querySelectorAll('.dm-chosen-chip'), function (c) { c.remove(); });
      options().filter(function (o) { return o.selected; }).forEach(function (o) {
        var chip = document.createElement('span');
        chip.className = 'dm-chosen-chip';
        chip.textContent = o.text;
        var remove = document.createElement('button');
        remove.type = 'button';
        remove.className = 'dm-chosen-remove';
        remove.setAttribute('aria-label', 'Remove ' + o.text);
        remove.textContent = '×';
        remove.addEventListener('click', function () { o.selected = false; select.dispatchEvent(new Event('change', { bubbles: true })); render(); });
        chip.appendChild(remove);
        box.insertBefore(chip, input);
      });
    }
    function suggest() {
      var text = input.value.toLowerCase();
      list.innerHTML = '';
      var matches = options().filter(function (o) { return !o.selected && o.text.toLowerCase().indexOf(text) >= 0; }).slice(0, 10);
      matches.forEach(function (o, i) {
        var item = document.createElement('li');
        item.className = 'dm-chosen-suggestion';
        item.setAttribute('role', 'option');
        item.setAttribute('aria-selected', i === 0 ? 'true' : 'false');
        item.textContent = o.text;
        item.addEventListener('mousedown', function (event) { event.preventDefault(); choose(o); });
        list.appendChild(item);
      });
      list.hidden = matches.length === 0;
      input.setAttribute('aria-expanded', list.hidden ? 'false' : 'true');
    }
    function choose(o) {
      o.selected = true;
      input.value = '';
      select.dispatchEvent(new Event('change', { bubbles: true }));
      render();
      suggest();
    }
    input.addEventListener('input', suggest);
    input.addEventListener('focus', suggest);
    input.addEventListener('blur', function () { list.hidden = true; input.setAttribute('aria-expanded', 'false'); });
    input.addEventListener('keydown', function (event) {
      var items = list.querySelectorAll('[role="option"]');
      var index = Array.prototype.findIndex.call(items, function (i) { return i.getAttribute('aria-selected') === 'true'; });
      if (event.key === 'ArrowDown' || event.key === 'ArrowUp') {
        event.preventDefault();
        if (!items.length) return;
        var next = (index + (event.key === 'ArrowDown' ? 1 : items.length - 1)) % items.length;
        Array.prototype.forEach.call(items, function (i, n) { i.setAttribute('aria-selected', n === next ? 'true' : 'false'); });
      } else if (event.key === 'Enter' && items.length && !list.hidden) {
        event.preventDefault();
        var text = items[index < 0 ? 0 : index].textContent;
        var option = options().filter(function (o) { return o.text === text; })[0];
        if (option) choose(option);
      } else if (event.key === 'Backspace' && !input.value) {
        var selected = options().filter(function (o) { return o.selected; });
        if (selected.length) { selected[selected.length - 1].selected = false; render(); }
      }
    });
    render();
  });
})();
