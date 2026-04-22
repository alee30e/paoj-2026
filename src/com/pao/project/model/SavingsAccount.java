package com.pao.project.model;

import com.pao.project.exception.InvalidaSavingsWithdrawException;

public class SavingsAccount extends Account{
    private Double interestRate, minimumBalance, withdrawalLimit;
    public SavingsAccount(String id, String IBAN, String openedDate, Currency currency, Double balance, Client owner, Double interestRate, Double minimumBalance, Double withdrawalLimit){
        super(id, IBAN, openedDate, currency, balance, owner);
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
        this.withdrawalLimit = withdrawalLimit;
    }
    public AccountType getAccountType(){
        return AccountType.SAVINGS;
    }
    public void validateSavingsWithdraw(Double sum, Double balance){
        if (sum - balance < 100) throw new InvalidaSavingsWithdrawException("Nu se pot scoate bani, suma ar ramane < 100");
    }
    public void withdraw(Double sum){
        validateAmount(sum);
        validateSavingsWithdraw(sum, balance);
        balance -= sum;
    }
}
