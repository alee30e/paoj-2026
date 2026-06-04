package com.pao.project.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Loan {
    private String id;
    private String loanNumber;
    private Client client;
    private String linkedAccountIBAN;
    private Double requestedAmount;
    private Double interestRate;
    private int numberOfMonths;
    private Frequency frequency;
    private Double remainingAmount;
    private LocalDate startDate, endDate;
    private LoanStatus status;
    private List<Installment> installments;

//    private static int nextId = 1;
    private static int nextLoanNumber = 1;

//    public Loan(String id, String loanNumber, Client client, Double requestedAmount, String linkedAccountIBAN, int numberOfMonths,
//                Double remainingAmount, Frequency frequency, LocalDate startDate, LocalDate endDate, LoanStatus status, Double interestRate) {
//        this.id = id;
//        this.loanNumber = loanNumber;
//        this.client = client;
//        this.requestedAmount = requestedAmount;
//        this.linkedAccountIBAN = linkedAccountIBAN;
//        this.numberOfMonths = numberOfMonths;
//        this.remainingAmount = remainingAmount;
//        this.frequency = frequency;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.status = status;
//        this.interestRate = interestRate;
//        this.installments = new ArrayList<>();
//    }
    protected Loan(Client client, Double requestedAmount, String linkedAccountIBAN, int numberOfMonths, Frequency frequency,
                   LocalDate startDate, Double interestRate) {

//        this.id = generateId();
        this.id = null;
        this.loanNumber = generateLoanNumber();
        this.client = client;
        this.requestedAmount = requestedAmount;
        this.linkedAccountIBAN = linkedAccountIBAN;
        this.numberOfMonths = numberOfMonths;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = startDate.plusMonths(numberOfMonths);
        this.interestRate = interestRate;
        this.status = LoanStatus.PENDING;
        this.remainingAmount = calculateTotalAmount();
        this.installments = new ArrayList<>();
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public Double getInterestRate(){
        return interestRate;
    }

//    public static int getNextId() {
//        return nextId;
//    }

    public void setInstallments(List<Installment> installments) {
        this.installments = installments;
    }

    public static void setNextLoanNumber(int nextLoanNumber) {
        Loan.nextLoanNumber = nextLoanNumber;
    }

//    public static void setNextId(int nextId) {
//        Loan.nextId = nextId;
//    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setRemainingAmount(Double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setNumberOfMonths(int numberOfMonths) {
        this.numberOfMonths = numberOfMonths;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public void setLinkedAccountIBAN(String linkedAccountIBAN) {
        this.linkedAccountIBAN = linkedAccountIBAN;
    }

    public void setRequestedAmount(Double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setLoanNumber(String loanNumber) {
        this.loanNumber = loanNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Installment> getInstallments() {
        return installments;
    }

    public static int getNextLoanNumber() {
        return nextLoanNumber;
    }

//    private static String generateId() {
//        return "LOAN_" + nextId++;
//    }

    private static String generateLoanNumber() {
        return "LN_" + nextLoanNumber++;
    }

    public Double calculateTotalAmount(){
        Double total = requestedAmount + requestedAmount * interestRate;
        return total;
    }
    public int calculateNumberOfInstallment(){
        switch (frequency){
            case MONTHLY : return numberOfMonths;
            case YEARLY : return (int) Math.ceil(numberOfMonths / 12.0);
            case WEEKLY: return numberOfMonths * 4;
            default:
                throw new IllegalArgumentException("Frecventa invalida:" + frequency);
        }
    }
    public double calculateInstallmentValue(){
        return calculateTotalAmount() / calculateNumberOfInstallment();
    }
    public List<Installment> generateInstallments(){
        List<Installment> generatedInstallments = new ArrayList<>();

        int numberOfInstallments = calculateNumberOfInstallment();
        double installmentValue = calculateInstallmentValue();

//        LocalDate dueDate = startDate;
        for(int i = 1; i <= numberOfInstallments; i++){
            LocalDate dueDate;
            switch(frequency){
                case MONTHLY : dueDate = startDate.plusMonths(i); break;
                case WEEKLY : dueDate = startDate.plusWeeks(i); break;
                case YEARLY : dueDate = startDate.plusYears(i); break;
                default: throw new IllegalArgumentException("Frecventa invalida");
            }
            Installment installment = new Installment(dueDate, installmentValue);
            generatedInstallments.add(installment);
        }
        this.installments = generatedInstallments;
        return generatedInstallments;
    }

    public Installment getNextUnpaidInstallment() {
        for (Installment installment : installments) {
            if (!installment.getPaid()) {
                return installment;
            }
        }
        return null;
    }

    public void markNextInstallmentAsPaid(LocalDate paidDate) {
        Installment installment = getNextUnpaidInstallment();

        if (installment == null) {
            status = LoanStatus.CLOSED;
            return;
        }

        installment.markAsPaid(paidDate);
        remainingAmount -= installment.getAmount();

        if (remainingAmount <= 0) {
            remainingAmount = 0.0;
            status = LoanStatus.CLOSED;
        }
    }

    public boolean isActive() {
        return status == LoanStatus.ACTIVE;
    }


    @Override
    public String toString() {
        return "Loan{" + "id='" + id + '\'' + ", loanNumber='" + loanNumber + '\'' + ", client=" + client.getDisplayName() +
                ", linkedAccountIBAN='" + linkedAccountIBAN + '\'' + ", requestedAmount=" + requestedAmount + ", interestRate=" + interestRate +
                ", numberOfMonths=" + numberOfMonths + ", frequency=" + frequency + ", remainingAmount=" + remainingAmount +
                ", startDate=" + startDate + ", endDate=" + endDate + ", status=" + status + ", loanType=" + getLoanType() + '}';
    }

    public abstract LoanType getLoanType();
    public abstract boolean isEligible();

}
