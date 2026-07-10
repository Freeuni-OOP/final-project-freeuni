document.addEventListener('DOMContentLoaded', function () {

    const dateInput = document.getElementById('spa-date');
    if (dateInput) {
        dateInput.min = new Date().toISOString().split('T')[0];
    }

    const form   = document.getElementById('spa-form');
    const status = document.getElementById('spa-status');
    const btn    = form.querySelector('.spa-submit');

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const name      = document.getElementById('spa-name').value.trim();
        const email     = document.getElementById('spa-email').value.trim();
        const treatment = document.getElementById('spa-treatment').value;
        const date      = document.getElementById('spa-date').value;
        const time      = document.getElementById('spa-time').value;
        const guestsRaw = document.getElementById('spa-guests').value;
        const notes     = document.getElementById('spa-notes').value.trim();

        status.textContent = '';
        status.className = 'form-status';

        if (!name) {
            showError('Please enter your full name.');
            return;
        }
        if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            showError('Please enter a valid email address.');
            return;
        }
        if (!treatment) {
            showError('Please select a treatment.');
            return;
        }
        if (!date) {
            showError('Please select a date.');
            return;
        }
        if (!time) {
            showError('Please select a time.');
            return;
        }

        const guests = parseInt(guestsRaw) || 1;

        btn.disabled = true;
        btn.textContent = 'Checking availability…';

        fetch('/api/spa/reservations', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email, treatment, date, time, guests, notes })
        })
        .then(function (res) { return res.json().then(function (data) { return { ok: res.ok, data: data }; }); })
        .then(function (result) {
            if (result.ok) {
                status.textContent = '✓ Your booking is confirmed! A confirmation has been sent to ' + email + '.';
                status.className = 'form-status success';
                btn.textContent = 'Booking Confirmed';
                form.reset();
                dateInput.min = new Date().toISOString().split('T')[0];
            } else {
                var msg = (result.data && result.data.error) ? result.data.error : 'Booking failed. Please try again.';
                showError(msg);
                btn.disabled = false;
                btn.textContent = 'Check Availability & Book';
            }
        })
        .catch(function () {
            showError('Connection error. Please check your network and try again.');
            btn.disabled = false;
            btn.textContent = 'Check Availability & Book';
        });
    });

    function showError(msg) {
        status.textContent = msg;
        status.className = 'form-status error';
    }
});
