package com.pao.project.model;

import java.util.List;

public class Loan {
    private String id;
    private String loanNumber;
    private Client client;
    private String linkedAccountIBAN;
    private Double requestedAmount;
    private Double interestRate;
    private int numberOfMonths;
    private Frequency frequency;
    private Double remainingAmount;
    private String startDate, endDate;
    private String status;
    private List<Installment> installments;

    public Loan(String id, String loanNumber, Client client, Double requestedAmount, String linkedAccountIBAN, int numberOfMonths,
                Double remainingAmount, Frequency frequency, String startDate, String endDate, String status, Double interestRate) {
        this.id = id;
        this.loanNumber = loanNumber;
        this.client = client;
        this.requestedAmount = requestedAmount;
        this.linkedAccountIBAN = linkedAccountIBAN;
        this.numberOfMonths = numberOfMonths;
        this.remainingAmount = remainingAmount;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.interestRate = interestRate;
    }

    public String getId() {
        return id;
    }

    public String getLoanNumber() {
        return loanNumber;
    }

    public Client getClient() {
        return client;
    }

    public String getLinkedAccountIBAN() {
        return linkedAccountIBAN;
    }

    public Double getRequestedAmount() {
        return requestedAmount;
    }

    public int getNumberOfMonths() {
        return numberOfMonths;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public Double getRemainingAmount() {
        return remainingAmount;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }
    public Double getInterestRate(){
        return interestRate;
    }

//    public int calculateInstallment(){
//
//    }
}
