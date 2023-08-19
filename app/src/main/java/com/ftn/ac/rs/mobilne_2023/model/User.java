package com.ftn.ac.rs.mobilne_2023.model;

public class User {

    private int id;
    private String email;
    private String username;
    private String password;

    public User() { }

    public User(String email, String username, String password) {
        this.id = generateId();
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(int id, String email, String username, String password) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    /*GENERATOR JEDINSTVENIH ID-a*/
    private static int idCounter = 1;

    public static int generateId()
    {
        return ++idCounter;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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
}
