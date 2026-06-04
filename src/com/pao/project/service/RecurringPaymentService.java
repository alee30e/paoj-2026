package com.pao.project.service;

import com.pao.project.model.Account;
import com.pao.project.model.Client;
import com.pao.project.model.Frequency;
import com.pao.project.model.RecurringPayment;
import com.pao.project.model.ServiceCategory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecurringPaymentService {
    private static RecurringPaymentService instance;

    private final List<RecurringPayment> recurringPayments;
    private final Map<String, RecurringPayment> recurringPaymentsById;

    private final AccountService accountService;

    private RecurringPaymentService() {
        recurringPayments = new ArrayList<>();
        recurringPaymentsById = new HashMap<>();
        accountService = AccountService.getInstance();
    }

    public static RecurringPaymentService getInstance() {
        if (instance == null) {
            instance = new RecurringPaymentService();
        }
        return instance;
    }

    public RecurringPayment addRecurringPayment(Client client,
                                                Account sourceAccount,
                                                Account destinationAccount,
                                                ServiceCategory serviceCategory,
                                                Double amount,
                                                Frequency frequency,
                                                LocalDate startDate,
                                                LocalDate endDate,
                                                String description) {

        RecurringPayment recurringPayment = new RecurringPayment(
                client,
                sourceAccount,
                destinationAccount,
                serviceCategory,
                amount,
                frequency,
                startDate,
                endDate,
                description
        );

        recurringPayments.add(recurringPayment);
        recurringPaymentsById.put(recurringPayment.getId(), recurringPayment);

        return recurringPayment;
    }

    public RecurringPayment addRecurringPaymentByIbans(Client client,
                                                       String sourceIban,
                                                       String destinationIban,
                                                       ServiceCategory serviceCategory,
                                                       Double amount,
                                                       Frequency frequency,
                                                       LocalDate startDate,
                                                       LocalDate endDate,
                                                       String description) {

        Account sourceAccount = accountService.findClientAccountByIban(client, sourceIban);
        Account destinationAccount = accountService.findByIban(destinationIban);

        return addRecurringPayment(
                client,
                sourceAccount,
                destinationAccount,
                serviceCategory,
                amount,
                frequency,
                startDate,
                endDate,
                description
        );
    }

    public RecurringPayment findById(String id) {
        RecurringPayment payment = recurringPaymentsById.get(id);

        if (payment == null) {
            throw new RuntimeException("Plata recurenta cu id-ul " + id + " nu exista.");
        }

        return payment;
    }

    public List<RecurringPayment> getAllRecurringPayments() {
        return new ArrayList<>(recurringPayments);
    }

    public List<RecurringPayment> getRecurringPaymentsByClient(Client client) {
        List<RecurringPayment> result = new ArrayList<>();

        for (RecurringPayment payment : recurringPayments) {
            if (payment.getClient().getId().equals(client.getId())) {
                result.add(payment);
            }
        }

        return result;
    }

    public void deactivateRecurringPayment(String id) {
        RecurringPayment payment = findById(id);
        payment.deactivate();
    }

    public void activateRecurringPayment(String id) {
        RecurringPayment payment = findById(id);
        payment.activate();
    }

    public void deleteRecurringPayment(String id) {
        RecurringPayment payment = findById(id);

        recurringPayments.remove(payment);
        recurringPaymentsById.remove(id);
    }

    public void executePayment(String id, LocalDate today) {
        if (today == null) {
            throw new IllegalArgumentException("Data curenta nu poate fi null.");
        }

        RecurringPayment payment = findById(id);

        if (!payment.isDue(today)) {
            throw new IllegalStateException("Plata nu este scadenta astazi.");
        }

        accountService.recurringPaymentTransfer(
                payment.getSourceAccount().getIBAN(),
                payment.getDestinationAccount().getIBAN(),
                payment.getAmount(),
                payment.getDescription()
        );

        payment.updateNextPaymentDate();
    }

    public void executeDuePayments(LocalDate today) {
        if (today == null) {
            throw new IllegalArgumentException("Data curenta nu poate fi null.");
        }

        for (RecurringPayment payment : recurringPayments) {
            try {
                if (payment.isDue(today)) {
                    accountService.recurringPaymentTransfer(
                            payment.getSourceAccount().getIBAN(),
                            payment.getDestinationAccount().getIBAN(),
                            payment.getAmount(),
                            payment.getDescription()
                    );

                    payment.updateNextPaymentDate();
                }
            } catch (RuntimeException e) {
                System.out.println("Plata recurenta " + payment.getId()
                        + " nu a putut fi executata: " + e.getMessage());
            }
        }
    }
}