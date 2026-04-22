package com.pao.project.model;

import com.pao.project.exception.InvalidAmountException;

public abstract class Account {
    private String id, IBAN, openedDate;
    protected Double balance;
    private Client owner;
    private Currency currency;

    public Account(String id, String IBAN, String openedDate, Currency currency, Double balance, Client owner){
        this.id = id;
        this.IBAN = IBAN;
        this.openedDate = openedDate;
        this.currency = currency;
        this.balance = balance;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public String getIBAN() {
        return IBAN;
    }

    public String getOpenedDate() {
        return openedDate;
    }

    public Double getBalance() {
        return balance;
    }

    public Client getOwner() {
        return owner;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void validateAmount(Double sum){
        if (sum <= 0) throw new InvalidAmountException("Suma trebuie sa fie pozitiva");
        if (balance - sum < 0) throw new InvalidAmountException("Fonduri insufieciente");
    }
    public void deposit(Double sum){
        validateAmount(sum);
        balance += sum;
        System.out.println("S-au adaugat "+ sum + "in cont. Suma totala:" + balance);
    }
    public abstract void withdraw(Double sum);
    public abstract AccountType getAccountType();
}
