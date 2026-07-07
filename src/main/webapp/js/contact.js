document.addEventListener('DOMContentLoaded', function () {

  function setError(input, message) {
    input.classList.add('invalid');
    const error = document.querySelector('.field-error[data-for="' + input.id + '"]');
    if (error) error.textContent = message;
  }

  function clearError(input) {
    input.classList.remove('invalid');
    const error = document.querySelector('.field-error[data-for="' + input.id + '"]');
    if (error) error.textContent = '';
  }

  function isValidEmail(value) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
  }

  const form = document.getElementById('contact-form');
  const status = document.getElementById('contact-status');
  const submitBtn = form.querySelector('.form-submit');

  form.addEventListener('submit', function (e) {
    e.preventDefault();

    const name = document.getElementById('cf-name');
    const email = document.getElementById('cf-email');
    const subject = document.getElementById('cf-subject');
    const message = document.getElementById('cf-message');
    let valid = true;

    clearError(name);
    clearError(email);
    clearError(subject);
    clearError(message);
    status.textContent = '';
    status.className = 'form-status';

    if (name.value.trim().length < 2) {
      setError(name, 'Enter your name.');
      valid = false;
    }

    if (!isValidEmail(email.value.trim())) {
      setError(email, 'Enter a valid email address.');
      valid = false;
    }

    if (!subject.value) {
      setError(subject, 'Choose a subject.');
      valid = false;
    }

    if (message.value.trim().length < 10) {
      setError(message, 'Message should be at least 10 characters.');
      valid = false;
    }

    if (!valid) return;

    submitBtn.disabled = true;
    status.textContent = 'Message sent. We will reply within one business day.';
    status.className = 'form-status success';
    form.reset();

    setTimeout(function () {
      submitBtn.disabled = false;
    }, 1500);
  });

});