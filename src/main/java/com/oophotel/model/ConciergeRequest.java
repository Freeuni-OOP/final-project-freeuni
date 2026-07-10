package com.oophotel.model;

import java.time.LocalDate;

// A concierge request.
public class ConciergeRequest {

    private final int id;
    private final String name;
    private final String email;
    private final String type;
    private final LocalDate requestDate;
    private final String details;
    private final String status;

    public ConciergeRequest(int id, String name, String email, String type,
                            LocalDate requestDate, String details, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.type = type;
        this.requestDate = requestDate;
        this.details = details;
        this.status = status;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getType() { return type; }
    public LocalDate getRequestDate() { return requestDate; }
    public String getDetails() { return details; }
    public String getStatus() { return status; }
}
