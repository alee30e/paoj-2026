package com.pao.project.model;

public class Transaction {
    private  String id, sourceIban, destinationIban, amount, currency, type, timestamp, description;

    public Transaction(String id, String sourceIban, String amount, String destinationIban, String currency, String type, String timestamp, String description) {
        this.id = id;
        this.sourceIban = sourceIban;
        this.amount = amount;
        this.destinationIban = destinationIban;
        this.currency = currency;
        this.type = type;
        this.timestamp = timestamp;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getSourceIban() {
        return sourceIban;
    }

    public String getDestinationIban() {
        return destinationIban;
    }

    public String getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getCurrency() {
        return currency;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }
}
