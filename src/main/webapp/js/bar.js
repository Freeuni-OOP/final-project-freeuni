document.addEventListener('DOMContentLoaded', function () {

  // drinks menu
  const menu = [
    { name: 'Old Town Negroni', price: '₾24', category: 'Cocktails', description: 'Gin, Campari, sweet vermouth, orange peel.' },
    { name: 'Kakheti Spritz', price: '₾22', category: 'Cocktails', description: 'Georgian sparkling wine, Aperol, soda.' },
    { name: 'Rustaveli Sour', price: '₾26', category: 'Cocktails', description: 'Chacha, lemon, honey, egg white.' },
    { name: 'Espresso Martini', price: '₾25', category: 'Cocktails', description: 'Vodka, coffee liqueur, fresh espresso.' },
    { name: 'Saperavi Glass', price: '₾18', category: 'Wine', description: 'Dry Georgian red, qvevri-aged.' },
    { name: 'Rkatsiteli Glass', price: '₾16', category: 'Wine', description: 'Amber white, crisp and mineral.' },
    { name: 'Highland Whisky', price: '₾30', category: 'Spirits', description: '12-year single malt, neat or on ice.' },
    { name: 'Craft Lager', price: '₾12', category: 'Beer', description: 'Local unfiltered draught.' }
  ];

  const menuEl = document.getElementById('bar-menu');
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

  // upcoming events
  const events = [
    { day: '12', month: 'Jul', title: 'Jazz Trio Night', description: 'Live jazz from 21:00, no cover charge.' },
    { day: '19', month: 'Jul', title: 'Georgian Wine Tasting', description: 'Six qvevri wines with a sommelier. ₾60 per guest.' },
    { day: '26', month: 'Jul', title: 'DJ & Cocktails', description: 'House and disco sets until late.' }
  ];

  const eventsEl = document.getElementById('bar-events');
  if (eventsEl) {
    eventsEl.innerHTML = events.map(function (ev) {
      return '<div class="event-card">' +
        '<div class="event-date">' +
        '<div class="event-day">' + ev.day + '</div>' +
        '<div class="event-month">' + ev.month + '</div>' +
        '</div>' +
        '<div class="event-info">' +
        '<h3>' + ev.title + '</h3>' +
        '<p>' + ev.description + '</p>' +
        '</div>' +
        '</div>';
    }).join('');
  }

  // reservation form
  const form = document.getElementById('bar-reservation-form');
  const status = document.getElementById('br-status');

  const dateInput = document.getElementById('br-date');
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

      const name = document.getElementById('br-name');
      const guests = document.getElementById('br-guests');
      const date = document.getElementById('br-date');
      const time = document.getElementById('br-time');
      let valid = true;

      [name, guests, date, time].forEach(clearError);
      status.textContent = '';
      status.className = 'form-status';

      if (name.value.trim().length < 2) { setError(name, 'Enter your name.'); valid = false; }
      if (!guests.value) { setError(guests, 'Select number of guests.'); valid = false; }
      if (!date.value) { setError(date, 'Choose a date.'); valid = false; }
      if (!time.value) { setError(time, 'Choose a time.'); valid = false; }

      if (!valid) return;

      status.textContent = 'Table reserved! We will confirm by email.';
      status.className = 'form-status success';
      form.reset();
    });
  }

});
