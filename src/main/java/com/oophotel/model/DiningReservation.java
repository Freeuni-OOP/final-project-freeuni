package com.oophotel.model;

import java.time.LocalDate;
import java.time.LocalTime;

// A dining table reservation.
public class DiningReservation {

    private final int id;
    private final String name;
    private final String guests;
    private final LocalDate date;
    private final LocalTime time;
    private final String notes;
    private final String status;

    public DiningReservation(int id, String name, String guests, LocalDate date,
                             LocalTime time, String notes, String status) {
        this.id = id;
        this.name = name;
        this.guests = guests;
        this.date = date;
        this.time = time;
        this.notes = notes;
        this.status = status;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getGuests() { return guests; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getNotes() { return notes; }
    public String getStatus() { return status; }
}
