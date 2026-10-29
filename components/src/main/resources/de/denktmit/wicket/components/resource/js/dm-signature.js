/* dm-signature: draws on the canvas and writes the PNG into the hidden field on every stroke. */
(function () {
  'use strict';
  dm.enhance('signature', '[data-dm-signature]', function (root) {
    var canvas = root.querySelector('[data-dm-signature-canvas]');
    var field = root.querySelector('input[type="hidden"]');
    var clear = root.querySelector('[data-dm-signature-clear]');
    if (!canvas || !field) return;
    canvas.width = Number(root.dataset.dmSignatureWidth) || 400;
    canvas.height = Number(root.dataset.dmSignatureHeight) || 150;
    var ctx = canvas.getContext('2d');
    ctx.lineWidth = 2;
    ctx.lineCap = 'round';
    ctx.strokeStyle = getComputedStyle(root).color;
    if (field.value) { var img = new Image(); img.onload = function () { ctx.drawImage(img, 0, 0); }; img.src = field.value; }
    var drawing = false;
    function point(event) { var r = canvas.getBoundingClientRect(); return { x: (event.clientX - r.left) * canvas.width / r.width, y: (event.clientY - r.top) * canvas.height / r.height }; }
    canvas.addEventListener('pointerdown', function (event) { drawing = true; var p = point(event); ctx.beginPath(); ctx.moveTo(p.x, p.y); canvas.setPointerCapture(event.pointerId); });
    canvas.addEventListener('pointermove', function (event) { if (!drawing) return; var p = point(event); ctx.lineTo(p.x, p.y); ctx.stroke(); });
    function end() { if (!drawing) return; drawing = false; field.value = canvas.toDataURL('image/png'); field.dispatchEvent(new Event('change', { bubbles: true })); }
    canvas.addEventListener('pointerup', end);
    canvas.addEventListener('pointerleave', end);
    if (clear) {
      clear.hidden = false;
      clear.addEventListener('click', function () { ctx.clearRect(0, 0, canvas.width, canvas.height); field.value = ''; });
    }
  });
})();
