(function () {
    const form = document.getElementById('pool-form');
    if (!form) return;

    const status = document.getElementById('pool-status');
    const btn = form.querySelector('.pool-submit');

    const dateInput = document.getElementById('pool-date');
    if (dateInput) {
        const today = new Date();
        const yyyy = today.getFullYear();
        const mm = String(today.getMonth() + 1).padStart(2, '0');
        const dd = String(today.getDate()).padStart(2, '0');
        dateInput.min = `${yyyy}-${mm}-${dd}`;
    }

    form.addEventListener('submit', function (e) {
        e.preventDefault();
        status.textContent = '';
        status.className = 'form-status';

        const name = document.getElementById('pool-name').value.trim();
        const email = document.getElementById('pool-email').value.trim();
        const pool = document.getElementById('pool-select').value;
        const date = document.getElementById('pool-date').value;
        const time = document.getElementById('pool-time').value;

        if (!name) {
            showError('Please enter your full name.');
            return;
        }
        if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            showError('Please enter a valid email address.');
            return;
        }
        if (!pool) {
            showError('Please select a pool.');
            return;
        }
        if (!date) {
            showError('Please select a date.');
            return;
        }
        if (!time) {
            showError('Please select a session time.');
            return;
        }

        btn.disabled = true;
        btn.textContent = 'Checking availability…';

        setTimeout(function () {
            btn.disabled = false;
            btn.textContent = 'Check Availability & Book';

            status.textContent = '✓ Your slot is confirmed! A confirmation has been sent to ' + email + '. Please arrive 10 minutes early.';
            status.className = 'form-status success';

            form.reset();
        }, 1400);
    });

    function showError(msg) {
        status.textContent = msg;
        status.className = 'form-status error';
    }
})();
