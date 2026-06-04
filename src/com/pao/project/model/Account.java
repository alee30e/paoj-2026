package com.pao.project.model;

import com.pao.project.exception.InvalidAmountException;

import java.time.LocalDate;

public abstract class Account implements Comparable<Account> {
    private String id, IBAN;
    private LocalDate openedDate;
    protected Double balance;
    private Client owner;
    private Currency currency;
    private static int nextId = 1;

    public Account(String IBAN, LocalDate openedDate, Currency currency, Double balance, Client owner){
        this.id = generateId();
        this.IBAN = IBAN;
        this.openedDate = openedDate;
        this.currency = currency;
        this.balance = balance;
        this.owner = owner;
    }
    private static String generateId(){
        return "ACC_" + nextId++;
    }

    public void setId(String id) { this.id = id; }

    public void setBalance(Double balance) { this.balance = balance; }

    public String getId() {
        return id;
    }

    public String getIBAN() {
        return IBAN;
    }

    public LocalDate getOpenedDate() {
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
        validatePositiveAmount(sum);
        if (balance - sum < 0) throw new InvalidAmountException("Fonduri insufieciente");
    }
    public void validatePositiveAmount(Double sum){
        if (sum <= 0) throw new InvalidAmountException("Suma trebuie sa fie pozitiva");
    }
    public void deposit(Double sum){
        validatePositiveAmount(sum);
        balance += sum;
        System.out.println("S-au adaugat "+ sum + "in cont. Suma totala:" + balance);
    }
    public abstract void withdraw(Double sum);
    public abstract AccountType getAccountType();

//    @Override
//    public String toString() {
//        return "Account{" + "id='" + id + '\'' + ", IBAN='" + IBAN + '\'' +
//                ", type=" + getAccountType() + ", owner='" + owner.getDisplayName() + '\'' +
//                ", balance=" + balance + ", currency=" + currency +
//                ", openedDate=" + openedDate + '}';
//    }
    @Override
    public String toString() {
        return id + " | " + IBAN + " | " + getAccountType() + " | " + balance;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return IBAN.equals(account.IBAN);
    }

    @Override
    public int hashCode() {
        return IBAN.hashCode();
    }
    @Override
    public int compareTo(Account o){
        return Double.compare(o.getBalance(), this.getBalance());
    }
}
