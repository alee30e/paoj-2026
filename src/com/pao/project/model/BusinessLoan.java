package com.pao.project.model;

import java.time.LocalDate;

public class BusinessLoan extends Loan {
    private Double declaredMonthlyRevenue;
    private Double declaredMonthlyExpenses;
    private Double maxAllowedDebtRatio;

    public BusinessLoan(Client client, Double requestedAmount, String linkedAccountIBAN, int numberOfMonths,
                        Frequency frequency, LocalDate startDate, Double interestRate, Double declaredMonthlyRevenue,
                        Double declaredMonthlyExpenses, Double maxAllowedDebtRatio) {

        super(client, requestedAmount, linkedAccountIBAN,
                numberOfMonths, frequency, startDate, interestRate);

        this.declaredMonthlyRevenue = declaredMonthlyRevenue;
        this.declaredMonthlyExpenses = declaredMonthlyExpenses;
        this.maxAllowedDebtRatio = maxAllowedDebtRatio;
    }

    public Double getDeclaredMonthlyRevenue() {
        return declaredMonthlyRevenue;
    }

    public Double getDeclaredMonthlyExpenses() {
        return declaredMonthlyExpenses;
    }

    public Double getMaxAllowedDebtRatio() {
        return maxAllowedDebtRatio;
    }

    public Double calculateDeclaredProfit() {
        return declaredMonthlyRevenue - declaredMonthlyExpenses;
    }

    @Override
    public LoanType getLoanType() {
        return LoanType.BUSINESS;
    }

    @Override
    public boolean isEligible() {
        if (!(getClient() instanceof BusinessClient)) {
            return false;
        }

        Double profit = calculateDeclaredProfit();
        Double installmentValue = calculateInstallmentValue();

        return profit > 0 && installmentValue <= profit * maxAllowedDebtRatio;
    }

    @Override
    public String toString() {
        return "BusinessLoan{" + "id='" + getId() + '\'' + ", loanNumber='" + getLoanNumber() + '\'' +
                ", client='" + getClient().getDisplayName() + '\'' + ", linkedAccountIBAN='" + getLinkedAccountIBAN() + '\'' +
                ", requestedAmount=" + getRequestedAmount() + ", interestRate=" + getInterestRate() +
                ", numberOfMonths=" + getNumberOfMonths() + ", frequency=" + getFrequency() +
                ", remainingAmount=" + getRemainingAmount() + ", startDate=" + getStartDate() + ", endDate=" + getEndDate() +
                ", status=" + getStatus() + ", declaredMonthlyRevenue=" + declaredMonthlyRevenue +
                ", declaredMonthlyExpenses=" + declaredMonthlyExpenses + ", declaredProfit=" + calculateDeclaredProfit() +
                ", maxAllowedDebtRatio=" + maxAllowedDebtRatio + '}';
    }
}