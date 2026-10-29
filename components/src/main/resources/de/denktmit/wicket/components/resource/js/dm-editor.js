/* dm-editor: a tool bar and an editable area over the textarea, which keeps the HTML source and stays the value. */
(function () {
  'use strict';
  var COMMANDS = [
    ['B', 'bold', 'Bold'], ['I', 'italic', 'Italic'], ['U', 'underline', 'Underline'],
    ['H2', 'formatBlock', 'Heading', 'h2'], ['P', 'formatBlock', 'Paragraph', 'p'],
    ['•', 'insertUnorderedList', 'Bulleted list'], ['1.', 'insertOrderedList', 'Numbered list'],
    ['"', 'formatBlock', 'Quote', 'blockquote'], ['🔗', 'createLink', 'Link']
  ];
  dm.enhance('editor', '[data-dm-editor]', function (root) {
    var source = root.querySelector('textarea');
    var toolbar = root.querySelector('[data-dm-editor-toolbar]');
    var area = root.querySelector('[data-dm-editor-area]');
    if (!source || !toolbar || !area) return;
    area.contentEditable = 'true';
    area.innerHTML = source.value;
    area.hidden = false;
    toolbar.hidden = false;
    COMMANDS.forEach(function (c) {
      var button = document.createElement('button');
      button.type = 'button';
      button.textContent = c[0];
      button.title = c[2];
      button.setAttribute('aria-label', c[2]);
      button.addEventListener('mousedown', function (event) { event.preventDefault(); });
      button.addEventListener('click', function () {
        area.focus();
        if (c[1] === 'createLink') {
          var url = window.prompt('Address of the link');
          if (url) document.execCommand('createLink', false, url);
        } else {
          document.execCommand(c[1], false, c[3] || null);
        }
        source.value = area.innerHTML;
      });
      toolbar.appendChild(button);
    });
    area.addEventListener('input', function () { source.value = area.innerHTML; });
    area.addEventListener('blur', function () { source.value = area.innerHTML; });
  });
})();
