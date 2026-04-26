package com.pao.project.service;

import com.pao.project.exception.UserNotFoundException;
import com.pao.project.model.*;
import com.pao.project.model.Currency;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountService {
    private List<Account> accounts;
    private Map<String, Account> accountsByIban;
    private static AccountService instance;

    private AccountService() {
        accounts = new ArrayList<>();
        accountsByIban = new HashMap<>();
    }

    public static AccountService getInstance() {
        if (instance == null)
            instance = new AccountService();
        return instance;
    }

    private String generateIban() {
        String countryCode = "RO";
        String checkDigits = "49";
        String bankCode = "PAOO";
        long timePart = System.currentTimeMillis();
        int randomPart = (int) (Math.random() * 9000) + 1000;
        return countryCode + checkDigits + bankCode + timePart + randomPart;
    }

    private String generateUniqueIban() {
        String iban;
        do {
            iban = generateIban();
        } while (accountsByIban.containsKey(iban));
        return iban;
    }

    public CurrentAccount addCurrentAccount(LocalDate openedDate, Currency currency, Double balance,
                                            Client owner, Double monthlyFee) {

        String iban = generateUniqueIban();
        CurrentAccount account = new CurrentAccount(iban, openedDate, currency, balance, owner, monthlyFee);

        accounts.add(account);
        accountsByIban.put(iban, account);
        owner.addAccount(account);
        return account;
    }
    public SavingsAccount addSavingsAccount(LocalDate openedDate, Currency currency, Double balance, Client owner,
                                            Double interestRate, Double minimumBalance, Double withdrawLimit) {
        String iban = generateUniqueIban();

        SavingsAccount account = new SavingsAccount(iban, openedDate, currency, balance, owner, interestRate, minimumBalance, withdrawLimit);

        accounts.add(account);
        accountsByIban.put(iban, account);
        owner.addAccount(account);
        return account;
    }
    public Account findByIban(String iban) {
        Account account = accountsByIban.get(iban);
        if (account == null) {
            throw new UserNotFoundException("Contul cu IBAN-ul " + iban + " nu exista");
        }
        return account;
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    public List<Account> getAccountsByClient(Client client) {
        List<Account> result = new ArrayList<>();

        for (Account account : accounts) {
            if (account.getOwner().equals(client)) {
                result.add(account);
            }
        }
        return result;
    }

    public void deposit(String iban, Double amount) {
        Account account = findByIban(iban);
        account.deposit(amount);
    }

    public void withdraw(String iban, Double amount) {
        Account account = findByIban(iban);
        account.withdraw(amount);
    }

    public void transfer(String sourceIban, String destinationIban, Double amount) {
        Account source = findByIban(sourceIban);
        Account destination = findByIban(destinationIban);
        source.withdraw(amount);
        destination.deposit(amount);
    }

    public void deleteAccount(String iban) {
        Account account = findByIban(iban);
        accounts.remove(account);
        accountsByIban.remove(iban);
    }
}