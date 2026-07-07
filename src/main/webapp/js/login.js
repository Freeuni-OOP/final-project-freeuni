document.addEventListener('DOMContentLoaded', function () {

  // ── Helpers ────────────────────────────────────────────────────────────────

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

  function setStatus(el, message, type) {
    el.textContent = message;
    el.className = 'form-status ' + (type || '');
  }

  function setLoading(btn, loading) {
    btn.disabled = loading;
    btn.textContent = loading ? 'Please wait…' : btn.dataset.label;
  }

  // ── Tab switching ───────────────────────────────────────────────────────────

  const tabLogin  = document.getElementById('tab-login');
  const tabSignup = document.getElementById('tab-signup');
  const panelLogin  = document.getElementById('panel-login');
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

  if (window.location.hash === '#signup') showSignup();

  // ── Password toggles ────────────────────────────────────────────────────────

  document.querySelectorAll('.toggle-pw').forEach(function (btn) {
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

  // ── Date-of-birth: cap max at today ────────────────────────────────────────

  const dob = document.getElementById('signup-dob');
  if (dob) dob.max = new Date().toISOString().split('T')[0];

  function getAge(value) {
    const birth = new Date(value);
    const today = new Date();
    let age = today.getFullYear() - birth.getFullYear();
    const m = today.getMonth() - birth.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birth.getDate())) age--;
    return age;
  }

  // Store original button labels so we can restore them after loading
  document.querySelectorAll('.form-submit').forEach(function (btn) {
    btn.dataset.label = btn.textContent;
  });

  // ── Login form ──────────────────────────────────────────────────────────────

  const loginForm   = document.getElementById('login-form');
  const loginStatus = document.getElementById('login-status');
  const loginBtn    = loginForm.querySelector('.form-submit');

  loginForm.addEventListener('submit', async function (e) {
    e.preventDefault();

    const email    = document.getElementById('login-email');
    const password = document.getElementById('login-password');
    let valid = true;

    clearError(email);
    clearError(password);
    setStatus(loginStatus, '', '');

    if (!isValidEmail(email.value.trim())) {
      setError(email, 'Enter a valid email address.');
      valid = false;
    }
    if (password.value.length < 6) {
      setError(password, 'Password must be at least 6 characters.');
      valid = false;
    }
    if (!valid) return;

    setLoading(loginBtn, true);

    try {
      const res = await fetch('api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'same-origin',
        body: JSON.stringify({
          email:    email.value.trim().toLowerCase(),
          password: password.value
        })
      });

      const json = await res.json();

      if (res.ok) {
        // Save first name for the home page greeting
        sessionStorage.setItem('user', JSON.stringify(json.data));
        setStatus(loginStatus, 'Signed in successfully. Redirecting…', 'success');
        setTimeout(function () { window.location.href = 'index.html'; }, 900);
      } else {
        const msg = json.error || 'Sign in failed. Please try again.';
        if (res.status === 401) {
          setStatus(loginStatus, 'Incorrect email or password.', 'error');
        } else {
          setStatus(loginStatus, msg, 'error');
        }
        setLoading(loginBtn, false);
      }
    } catch (err) {
      setStatus(loginStatus, 'Network error — could not reach the server.', 'error');
      setLoading(loginBtn, false);
    }
  });

  // ── Sign-up form ────────────────────────────────────────────────────────────

  const signupForm   = document.getElementById('signup-form');
  const signupStatus = document.getElementById('signup-status');
  const signupBtn    = signupForm.querySelector('.form-submit');

  signupForm.addEventListener('submit', async function (e) {
    e.preventDefault();

    const nameEl    = document.getElementById('signup-name');
    const email     = document.getElementById('signup-email');
    const birth     = document.getElementById('signup-dob');
    const password  = document.getElementById('signup-password');
    const confirm   = document.getElementById('signup-confirm');
    const terms     = document.getElementById('agree-terms');
    let valid = true;

    [nameEl, email, birth, password, confirm].forEach(clearError);
    setStatus(signupStatus, '', '');

    if (nameEl.value.trim().length < 2) {
      setError(nameEl, 'Enter your full name.');
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
      setStatus(signupStatus, 'Please accept the Terms and Privacy Policy.', 'error');
      valid = false;
    }
    if (!valid) return;

    // Split "Full Name" into firstName / lastName (everything after first space is lastName)
    const parts     = nameEl.value.trim().split(/\s+/);
    const firstName = parts[0];
    const lastName  = parts.slice(1).join(' ') || parts[0]; // fallback: use same if single word

    setLoading(signupBtn, true);

    try {
      const res = await fetch('api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'same-origin',
        body: JSON.stringify({
          firstName:   firstName,
          lastName:    lastName,
          email:       email.value.trim().toLowerCase(),
          password:    password.value,
          dateOfBirth: birth.value   // YYYY-MM-DD
        })
      });

      const json = await res.json();

      if (res.ok) {
        sessionStorage.setItem('user', JSON.stringify(json.data));
        setStatus(signupStatus, 'Account created! Redirecting…', 'success');
        setTimeout(function () { window.location.href = 'index.html'; }, 900);
      } else {
        const msg = json.error || 'Registration failed. Please try again.';
        if (res.status === 409) {
          setStatus(signupStatus, 'This email address is already registered.', 'error');
        } else {
          setStatus(signupStatus, msg, 'error');
        }
        setLoading(signupBtn, false);
      }
    } catch (err) {
      setStatus(signupStatus, 'Network error — could not reach the server.', 'error');
      setLoading(signupBtn, false);
    }
  });

});
