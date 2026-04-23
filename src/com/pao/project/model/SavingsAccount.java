package com.pao.project.model;

import com.pao.project.exception.InvalidSavingsWithdrawException;

public class SavingsAccount extends Account{
    private Double interestRate, minimumBalance, withdrawalLimit;
    public SavingsAccount(String id, String IBAN, String openedDate, Currency currency, Double balance, Client owner, Double interestRate, Double minimumBalance, Double withdrawalLimit){
        super(id, IBAN, openedDate, currency, balance, owner);
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
        this.withdrawalLimit = withdrawalLimit;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public Double getMinimumBalance() {
        return minimumBalance;
    }

    public Double getWithdrawalLimit() {
        return withdrawalLimit;
    }

    public AccountType getAccountType(){
        return AccountType.SAVINGS;
    }
    public void validateSavingsWithdraw(Double sum){
        if ( balance - sum < 100) throw new InvalidSavingsWithdrawException("Nu se pot scoate bani, suma ar ramane < 100");
    }
    public void withdraw(Double sum){
        validateAmount(sum);
        validateSavingsWithdraw(sum);
        balance -= sum;
    }
}
