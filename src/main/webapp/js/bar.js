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

  // bar reservation
  const reservationForm = document.getElementById("bar-reservation-form");
  const statusMessage = document.getElementById("br-status");

  const dateInput = document.getElementById("br-date");

  if (dateInput) {
    dateInput.min = new Date().toISOString().split("T")[0];
  }

  function showError(input, message) {
    input.classList.add("invalid");

    const error = document.querySelector(
        `.field-error[data-for="${input.id}"]`
    );

    if (error) {
      error.textContent = message;
    }
  }

  function clearError(input) {
    input.classList.remove("invalid");

    const error = document.querySelector(
        `.field-error[data-for="${input.id}"]`
    );

    if (error) {
      error.textContent = "";
    }
  }

  if (reservationForm) {

    reservationForm.addEventListener("submit", (event) => {

      event.preventDefault();

      const name = document.getElementById("br-name");
      const guests = document.getElementById("br-guests");
      const date = document.getElementById("br-date");
      const time = document.getElementById("br-time");
      const notes = document.getElementById("br-notes");

      let isValid = true;

      [name, guests, date, time].forEach(clearError);

      statusMessage.textContent = "";
      statusMessage.className = "form-status";

      if (name.value.trim().length < 2) {
        showError(name, "Enter your name.");
        isValid = false;
      }

      if (!guests.value) {
        showError(guests, "Select number of guests.");
        isValid = false;
      }

      if (!date.value) {
        showError(date, "Choose a date.");
        isValid = false;
      }

      if (!time.value) {
        showError(time, "Choose a time.");
        isValid = false;
      }

      if (!isValid) {
        return;
      }

      const submitButton = reservationForm.querySelector(".form-submit");
      submitButton.disabled = true;

      const formData = new URLSearchParams();

      formData.append("name", name.value.trim());
      formData.append("guests", guests.value);
      formData.append("date", date.value);
      formData.append("time", time.value);
      formData.append("notes", notes.value);

      fetch("/api/reserve-bar-table", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded"
        },
        body: formData
      })
          .then(response => {
            return response.json().then(data => ({
              status: response.status,
              data: data
            }));
          })
          .then(result => {

            if (result.status === 200) {

              statusMessage.textContent =
                  "Table reserved! We will confirm by email.";
              statusMessage.className = "form-status success";

              reservationForm.reset();

            } else if (result.status === 401) {

              statusMessage.textContent =
                  "Please sign in to reserve a table.";
              statusMessage.className = "form-status error";

            } else {

              statusMessage.textContent =
                  result.data.error || "Could not complete reservation.";
              statusMessage.className = "form-status error";
            }

          })
          .catch(() => {

            statusMessage.textContent =
                "Something went wrong. Please try again.";
            statusMessage.className = "form-status error";

          })
          .finally(() => {
            submitButton.disabled = false;
          });
    });
  }

});