package com.oophotel.model;

import java.math.BigDecimal;

// A hotel room.
public class Room {

    private final int id;
    private final String type;
    private final String name;
    private final String description;
    private final BigDecimal pricePerNight;
    private final int capacity;

    public Room(int id, String type, String name, String description, BigDecimal pricePerNight, int capacity) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPricePerNight() { return pricePerNight; }
    public int getCapacity() { return capacity; }
}
