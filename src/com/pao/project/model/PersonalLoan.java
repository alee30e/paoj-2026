package com.pao.project.model;

import java.time.LocalDate;

public class PersonalLoan extends Loan {
    private Double declaredMonthlyIncome;
    private Double maxAllowedDebtRatio;

    public PersonalLoan(Client client, Double requestedAmount, String linkedAccountIBAN, int numberOfMonths,
                        Frequency frequency, LocalDate startDate, Double interestRate, Double declaredMonthlyIncome,
                        Double maxAllowedDebtRatio) {

        super(client, requestedAmount, linkedAccountIBAN,
                numberOfMonths, frequency, startDate, interestRate);

        this.declaredMonthlyIncome = declaredMonthlyIncome;
        this.maxAllowedDebtRatio = maxAllowedDebtRatio;
    }

    public Double getDeclaredMonthlyIncome() {
        return declaredMonthlyIncome;
    }

    public Double getMaxAllowedDebtRatio() {
        return maxAllowedDebtRatio;
    }

    @Override
    public LoanType getLoanType() {
        return LoanType.PERSONAL;
    }

    @Override
    public boolean isEligible() {
        if (!(getClient() instanceof IndividualClient)) {
            return false;
        }

        Double installmentValue = calculateInstallmentValue();

        return declaredMonthlyIncome != null && declaredMonthlyIncome > 0
                && installmentValue <= declaredMonthlyIncome * maxAllowedDebtRatio;
    }
}