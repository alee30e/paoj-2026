package com.pao.project.model;

import com.pao.project.exception.InvalidUsernameException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Client {
    private String id, address, email, phone;
    private Set<Account> accounts;
    private static int nextId = 0;

    public Client(String address, String email, String phone) {
        validateEmail(email);
        validatePhone(phone);
        this.id = generateId();
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.accounts = new HashSet<>();
    }
    public void setId(String id) {
        this.id = id;
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

    public Set<Account> getAccounts() {
        return accounts;
    }
    private static String generateId(){
        return "Client_" + nextId++;
    }
    public void validateEmail(String email){
        String regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(regexEmail)) throw new InvalidUsernameException("Email invalid!");
    }

    public void validatePhone(String phone){
        String regexPhone = "^07\\d{8}$";
        if (!phone.matches(regexPhone)) throw new InvalidUsernameException("Nr de telefon invalid!");
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
    }

    public abstract ClientType getClientType();
    public abstract String getDisplayName();
    public abstract String getIdentificationNumber();

    @Override
    public String toString() {
        return "Client{" + "id='" + id + '\'' + ", type=" + getClientType() + ", name='" + getDisplayName() + '\'' +
                ", identification='" + getIdentificationNumber() + '\'' + ", email='" + email + '\'' +
                ", phone='" + phone + '\'' + ", address='" + address + '\'' + ", numberOfAccounts=" + accounts.size() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return getId().equals(client.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
