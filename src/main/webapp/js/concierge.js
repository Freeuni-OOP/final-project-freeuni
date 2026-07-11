document.addEventListener('DOMContentLoaded', function () {

  const form = document.getElementById('concierge-form');
  const status = document.getElementById('cr-status');
  const submitBtn = form ? form.querySelector('.form-submit') : null;

  function setError(input, message) {
    input.classList.add('invalid');
    const el = document.querySelector('.field-error[data-for="' + input.id + '"]');
    if (el) el.textContent = message;
  }

  function clearError(input) {
    input.classList.remove('invalid');
    const el = document.querySelector('.field-error[data-for="' + input.id + '"]');
    if (el) el.textContent = '';
  }

  function isValidEmail(value) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
  }

  if (form) {
    form.addEventListener('submit', function (e) {
      e.preventDefault();

      const name = document.getElementById('cr-name');
      const email = document.getElementById('cr-email');
      const type = document.getElementById('cr-type');
      const date = document.getElementById('cr-date');
      const details = document.getElementById('cr-details');
      let valid = true;

      [name, email, type, details].forEach(clearError);
      status.textContent = '';
      status.className = 'form-status';

      if (name.value.trim().length < 2) { setError(name, 'Enter your name.'); valid = false; }
      if (!isValidEmail(email.value.trim())) { setError(email, 'Enter a valid email.'); valid = false; }
      if (!type.value) { setError(type, 'Choose a request type.'); valid = false; }
      if (details.value.trim().length < 5) { setError(details, 'Tell us a bit more.'); valid = false; }

      if (!valid) return;

      submitBtn.disabled = true;

      const body = new URLSearchParams();
      body.append('name', name.value);
      body.append('email', email.value);
      body.append('type', type.value);
      body.append('date', date.value);
      body.append('details', details.value);

      fetch('/api/concierge', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: body
      })
        .then(function (res) {
          if (res.status === 401) {
            status.textContent = 'Please sign in to send a concierge request.';
            status.className = 'form-status error';
            return null;
          }
          if (res.ok) return res.json();
          throw new Error('Request failed');
        })
        .then(function (data) {
          if (data === null) return;
          status.textContent = 'Request sent! Our concierge will be in touch shortly.';
          status.className = 'form-status success';
          form.reset();
        })
        .catch(function (err) {
          console.log(err);
          status.textContent = 'Something went wrong. Please try again.';
          status.className = 'form-status error';
        })
        .finally(function () {
          setTimeout(function () { submitBtn.disabled = false; }, 1500);
        });
    });
  }

});
