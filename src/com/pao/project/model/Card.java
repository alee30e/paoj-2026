package com.pao.project.model;

public class Card {
    private String id;
    private String cardNumber, holderName, expirationDate, cvv, accountIban;
    private CardStatus status;
    private static int nextId = 1;
    public Card(String id, String cardNumber, String expirationDate, String holderName, String cvv, String accountIban, CardStatus status) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.holderName = holderName;
        this.cvv = cvv;
        this.accountIban = accountIban;
        this.status = status;
    }
    public String generateId(){
        return "Card_" + nextId++;
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

    public CardStatus getStatus() {
        return status;
    }

    public String getAccountIban() {
        return accountIban;
    }

    public String getHolderName() {
        return holderName;
    }

    private String maskCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }

        String last4 = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + last4;
    }
    @Override
    public String toString() {
        return "Card{" + "id='" + id + '\'' + ", cardNumber='" + maskCardNumber() + '\'' +
                ", holderName='" + holderName + '\'' + ", expirationDate='" + expirationDate + '\'' +
                ", status='" + status + '\'' + ", accountIban='" + accountIban + '\'' + '}';
    }
}
