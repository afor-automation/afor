package nz.co.afor.framework.model;

import nz.co.afor.framework.Encrypted;

import java.time.ZonedDateTime;

/**
 * Created by Matt Belcher on 11/01/2016.
 */
public class Customer {
    private String username;

    @Encrypted
    private String password;
    private ZonedDateTime dateOfBirth;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ZonedDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(ZonedDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
