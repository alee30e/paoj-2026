package com.pao.project.model;

public class Installment {
    private String id;
//    private Loan
    private String dueDate;
    private Double amount;
    private Boolean paid;
    private String paidDate;
    private Boolean penaltyApplied;

    public Installment(String id, String dueDate, Double amount, Boolean paid,
                       String paidDate, Boolean penaltyApplied) {
        this.id = id;
        this.dueDate = dueDate;
        this.amount = amount;
        this.paid = paid;
        this.paidDate = paidDate;
        this.penaltyApplied = penaltyApplied;
    }

    public String getId() {
        return id;
    }

    public String getDueDate() {
        return dueDate;
    }

    public Double getAmount() {
        return amount;
    }

    public Boolean getPaid() {
        return paid;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public Boolean getPenaltyApplied() {
        return penaltyApplied;
    }
}

