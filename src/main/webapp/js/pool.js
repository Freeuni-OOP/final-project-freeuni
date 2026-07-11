(function () {
    var form = document.getElementById('pool-form');
    if (!form) return;

    var status = document.getElementById('pool-status');
    var btn = form.querySelector('.pool-submit');

    var dateInput = document.getElementById('pool-date');
    if (dateInput) {
        dateInput.min = new Date().toISOString().split('T')[0];
    }

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        var name     = document.getElementById('pool-name').value.trim();
        var email    = document.getElementById('pool-email').value.trim();
        var pool     = document.getElementById('pool-select').value;
        var activity = document.getElementById('pool-activity').value;
        var date     = document.getElementById('pool-date').value;
        var time     = document.getElementById('pool-time').value;
        var guestsRaw = document.getElementById('pool-guests').value;
        var notes    = document.getElementById('pool-notes').value.trim();

        status.textContent = '';
        status.className = 'form-status';

        if (!name) { showError('Please enter your full name.'); return; }
        if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) { showError('Please enter a valid email address.'); return; }
        if (!pool) { showError('Please select a pool.'); return; }
        if (!date) { showError('Please select a date.'); return; }
        if (!time) { showError('Please select a session time.'); return; }

        var guests = parseInt(guestsRaw) || 1;

        btn.disabled = true;
        btn.textContent = 'Checking availability…';

        fetch('/api/pool/reservations', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name: name, email: email, pool: pool, activity: activity, date: date, time: time, guests: guests, notes: notes })
        })
        .then(function (res) { return res.json().then(function (data) { return { ok: res.ok, data: data }; }); })
        .then(function (result) {
            if (result.ok) {
                status.textContent = '✓ Your slot is confirmed! A confirmation has been sent to ' + email + '. Please arrive 10 minutes early.';
                status.className = 'form-status success';
                btn.textContent = 'Slot Reserved';
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
})();
