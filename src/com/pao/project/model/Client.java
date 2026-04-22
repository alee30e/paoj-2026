package com.pao.project.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Client {
    private String id, address, email, phone;
    List<Account> conturi;

    public Client(String id, String address, String email, String phone) {
        this.id = id;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public List<Account> getConturi() {
        return conturi;
    }

    public abstract ClientType getClientType();
    public abstract String getDisplayName();
    public abstract String getIdentificationNumber();
}
