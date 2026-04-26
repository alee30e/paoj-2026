package com.pao.project.model;

import com.pao.project.exception.InvalidSavingsWithdrawException;

import java.time.LocalDate;

public class SavingsAccount extends Account{
    private Double interestRate, minimumBalance, withdrawalLimit;
    public SavingsAccount(String IBAN, LocalDate openedDate, Currency currency, Double balance, Client owner, Double interestRate, Double minimumBalance, Double withdrawalLimit){
        super(IBAN, openedDate, currency, balance, owner);
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
        if (sum > withdrawalLimit) {
            throw new InvalidSavingsWithdrawException("Suma depaseste limita de retragere pentru contul de economii.");
        }
        if ( balance - sum < minimumBalance) throw new InvalidSavingsWithdrawException("Nu se pot scoate bani, suma ar ramane < "+ minimumBalance);
    }
    public void withdraw(Double sum){
        validateAmount(sum);
        validateSavingsWithdraw(sum);
        balance -= sum;
    }
    @Override
    public String toString() {
        return "SavingsAccount{" + "id='" + getId() + '\'' + ", IBAN='" + getIBAN() + '\'' + ", owner='" + getOwner().getDisplayName() + '\'' +
                ", balance=" + getBalance() + ", currency=" + getCurrency() + ", openedDate=" + getOpenedDate() +
                ", interestRate=" + interestRate + ", minimumBalance=" + minimumBalance + ", withdrawalLimit=" + withdrawalLimit + '}';
    }
}
