package com.pao.project.model;

public class CurrentAccount extends Account{
    private Double monthlyFee; //overdraftLimit
    public CurrentAccount(String id, String IBAN, String openedDate, Currency currency, Double balance, Client owner, Double monthlyFee){
        super(id, IBAN, openedDate, currency, balance, owner);
        this.monthlyFee = monthlyFee;
    }
    public AccountType getAccountType(){
        return AccountType.CURRENT;
    }

    public void withdraw(Double sum){
        validateAmount(sum);
        balance -= sum;
    }
}
