package com.pao.project.model;

import java.time.LocalDate;

public class Installment {
    private String id;
//    private Loan
    private LocalDate dueDate;
    private Double amount;
    private Boolean paid;
    private LocalDate paidDate;
    private Boolean penaltyApplied;
    private InstallmentStatus status;
    private static int nextId = 1;

    public Installment(LocalDate dueDate, Double amount) {
        this.id = generateId();
        this.dueDate = dueDate;
        this.amount = amount;
        this.paid = false;
        this.paidDate = null;
        this.penaltyApplied = false;
        this.status = InstallmentStatus.PENDING;
    }
    public Installment(String id, LocalDate dueDate, Double amount, Boolean paid) {
        this.id = id;
        this.dueDate = dueDate;
        this.amount = amount;
        this.paid = paid;
        this.paidDate = null;
        this.penaltyApplied = false;
        this.status = paid ? InstallmentStatus.PAID : InstallmentStatus.PENDING;
    }
    public Installment(String id, LocalDate dueDate, Double amount, Boolean paid,
                       LocalDate paidDate, Boolean penaltyApplied) {
        this.id = id;
        this.dueDate = dueDate;
        this.amount = amount;
        this.paid = paid;
        this.paidDate = paidDate;
        this.penaltyApplied = penaltyApplied;
    }
//    public Installment(String id, LocalDate dueDate, Double amount, Boolean paid) {
//        this.id = id;
//        this.dueDate = dueDate;
//        this.amount = amount;
//        this.paid = paid;
////        this.paidDate = paidDate;
//    }
    private static String generateId(){
        return "INST" + nextId++;
    }

    public String getId() {
        return id;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Double getAmount() {
        return amount;
    }

    public Boolean getPaid() {
        return paid;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public Boolean getPenaltyApplied() {
        return penaltyApplied;
    }

    public InstallmentStatus getStatus() {
        return status;
    }

    public void markAsPaid(LocalDate paidDate) {
        this.paid = true;
        this.paidDate = paidDate;
        this.status = InstallmentStatus.PAID;
    }

    public Boolean isOverdue(LocalDate today){
        return !paid && today.isAfter(dueDate);
    }

    public void markAsOverdue(){
        if (!paid) {
            this.status = InstallmentStatus.OVERDUE;
        }
    }

    public void applyPenalty(Double penaltyAmount){
        if (!penaltyApplied){
            amount += penaltyAmount;
            penaltyApplied = true;
        }
    }
    @Override
    public String toString() {
        return "Installment{" + "id='" + id + '\'' + ", dueDate=" + dueDate +
                ", amount=" + amount + ", paid=" + paid + ", paidDate=" + paidDate +
                ", penaltyApplied=" + penaltyApplied + ", status=" + status + '}';
    }


}

