package com.pao.project.model;

import java.time.LocalDate;

public class RecurringPayment {
    private static int nextId = 1;

    private String id;
    private Client client;
    private Account sourceAccount;
    private Merchant merchant;
    private Double amount;
    private Currency currency;
    private Frequency frequency;
    private LocalDate startDate;
    private LocalDate nextPaymentDate;
    private LocalDate endDate;
    private Boolean active;
    private String description;

    public RecurringPayment(Client client, Account sourceAccount, Merchant merchant, Double amount,
                            Currency currency, Frequency frequency, LocalDate startDate, LocalDate endDate,
                            String description) {
        this.id = generateId();
        this.client = client;
        this.sourceAccount = sourceAccount;
        this.merchant = merchant;
        this.amount = amount;
        this.currency = currency;
        this.frequency = frequency;
        this.startDate = startDate;
        this.nextPaymentDate = startDate;
        this.endDate = endDate;
        this.active = true;
        this.description = description;
    }

    private static String generateId() {
        return "RP_" + nextId++;
    }

    public String getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public Double getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getNextPaymentDate() {
        return nextPaymentDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Boolean getActive() {
        return active;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    public boolean isDue(LocalDate today) {
        if (!active) {
            return false;
        }

        if (endDate != null && today.isAfter(endDate)) {
            return false;
        }

        return !today.isBefore(nextPaymentDate);
    }

    public void updateNextPaymentDate() {
        switch (frequency) {
            case WEEKLY:
                nextPaymentDate = nextPaymentDate.plusWeeks(1);
                break;
            case MONTHLY:
                nextPaymentDate = nextPaymentDate.plusMonths(1);
                break;
            case YEARLY:
                nextPaymentDate = nextPaymentDate.plusYears(1);
                break;
            default:
                throw new IllegalArgumentException("Frecventa invalida");
        }
    }

    @Override
    public String toString() {
        return "RecurringPayment{" + "id='" + id + '\'' + ", client=" + client.getDisplayName() + ", sourceAccount=" + sourceAccount.getIBAN() +
                ", merchant=" + merchant.getName() + ", amount=" + amount + ", currency=" + currency + ", frequency=" + frequency + ", nextPaymentDate=" + nextPaymentDate +", active=" + active +
                ", description='" + description + '\'' + '}';
    }
}