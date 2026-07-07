document.addEventListener('DOMContentLoaded', function () {

  // helpers
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

  const tabLogin = document.getElementById('tab-login');
  const tabSignup = document.getElementById('tab-signup');
  const panelLogin = document.getElementById('panel-login');
  const panelSignup = document.getElementById('panel-signup');

  function showLogin() {
    tabLogin.classList.add('active');
    tabSignup.classList.remove('active');
    tabLogin.setAttribute('aria-selected', 'true');
    tabSignup.setAttribute('aria-selected', 'false');
    panelLogin.classList.remove('hidden');
    panelSignup.classList.add('hidden');
  }

  function showSignup() {
    tabSignup.classList.add('active');
    tabLogin.classList.remove('active');
    tabSignup.setAttribute('aria-selected', 'true');
    tabLogin.setAttribute('aria-selected', 'false');
    panelSignup.classList.remove('hidden');
    panelLogin.classList.add('hidden');
  }

  tabLogin.addEventListener('click', showLogin);
  tabSignup.addEventListener('click', showSignup);

  if (window.location.hash === '#signup') {
    showSignup();
  }

  const toggles = document.querySelectorAll('.toggle-pw');
  toggles.forEach(function (btn) {
    btn.addEventListener('click', function () {
      const input = document.getElementById(btn.dataset.target);
      if (input.type === 'password') {
        input.type = 'text';
        btn.textContent = 'Hide';
      } else {
        input.type = 'password';
        btn.textContent = 'Show';
      }
    });
  });


  const dob = document.getElementById('signup-dob');
  if (dob) {
    dob.max = new Date().toISOString().split('T')[0];
  }

  function getAge(value) {
    const birth = new Date(value);
    const today = new Date();
    let age = today.getFullYear() - birth.getFullYear();
    const months = today.getMonth() - birth.getMonth();
    if (months < 0 || (months === 0 && today.getDate() < birth.getDate())) {
      age = age - 1;
    }
    return age;
  }

  // login form
  const loginForm = document.getElementById('login-form');
  const loginStatus = document.getElementById('login-status');

  loginForm.addEventListener('submit', function (e) {
    e.preventDefault();

    const email = document.getElementById('login-email');
    const password = document.getElementById('login-password');
    let valid = true;

    clearError(email);
    clearError(password);
    loginStatus.textContent = '';
    loginStatus.className = 'form-status';

    if (!isValidEmail(email.value.trim())) {
      setError(email, 'Enter a valid email address.');
      valid = false;
    }

    if (password.value.length < 6) {
      setError(password, 'Password must be at least 6 characters.');
      valid = false;
    }

    if (!valid) return;

    loginStatus.textContent = 'Signed in successfully. Redirecting...';
    loginStatus.className = 'form-status success';
    setTimeout(function () {
      window.location.href = 'index.html';
    }, 900);
  });

  // sign up form
  const signupForm = document.getElementById('signup-form');
  const signupStatus = document.getElementById('signup-status');

  signupForm.addEventListener('submit', function (e) {
    e.preventDefault();

    const name = document.getElementById('signup-name');
    const email = document.getElementById('signup-email');
    const birth = document.getElementById('signup-dob');
    const password = document.getElementById('signup-password');
    const confirm = document.getElementById('signup-confirm');
    const terms = document.getElementById('agree-terms');
    let valid = true;

    clearError(name);
    clearError(email);
    clearError(birth);
    clearError(password);
    clearError(confirm);
    signupStatus.textContent = '';
    signupStatus.className = 'form-status';

    if (name.value.trim().length < 2) {
      setError(name, 'Enter your full name.');
      valid = false;
    }

    if (!isValidEmail(email.value.trim())) {
      setError(email, 'Enter a valid email address.');
      valid = false;
    }

    if (!birth.value) {
      setError(birth, 'Enter your date of birth.');
      valid = false;
    } else if (getAge(birth.value) < 18) {
      setError(birth, 'You must be at least 18 years old to register.');
      valid = false;
    }

    if (password.value.length < 8) {
      setError(password, 'Password must be at least 8 characters.');
      valid = false;
    }

    if (confirm.value === '' || confirm.value !== password.value) {
      setError(confirm, 'Passwords do not match.');
      valid = false;
    }

    if (!terms.checked) {
      signupStatus.textContent = 'Please accept the Terms and Privacy Policy.';
      signupStatus.className = 'form-status error';
      valid = false;
    }

    if (!valid) return;

    signupStatus.textContent = 'Account created. Redirecting...';
    signupStatus.className = 'form-status success';
    setTimeout(function () {
      window.location.href = 'index.html';
    }, 900);
  });

});