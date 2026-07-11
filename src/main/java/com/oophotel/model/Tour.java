package com.oophotel.model;

public class Tour {

    private int id;
    private String name;
    private String description;
    private int maxGuests;

    public Tour() {
    }

    public Tour(int id, String name, String description, int maxGuests) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxGuests = maxGuests;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }
}