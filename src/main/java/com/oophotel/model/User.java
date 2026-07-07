package com.oophotel.model;

import java.sql.Date;
import java.sql.Timestamp;

public class User {

    private long      id;
    private String    firstName;
    private String    lastName;
    private String    email;
    private String    passwordHash;
    private Date      dateOfBirth;
    private String    role;
    private Timestamp createdAt;

    public User() {}

    public long      getId()           { return id; }
    public void      setId(long id)    { this.id = id; }

    public String    getFirstName()                  { return firstName; }
    public void      setFirstName(String firstName)  { this.firstName = firstName; }

    public String    getLastName()                   { return lastName; }
    public void      setLastName(String lastName)    { this.lastName = lastName; }

    public String    getEmail()                      { return email; }
    public void      setEmail(String email)          { this.email = email; }

    public String    getPasswordHash()                         { return passwordHash; }
    public void      setPasswordHash(String passwordHash)      { this.passwordHash = passwordHash; }

    public Date      getDateOfBirth()                          { return dateOfBirth; }
    public void      setDateOfBirth(Date dateOfBirth)          { this.dateOfBirth = dateOfBirth; }

    public String    getRole()                       { return role; }
    public void      setRole(String role)            { this.role = role; }

    public Timestamp getCreatedAt()                            { return createdAt; }
    public void      setCreatedAt(Timestamp createdAt)         { this.createdAt = createdAt; }
}
