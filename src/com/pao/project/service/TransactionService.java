package com.pao.project.service;

import com.pao.project.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionService {
    private static TransactionService instance;
    private List<Transaction> transactions;

    private TransactionService() {
        transactions = new ArrayList<>();
    }

    public static TransactionService getInstance() {
        if (instance == null)
            instance = new TransactionService();
        return instance;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    public List<Transaction> getTransactionsByIban(String iban) {
        List<Transaction> result = new ArrayList<>();

        for (Transaction t : transactions) {
            if ((t.getSourceIban() != null && t.getSourceIban().equals(iban)) ||
                    (t.getDestinationIban() != null && t.getDestinationIban().equals(iban))) {
                result.add(t);
            }
        }

        return result;
    }
}