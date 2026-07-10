package com.oophotel.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class SpaReservation {

    private final int id;
    private final String name;
    private final String email;
    private final String treatment;
    private final LocalDate date;
    private final LocalTime time;
    private final int guests;
    private final String notes;
    private final String status;

    public SpaReservation(int id, String name, String email, String treatment,
                          LocalDate date, LocalTime time, int guests, String notes, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.treatment = treatment;
        this.date = date;
        this.time = time;
        this.guests = guests;
        this.notes = notes;
        this.status = status;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getTreatment() { return treatment; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public int getGuests() { return guests; }
    public String getNotes() { return notes; }
    public String getStatus() { return status; }
}
