package com.pao.project.service;

import com.pao.project.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecurringPaymentService {
    private static RecurringPaymentService instance;

    private List<RecurringPayment> recurringPayments;
    private Map<String, RecurringPayment> recurringPaymentsById;

    private AccountService accountService;
    private TransactionService transactionService;

    private RecurringPaymentService() {
        recurringPayments = new ArrayList<>();
        recurringPaymentsById = new HashMap<>();
        accountService = AccountService.getInstance();
        transactionService = TransactionService.getInstance();
    }

    public static RecurringPaymentService getInstance() {
        if (instance == null) {
            instance = new RecurringPaymentService();
        }
        return instance;
    }

    public RecurringPayment addRecurringPayment(Client client, Account sourceAccount, Merchant merchant, Double amount,
                                                Currency currency, Frequency frequency, LocalDate startDate, LocalDate endDate,
                                                String description) {

        RecurringPayment recurringPayment = new RecurringPayment(client, sourceAccount, merchant, amount,currency,
                frequency, startDate, endDate, description);

        recurringPayments.add(recurringPayment);
        recurringPaymentsById.put(recurringPayment.getId(), recurringPayment);

        return recurringPayment;
    }

    public RecurringPayment findById(String id) {
        RecurringPayment payment = recurringPaymentsById.get(id);

        if (payment == null) {
            throw new RuntimeException("Plata recurenta cu id-ul " + id + " nu exista");
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
        RecurringPayment payment = findById(id);

        if (!payment.isDue(today)) {
            throw new IllegalStateException("Plata nu este scadenta astazi");
        }

        if (!payment.getMerchant().isActive()) {
            throw new IllegalStateException("Merchantul nu este activ");
        }

        Account source = payment.getSourceAccount();
        Account destination = payment.getMerchant().getSettlementAccount();

        accountService.transfer(source.getIBAN(), destination.getIBAN(), payment.getAmount());

        Transaction transaction = new Transaction(source.getIBAN(), destination.getIBAN(),
                payment.getAmount(), payment.getCurrency(), TransactionType.RECURRING_PAYMENT,
                payment.getDescription());

        transactionService.addTransaction(transaction);

        payment.updateNextPaymentDate();
    }

    public void executeDuePayments(LocalDate today) {
        for (RecurringPayment payment : recurringPayments) {
            if (payment.isDue(today) && payment.getMerchant().isActive()) {
                Account source = payment.getSourceAccount();
                Account destination = payment.getMerchant().getSettlementAccount();

                accountService.transfer(source.getIBAN(), destination.getIBAN(), payment.getAmount());

                Transaction transaction = new Transaction(source.getIBAN(), destination.getIBAN(),
                        payment.getAmount(), payment.getCurrency(), TransactionType.RECURRING_PAYMENT,
                        payment.getDescription());

                transactionService.addTransaction(transaction);

                payment.updateNextPaymentDate();
            }
        }
    }
}