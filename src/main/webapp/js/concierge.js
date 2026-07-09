document.addEventListener('DOMContentLoaded', function () {

  const form = document.getElementById('concierge-form');
  const status = document.getElementById('cr-status');

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

      status.textContent = 'Request sent! Our concierge will be in touch shortly.';
      status.className = 'form-status success';
      form.reset();
    });
  }

});
