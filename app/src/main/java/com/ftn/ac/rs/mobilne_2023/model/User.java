package com.ftn.ac.rs.mobilne_2023.model;

import com.ftn.ac.rs.mobilne_2023.services.UserService;

public class User {

    private int id;
    private String email;
    private String username;
    private String password;
    private int tokens;
    private String tokensLastReceived;

    public User() { }

    public User(String email, String username, String password) {
        this.id = generateId();
        this.email = email;
        this.username = username;
        this.password = password;
        this.tokens = 5;
        this.tokensLastReceived = null;
    }

    public User(int id, String email, String username, String password, int tokens, String tokensLastReceived) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.tokens = tokens;
        this.tokensLastReceived = tokensLastReceived;
    }

    public static int generateId()
    {
        return UserService.getUserCount() + 1;
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

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public String getTokensLastReceived() {
        return tokensLastReceived;
    }

    public void setTokensLastReceived(String tokensLastReceived) {
        this.tokensLastReceived = tokensLastReceived;
    }
}
