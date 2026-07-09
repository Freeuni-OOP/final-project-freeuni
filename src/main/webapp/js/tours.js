(function () {
    // Thumbnail gallery switcher
    document.querySelectorAll('.tour-gallery').forEach(function (gallery) {
        var mainImg = gallery.querySelector('.tour-gallery-main');
        gallery.querySelectorAll('.tour-thumb').forEach(function (thumb) {
            thumb.addEventListener('click', function () {
                gallery.querySelectorAll('.tour-thumb').forEach(function (t) { t.classList.remove('active'); });
                thumb.classList.add('active');
                mainImg.style.backgroundImage = "url('" + thumb.dataset.full + "')";
            });
        });
    });

    // "Book This Tour" buttons pre-fill the select
    document.querySelectorAll('.tour-book-btn').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            var tourName = btn.dataset.tour;
            var select = document.getElementById('t-tour');
            if (!select) return;
            for (var i = 0; i < select.options.length; i++) {
                if (select.options[i].text.startsWith(tourName)) {
                    select.selectedIndex = i;
                    break;
                }
            }
        });
    });

    // Date min = today
    var dateInput = document.getElementById('t-date');
    if (dateInput) {
        var today = new Date();
        var yyyy = today.getFullYear();
        var mm = String(today.getMonth() + 1).padStart(2, '0');
        var dd = String(today.getDate()).padStart(2, '0');
        dateInput.min = yyyy + '-' + mm + '-' + dd;
    }

    // Form submission
    var form = document.getElementById('tours-form');
    if (!form) return;

    var status = document.getElementById('t-status');
    var btn = form.querySelector('.tours-submit');

    form.addEventListener('submit', function (e) {
        e.preventDefault();
        status.textContent = '';
        status.className = 'form-status';

        var name   = document.getElementById('t-name').value.trim();
        var email  = document.getElementById('t-email').value.trim();
        var tour   = document.getElementById('t-tour').value;
        var date   = document.getElementById('t-date').value;

        if (!name) { showError('Please enter your full name.'); return; }
        if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) { showError('Please enter a valid email address.'); return; }
        if (!tour)  { showError('Please select a tour.'); return; }
        if (!date)  { showError('Please choose a date.'); return; }

        btn.disabled = true;
        btn.textContent = 'Reserving…';

        setTimeout(function () {
            btn.disabled = false;
            btn.textContent = 'Reserve My Place';
            status.textContent = '✓ Your place is reserved! A confirmation and full itinerary have been sent to ' + email + '.';
            status.className = 'form-status success';
            form.reset();
        }, 1400);
    });

    function showError(msg) {
        status.textContent = msg;
        status.className = 'form-status error';
    }
})();
