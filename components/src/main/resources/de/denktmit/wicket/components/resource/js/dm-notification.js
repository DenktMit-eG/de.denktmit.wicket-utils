/* dm-notification: a message leaves after its duration or on its close button. */
(function () {
  'use strict';
  dm.enhance('notification', '.dm-notification', function (item) {
    var close = item.querySelector('[data-dm-notification-close]');
    function leave() { item.remove(); }
    if (close) { close.hidden = false; close.addEventListener('click', leave); }
    var duration = Number(item.dataset.dmNotificationDuration || 0);
    if (duration > 0) setTimeout(leave, duration);
  });
})();
