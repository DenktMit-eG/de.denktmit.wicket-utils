/* dm-card: maximise and close buttons; the card collapses natively as a details element. */
(function () {
  'use strict';
  dm.enhance('card', '[data-dm-card]', function (card) {
    var max = card.querySelector('[data-dm-card-maximize]');
    var close = card.querySelector('[data-dm-card-close]');
    if (max) {
      max.hidden = false;
      max.addEventListener('click', function (event) {
        event.preventDefault();
        event.stopPropagation();
        var on = card.classList.toggle('dm-card-maximized');
        max.setAttribute('aria-pressed', on ? 'true' : 'false');
      });
    }
    if (close) {
      close.hidden = false;
      close.addEventListener('click', function (event) {
        event.preventDefault();
        event.stopPropagation();
        card.hidden = true;
        dm.emit(card, 'dm:card-close', {});
      });
    }
  });
})();
