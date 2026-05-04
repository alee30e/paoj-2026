package com.pao.project.service;

import com.pao.project.exception.InvalidAmountException;
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
    private TransactionService transactionService;
    private AccountService() {
        accounts = new ArrayList<>();
        accountsByIban = new HashMap<>();
        transactionService = TransactionService.getInstance();
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
    public Account findClientAccountByIban(Client client, String iban) {
        Account account = findByIban(iban);

        if (!account.getOwner().equals(client)) {
            throw new UserNotFoundException("Acest cont nu apartine clientului conectat.");
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
        transactionService.addTransaction(
                new Transaction(null, iban, amount,
                        account.getCurrency(), TransactionType.DEPOSIT,
                        "Depunere bani in cont"));
    }

    public void depositForClient(Client client, String iban, Double amount) {
        Account account = findClientAccountByIban(client, iban);
//        account.deposit(amount);
        deposit(iban, amount);
    }

    public void withdraw(String iban, Double amount) {
        Account account = findByIban(iban);
        account.withdraw(amount);

        transactionService.addTransaction(
                new Transaction(iban, null, amount,
                        account.getCurrency(), TransactionType.WITHDRAW,
                        "Retragere bani din cont"));
    }

    public void withdrawForClient(Client client, String iban, Double amount) {
        Account account = findClientAccountByIban(client, iban);
//        account.withdraw(amount);
        withdraw(iban, amount);
    }
    public void withdrawLoanPayment(String iban, Double amount) {
        Account account = findByIban(iban);
        account.withdraw(amount);

        transactionService.addTransaction(
                new Transaction(iban, null, amount,
                        account.getCurrency(), TransactionType.LOAN_PAYMENT,
                        "Plata rata credit"));
    }

    public void recurringPaymentTransfer(String sourceIban, String destinationIban, Double amount, String description) {
        Account source = findByIban(sourceIban);
        Account destination = findByIban(destinationIban);

        validateTransfer(source, destination);

        source.withdraw(amount);
        destination.deposit(amount);

        transactionService.addTransaction(
                new Transaction(sourceIban, destinationIban, amount,
                        source.getCurrency(), TransactionType.RECURRING_PAYMENT, description));
    }

    private void validateTransfer(Account source, Account destination) {
        if (source.equals(destination)) {
            throw new InvalidAmountException("Nu poti transfera bani in acelasi cont.");
        }

        if (!source.getCurrency().equals(destination.getCurrency())) {
            throw new InvalidAmountException("Transferul intre conturi cu valute diferite nu este permis.");
        }
    }

    public void transfer(String sourceIban, String destinationIban, Double amount) {
        Account source = findByIban(sourceIban);
        Account destination = findByIban(destinationIban);

        validateTransfer(source, destination);
        source.withdraw(amount);
        destination.deposit(amount);

        transactionService.addTransaction(
                new Transaction(sourceIban, destinationIban, amount,
                        source.getCurrency(), TransactionType.TRANSFER,
                        "Transfer intre conturi"));
    }

    public void transferFromClient(Client client, String sourceIban, String destinationIban, Double amount) {
        Account source = findClientAccountByIban(client, sourceIban);
//        Account destination = findByIban(destinationIban);
//
//        validateTransfer(source, destination);
//
//        source.withdraw(amount);
//        destination.deposit(amount);
        transfer(sourceIban, destinationIban, amount);
    }

    public void deleteAccount(String iban) {
        Account account = findByIban(iban);
        accounts.remove(account);
        accountsByIban.remove(iban);
        account.getOwner().removeAccount(account);
    }
    public void depositLoanAmount(String iban, Double amount) {
        Account account = findByIban(iban);
        account.deposit(amount);

        transactionService.addTransaction(
                new Transaction(null, iban, amount,
                        account.getCurrency(), TransactionType.LOAN_DISBURSEMENT,
                        "Virare suma credit aprobat"));
    }
}