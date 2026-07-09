document.addEventListener('DOMContentLoaded', function () {

  // menu
  const menu = [
    { name: 'Badrijani', price: '₾14', category: 'Starters', description: 'Fried aubergine rolls with walnut paste.' },
    { name: 'Adjarian Khachapuri', price: '₾18', category: 'Starters', description: 'Boat-shaped cheese bread with a soft egg.' },
    { name: 'Georgian Salad', price: '₾12', category: 'Starters', description: 'Tomato, cucumber, walnut and fresh herbs.' },
    { name: 'Khinkali (5 pcs)', price: '₾15', category: 'Mains', description: 'Soup dumplings with spiced beef and pork.' },
    { name: 'Mtsvadi', price: '₾28', category: 'Mains', description: 'Charcoal-grilled pork skewers.' },
    { name: 'Chakhokhbili', price: '₾24', category: 'Mains', description: 'Chicken stew with tomato and herbs.' },
    { name: 'Grilled Trout', price: '₾26', category: 'Mains', description: 'Local trout with lemon and herbs.' },
    { name: 'Churchkhela', price: '₾8', category: 'Desserts', description: 'Walnuts dipped in thickened grape must.' },
    { name: 'Honey Cake', price: '₾10', category: 'Desserts', description: 'Layered medovik with cream.' }
  ];

  const menuEl = document.getElementById('dining-menu');
  if (menuEl) {
    menuEl.innerHTML = menu.map(function (item) {
      return '<div class="menu-item">' +
        '<div class="menu-item-top">' +
        '<span class="menu-item-name">' + item.name + '</span>' +
        '<span class="menu-item-price">' + item.price + '</span>' +
        '</div>' +
        '<p class="menu-item-desc">' + item.description + '</p>' +
        '<span class="menu-item-tag">' + item.category + '</span>' +
        '</div>';
    }).join('');
  }

  // reservation form
  const form = document.getElementById('dining-reservation-form');
  const status = document.getElementById('dr-status');

  const dateInput = document.getElementById('dr-date');
  if (dateInput) {
    dateInput.min = new Date().toISOString().split('T')[0];
  }

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

  if (form) {
    form.addEventListener('submit', function (e) {
      e.preventDefault();

      const name = document.getElementById('dr-name');
      const guests = document.getElementById('dr-guests');
      const date = document.getElementById('dr-date');
      const time = document.getElementById('dr-time');
      let valid = true;

      [name, guests, date, time].forEach(clearError);
      status.textContent = '';
      status.className = 'form-status';

      if (name.value.trim().length < 2) { setError(name, 'Enter your name.'); valid = false; }
      if (!guests.value) { setError(guests, 'Select number of guests.'); valid = false; }
      if (!date.value) { setError(date, 'Choose a date.'); valid = false; }
      if (!time.value) { setError(time, 'Choose a time.'); valid = false; }

      if (!valid) return;

      status.textContent = 'Table reserved! We look forward to seeing you.';
      status.className = 'form-status success';
      form.reset();
    });
  }

});
