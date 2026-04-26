package com.pao.project.model;

import java.time.LocalDate;

public class CurrentAccount extends Account{
    private Double monthlyFee; //overdraftLimit
    public CurrentAccount(String IBAN, LocalDate openedDate, Currency currency, Double balance, Client owner, Double monthlyFee){
        super(IBAN, openedDate, currency, balance, owner);
        this.monthlyFee = monthlyFee;
    }

    public Double getMonthlyFee() {
        return monthlyFee;
    }

    public AccountType getAccountType(){
        return AccountType.CURRENT;
    }

    public void withdraw(Double sum){
        validateAmount(sum);
        balance -= sum;
    }
}
