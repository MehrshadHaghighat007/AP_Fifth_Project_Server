package org.example.model;

import java.util.ArrayList;

import java.util.List;


public class User {
    private String username;
    private String hashedPassword;
    private List<String> files = new ArrayList<>();

    public User(String username, String hashedPassword) {
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public List<String> getFiles() {
        return files;
    }
}
