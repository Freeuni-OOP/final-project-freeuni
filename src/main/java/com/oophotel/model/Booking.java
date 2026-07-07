package com.oophotel.model;

import java.time.LocalDate;

// A room booking.
public class Booking {

    private final int id;
    private final int roomId;
    private final int userId;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final String status;

    public Booking(int id, int roomId, int userId, LocalDate checkIn, LocalDate checkOut, String status) {
        this.id = id;
        this.roomId = roomId;
        this.userId = userId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
    }

    public int getId() { return id; }
    public int getRoomId() { return roomId; }
    public int getUserId() { return userId; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public String getStatus() { return status; }
}
