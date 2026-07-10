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

        var name    = document.getElementById('t-name').value.trim();
        var email   = document.getElementById('t-email').value.trim();
        var tourSel = document.getElementById('t-tour');
        var tourId  = tourSel.value;
        var date    = document.getElementById('t-date').value;
        var guestsEl = document.getElementById('t-guests');
        var guests  = guestsEl ? guestsEl.value : 1;
        var notesEl = document.getElementById('t-notes');
        var notes   = notesEl ? notesEl.value.trim() : '';

        if (!name) { showError('Please enter your full name.'); return; }
        if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) { showError('Please enter a valid email address.'); return; }
        if (!tourId)  { showError('Please select a tour.'); return; }
        if (!date)  { showError('Please choose a date.'); return; }

        btn.disabled = true;
        btn.textContent = 'Reserving…';

        var formData = new URLSearchParams();
        formData.append('name', name);
        formData.append('email', email);
        formData.append('tourId', tourId);
        formData.append('date', date);
        formData.append('guests', guests);
        formData.append('notes', notes);

        fetch('/api/reserve-tour', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: formData
        })
            .then(function (res) {
                return res.json().then(function (data) {
                    return { status: res.status, data: data };
                });
            })
            .then(function (result) {
                if (result.status === 200) {
                    status.textContent = 'Your place is reserved. We look forward to seeing you.';
                    status.className = 'form-status success';
                    form.reset();
                } else if (result.status === 401) {
                    showError('Please sign in to book a tour.');
                } else {
                    showError(result.data.error || 'Could not complete reservation.');
                }
            })
            .catch(function () {
                showError('Something went wrong. Please try again.');
            })
            .finally(function () {
                btn.disabled = false;
                btn.textContent = 'Reserve My Place';
            });
    });

    function showError(msg) {
        status.textContent = msg;
        status.className = 'form-status error';
    }
})();