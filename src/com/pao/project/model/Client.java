package com.pao.project.model;

import com.pao.project.exception.InvalidUsernameException;

import java.util.ArrayList;
import java.util.List;

public abstract class Client {
    private String id, address, email, phone;
    private List<Account> conturi;

    public Client(String id, String address, String email, String phone) {
        validateEmail(email);
        validatePhone(phone);
        this.id = id;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.conturi = new ArrayList<>();
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
    public void validateEmail(String email){
        String regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(regexEmail)) throw new InvalidUsernameException("Email invalid!");
    }
    public void validatePhone(String phone){
        String regexPhone = "^07\\d{8}$";
        if (!phone.matches(regexPhone)) throw new InvalidUsernameException("Nr de telefon invalid!");
    }

    public abstract ClientType getClientType();
    public abstract String getDisplayName();
    public abstract String getIdentificationNumber();
}
