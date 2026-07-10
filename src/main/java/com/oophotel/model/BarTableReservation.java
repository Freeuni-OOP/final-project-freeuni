package com.oophotel.model;

import java.sql.Date;
import java.sql.Time;


public class BarTableReservation {

    private int id;
    private int userId;
    private String fullName;
    private int guests;
    private Date reservationDate;
    private Time reservationTime;
    private String notes;


    public BarTableReservation() {
    }

    public BarTableReservation(int userId, String fullName, int guests,
                               Date reservationDate, Time reservationTime,
                               String notes) {

        this.userId = userId;
        this.fullName = fullName;
        this.guests = guests;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Time getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(Time reservationTime) {
        this.reservationTime = reservationTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}