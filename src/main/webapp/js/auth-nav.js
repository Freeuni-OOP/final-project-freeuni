/**
 * Checks whether the user has an active server session (GET /api/auth/me).
 * If logged in  → replaces the "Sign in" button with "Hello, <name>" + Logout.
 * If not logged in → keeps the "Sign in" button.
 */
(async function () {
  const navUser = document.getElementById('navbar-user');
  if (!navUser) return;

  try {
    const res = await fetch('api/auth/me', { credentials: 'same-origin' });
    if (!res.ok) return; // not logged in — keep default "Sign in" button

    const json = await res.json();
    const user = json.data;
    const name = user.firstName || user.email;

    navUser.innerHTML =
      '<span class="navbar-greeting">Hello, ' + escapeHtml(name) + '</span>' +
      '<button class="navbar-booking navbar-logout" id="nav-logout-btn">Log out</button>';

    document.getElementById('nav-logout-btn').addEventListener('click', async function () {
      await fetch('api/auth/logout', { method: 'POST', credentials: 'same-origin' });
      sessionStorage.removeItem('user');
      window.location.reload();
    });

  } catch (_) {
    // Network error or server down — silently keep default state
  }

  function escapeHtml(str) {
    return String(str)
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;');
  }
})();
