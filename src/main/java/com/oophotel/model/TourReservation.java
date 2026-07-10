package com.oophotel.model;

import java.sql.Date;

public class TourReservation {

    private int id;
    private int userId;
    private int tourId;
    private String fullName;
    private String email;
    private int guests;
    private Date tourDate;
    private String specialRequests;

    public TourReservation() {
    }

    public TourReservation(int userId, int tourId, String fullName, String email,
                           int guests, Date tourDate, String specialRequests) {

        this.userId = userId;
        this.tourId = tourId;
        this.fullName = fullName;
        this.email = email;
        this.guests = guests;
        this.tourDate = tourDate;
        this.specialRequests = specialRequests;
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

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public Date getTourDate() {
        return tourDate;
    }

    public void setTourDate(Date tourDate) {
        this.tourDate = tourDate;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }
}