package com.pao.project.model;

import java.time.LocalDateTime;

public class Transaction {
    private static int nextId = 1;

    private final String id;
    private final String sourceIban;
    private final String destinationIban;
    private final Double amount;
    private final Currency currency;
    private final TransactionType type;
    private final LocalDateTime timestamp;
    private final String description;

    public Transaction(String sourceIban, String destinationIban, Double amount,
                       Currency currency, TransactionType type, String description) {
        this.id = generateId();
        this.sourceIban = sourceIban;
        this.destinationIban = destinationIban;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.description = description;
    }

    private static String generateId() {
        return "TX_" + nextId++;
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

    public Double getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Transaction{" + "id='" + id + '\'' + ", sourceIban='" + sourceIban + '\'' +
                ", destinationIban='" + destinationIban + '\'' + ", amount=" + amount + ", currency=" + currency +
                ", type=" + type + ", timestamp=" + timestamp + ", description='" + description + '\'' + '}';
    }
}