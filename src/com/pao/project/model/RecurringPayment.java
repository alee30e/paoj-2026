package com.pao.project.model;

import java.time.LocalDate;

public class RecurringPayment {
    private static int nextId = 1;

    private String id;

    private Client client;
    private Account sourceAccount;
    private Account destinationAccount;

    private ServiceCategory serviceCategory;

    private Double amount;
    private Currency currency;
    private Frequency frequency;

    private LocalDate startDate;
    private LocalDate nextPaymentDate;
    private LocalDate endDate;

    private Boolean active;
    private String description;

    public RecurringPayment(Client client,
                            Account sourceAccount,
                            Account destinationAccount,
                            ServiceCategory serviceCategory,
                            Double amount,
                            Frequency frequency,
                            LocalDate startDate,
                            LocalDate endDate,
                            String description) {

        validateRecurringPayment(
                client,
                sourceAccount,
                destinationAccount,
                serviceCategory,
                amount,
                frequency,
                startDate
        );

        this.id = generateId();
        this.client = client;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.serviceCategory = serviceCategory;
        this.amount = amount;
        this.currency = sourceAccount.getCurrency();

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

    private void validateRecurringPayment(Client client,
                                          Account sourceAccount,
                                          Account destinationAccount,
                                          ServiceCategory serviceCategory,
                                          Double amount,
                                          Frequency frequency,
                                          LocalDate startDate) {

        if (client == null) {
            throw new IllegalArgumentException("Clientul nu poate fi null.");
        }

        if (sourceAccount == null) {
            throw new IllegalArgumentException("Contul sursa nu poate fi null.");
        }

        if (destinationAccount == null) {
            throw new IllegalArgumentException("Contul destinatie nu poate fi null.");
        }

        if (!sourceAccount.getOwner().equals(client)) {
            throw new IllegalArgumentException("Contul sursa nu apartine clientului.");
        }

        if (sourceAccount.equals(destinationAccount)) {
            throw new IllegalArgumentException("Contul sursa si contul destinatie nu pot fi acelasi.");
        }

        if (!sourceAccount.getCurrency().equals(destinationAccount.getCurrency())) {
            throw new IllegalArgumentException("Conturile trebuie sa aiba aceeasi valuta.");
        }

        if (serviceCategory == null) {
            throw new IllegalArgumentException("Categoria serviciului nu poate fi null.");
        }

        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Suma platii recurente trebuie sa fie pozitiva.");
        }

        if (frequency == null) {
            throw new IllegalArgumentException("Frecventa nu poate fi null.");
        }

        if (startDate == null) {
            throw new IllegalArgumentException("Data de inceput nu poate fi null.");
        }
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

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public ServiceCategory getServiceCategory() {
        return serviceCategory;
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

    public String getDestinationName() {
        return destinationAccount.getOwner().getDisplayName();
    }

    public String getSourceIban() {
        return sourceAccount.getIBAN();
    }

    public String getDestinationIban() {
        return destinationAccount.getIBAN();
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
                throw new IllegalArgumentException("Frecventa invalida.");
        }
    }

    @Override
    public String toString() {
        return "RecurringPayment{" +
                "id='" + id + '\'' +
                ", client=" + client.getDisplayName() +
                ", sourceAccount=" + sourceAccount.getIBAN() +
                ", destinationAccount=" + destinationAccount.getIBAN() +
                ", destinationName=" + getDestinationName() +
                ", serviceCategory=" + serviceCategory +
                ", amount=" + amount +
                ", currency=" + currency +
                ", frequency=" + frequency +
                ", startDate=" + startDate +
                ", nextPaymentDate=" + nextPaymentDate +
                ", endDate=" + endDate +
                ", active=" + active +
                ", description='" + description + '\'' +
                '}';
    }
}