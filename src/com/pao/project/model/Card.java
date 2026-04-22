package com.pao.project.model;

public class Card {
    private String id, cardNumber, holderName, expirationDate, cvv, status, accountIban;

    public Card(String id, String cardNumber, String expirationDate, String holderName, String cvv, String accountIban, String status) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.holderName = holderName;
        this.cvv = cvv;
        this.accountIban = accountIban;
        this.status = status;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getId() {
        return id;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getCvv() {
        return cvv;
    }

    public String getStatus() {
        return status;
    }

    public String getAccountIban() {
        return accountIban;
    }

    public String getHolderName() {
        return holderName;
    }
}
