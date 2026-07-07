document.addEventListener('DOMContentLoaded', function () {

  const pills = document.querySelectorAll('.filter-pill');
  const tiles = document.querySelectorAll('.gal-tile');
  const emptyState = document.getElementById('gallery-empty');

  // filtering
  function applyFilter(category) {
    let visible = 0;

    tiles.forEach(function (tile) {
      const match = category === 'all' || tile.dataset.category === category;
      if (match) {
        tile.classList.remove('hidden-tile');
        visible++;
      } else {
        tile.classList.add('hidden-tile');
      }
    });

    if (visible > 0) {
      emptyState.classList.add('hidden');
    } else {
      emptyState.classList.remove('hidden');
    }
  }

  pills.forEach(function (pill) {
    pill.addEventListener('click', function () {
      pills.forEach(function (other) {
        other.classList.remove('active');
        other.setAttribute('aria-selected', 'false');
      });
      pill.classList.add('active');
      pill.setAttribute('aria-selected', 'true');
      applyFilter(pill.dataset.filter);
    });
  });

  // lightbox
  const lightbox = document.getElementById('lightbox');
  const lightboxImg = document.getElementById('lightbox-img');
  const lightboxCaption = document.getElementById('lightbox-caption');
  let current = 0;

  function visibleTiles() {
    const list = [];
    tiles.forEach(function (tile) {
      if (!tile.classList.contains('hidden-tile')) {
        list.push(tile);
      }
    });
    return list;
  }

  function showImage() {
    const tile = visibleTiles()[current];
    if (!tile) return;
    const img = tile.querySelector('img');
    const caption = tile.querySelector('figcaption');
    lightboxImg.src = img.src;
    lightboxImg.alt = img.alt;
    lightboxCaption.textContent = caption ? caption.textContent : '';
  }

  function openLightbox(index) {
    if (visibleTiles().length === 0) return;
    current = index;
    showImage();
    lightbox.classList.add('open');
    document.body.style.overflow = 'hidden';
  }

  function closeLightbox() {
    lightbox.classList.remove('open');
    document.body.style.overflow = '';
  }

  function move(step) {
    const list = visibleTiles();
    if (list.length === 0) return;
    current = (current + step + list.length) % list.length;
    showImage();
  }

  tiles.forEach(function (tile) {
    tile.addEventListener('click', function () {
      openLightbox(visibleTiles().indexOf(tile));
    });
  });

  document.getElementById('lightbox-close').addEventListener('click', closeLightbox);
  document.getElementById('lightbox-prev').addEventListener('click', function () {
    move(-1);
  });
  document.getElementById('lightbox-next').addEventListener('click', function () {
    move(1);
  });

  lightbox.addEventListener('click', function (e) {
    if (e.target === lightbox) closeLightbox();
  });

  document.addEventListener('keydown', function (e) {
    if (!lightbox.classList.contains('open')) return;
    if (e.key === 'Escape') closeLightbox();
    if (e.key === 'ArrowLeft') move(-1);
    if (e.key === 'ArrowRight') move(1);
  });

});