package com.pao.laboratory11.exercise1;

public class Tranzactie {
    int id;
    double amount;
    String date;
    String country;
    String channel;
    String accountId;

    public Tranzactie(int id, double amount, String date, String country, String channel) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.country = country;
        this.channel = channel;
    }

    public Tranzactie(int id, double amount, String date, String country, String channel, String accountId) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.country = country;
        this.channel = channel;
        this.accountId = accountId;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getCountry() {
        return country;
    }

    public String getChannel() {
        return channel;
    }
    public String getAccountId() {
        return accountId;
    }
}