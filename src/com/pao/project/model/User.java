package com.pao.project.model;

public class User {
    private String id, username, password, role, clientId;

    public User(String id, String username, String password, String role, String clientId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.clientId = clientId;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getClientId() {
        return clientId;
    }
}
