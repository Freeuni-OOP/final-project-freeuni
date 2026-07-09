document.addEventListener('DOMContentLoaded', function () {

  // Set minimum date to today
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

    status.textContent = '';
    status.className = 'form-status';

    if (!name) {
      status.textContent = 'Please enter your full name.';
      status.className = 'form-status error';
      return;
    }
    if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      status.textContent = 'Please enter a valid email address.';
      status.className = 'form-status error';
      return;
    }
    if (!treatment) {
      status.textContent = 'Please select a treatment.';
      status.className = 'form-status error';
      return;
    }
    if (!date) {
      status.textContent = 'Please select a date.';
      status.className = 'form-status error';
      return;
    }
    if (!time) {
      status.textContent = 'Please select a time.';
      status.className = 'form-status error';
      return;
    }

    // Simulate availability check
    btn.disabled = true;
    btn.textContent = 'Checking availability…';

    setTimeout(function () {
      status.textContent =
        '✓ Your booking is confirmed! A confirmation has been sent to ' + email + '.';
      status.className = 'form-status success';
      btn.textContent = 'Booking Confirmed';
      form.reset();
      dateInput.min = new Date().toISOString().split('T')[0];
    }, 1200);
  });

});
