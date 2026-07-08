document.addEventListener("DOMContentLoaded", function() {
    const checkInInput = document.getElementById('check-in-date');
    const checkOutInput = document.getElementById('check-out-date');

    fetch('/api/bookings')
        .then(response => {
            if (response.status === 401) {
                document.getElementById('auth-warning').classList.remove('hidden-content');
            } else {
                document.getElementById('booking-content').classList.remove('hidden-content');
                setupCalendarLimits();
            }
        })
        .catch(err => {
            document.getElementById('auth-warning').classList.remove('hidden-content');
        });

    function setupCalendarLimits() {
        const today = new Date().toISOString().split('T')[0];
        checkInInput.min = today;
        checkOutInput.min = today;

        checkInInput.addEventListener('change', function() {
            if (checkInInput.value) {
                const nextDay = new Date(checkInInput.value);
                nextDay.setDate(nextDay.getDate() + 1);

                const nextDayFormatted = nextDay.toISOString().split('T')[0];
                checkOutInput.min = nextDayFormatted;

                if (checkOutInput.value && checkOutInput.value <= checkInInput.value) {
                    checkOutInput.value = nextDayFormatted;
                }
            }
        });
    }
});

// ოთახის ტიპის არჩევა და 10 ნომრის დინამიური გენერაცია
function selectRoomType(type, startNum, endNum) {
    document.querySelectorAll('.room-type-card').forEach(card => card.classList.remove('selected'));
    document.getElementById('card-' + type).classList.add('selected');

    const roomSelect = document.getElementById('room-select');
    roomSelect.innerHTML = '<option value="">-- Choose specific room --</option>';
    roomSelect.disabled = false;

    for (let i = startNum; i <= endNum; i++) {
        const option = document.createElement('option');
        option.value = i;
        option.textContent = `Room ${i} (${type.toUpperCase()})`;
        roomSelect.appendChild(option);
    }
}

// ფორმის გაგზავნა
document.getElementById('hotel-booking-form').addEventListener('submit', function(e) {
    e.preventDefault();

    const statusBox = document.getElementById('status-box');
    statusBox.style.display = 'none';

    const bookingData = {
        roomId: parseInt(document.getElementById('room-select').value),
        checkIn: document.getElementById('check-in-date').value,
        checkOut: document.getElementById('check-out-date').value
    };

    fetch('/api/bookings', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(bookingData)
    })
        .then(async response => {
            const data = await response.json();

            if (response.ok) {
                statusBox.className = 'status-msg status-success';
                statusBox.innerHTML = '✨ <strong>Success!</strong> Booking placed successfully. ID: ' + data.id;
                statusBox.style.display = 'block';
                document.getElementById('hotel-booking-form').reset();
                document.getElementById('room-select').disabled = true;
                document.getElementById('room-select').innerHTML = '<option value="">-- Select a room type on the left first --</option>';
                document.querySelectorAll('.room-type-card').forEach(card => card.classList.remove('selected'));
            } else {
                let cleanErrorMessage = 'An unexpected error occurred. Please try again.';
                if (data && data.error) {
                    cleanErrorMessage = data.error;
                } else if (typeof data === 'string') {
                    cleanErrorMessage = data;
                }
                throw new Error(cleanErrorMessage);
            }
        })
        .catch(error => {
            statusBox.className = 'status-msg status-error';
            statusBox.innerHTML = '⚠️ <strong>Notice:</strong> ' + error.message;
            statusBox.style.display = 'block';
        });
});