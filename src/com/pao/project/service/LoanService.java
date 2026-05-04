package com.pao.project.service;

import com.pao.project.exception.LoanNotFoundException;
import com.pao.project.model.*;
import com.pao.project.model.Frequency;
import com.pao.project.model.InstallmentStatus;
import com.pao.project.model.LoanStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoanService {
    private static LoanService instance;

    private List<Loan> loans;
    private Map<String, Loan> loansById;

    private AccountService accountService;

    private LoanService() {
        loans = new ArrayList<>();
        loansById = new HashMap<>();
        accountService = AccountService.getInstance();
    }

    public static LoanService getInstance() {
        if (instance == null) {
            instance = new LoanService();
        }
        return instance;
    }

    public PersonalLoan createPersonalLoan(Client client, Double requestedAmount, String linkedAccountIBAN,
                                           int numberOfMonths,Frequency frequency, Double interestRate,
                                           Double declaredMonthlyIncome) {
        accountService.findClientAccountByIban(client, linkedAccountIBAN);
        PersonalLoan loan = new PersonalLoan(client, requestedAmount, linkedAccountIBAN, numberOfMonths, frequency,
                LocalDate.now(), interestRate, declaredMonthlyIncome, 0.40);

        processLoanApplication(loan);

        loans.add(loan);
        loansById.put(loan.getId(), loan);

        return loan;
    }

    public BusinessLoan createBusinessLoan(Client client, Double requestedAmount, String linkedAccountIBAN,
                                           int numberOfMonths, Frequency frequency, Double interestRate,
                                           Double declaredMonthlyRevenue, Double declaredMonthlyExpenses) {
        accountService.findClientAccountByIban(client, linkedAccountIBAN);

        BusinessLoan loan = new BusinessLoan(client, requestedAmount, linkedAccountIBAN, numberOfMonths,
                frequency, LocalDate.now(), interestRate, declaredMonthlyRevenue, declaredMonthlyExpenses, 0.35);

        processLoanApplication(loan);

        loans.add(loan);
        loansById.put(loan.getId(), loan);

        return loan;
    }

    private void processLoanApplication(Loan loan) {
        if (loan.isEligible()) {
            approveLoan(loan);
        } else {
            rejectLoan(loan);
        }
    }

    private void approveLoan(Loan loan) {
        loan.setStatus(LoanStatus.APPROVED);

        loan.generateInstallments();

        accountService.depositLoanAmount(
                loan.getLinkedAccountIBAN(),
                loan.getRequestedAmount()
        );

        loan.setStatus(LoanStatus.ACTIVE);
    }

    private void rejectLoan(Loan loan) {
        loan.setStatus(LoanStatus.REJECTED);
    }

    public Loan findById(String id) {
        Loan loan = loansById.get(id);

        if (loan == null) {
            throw new LoanNotFoundException("Imprumutul cu id-ul " + id + " nu exista");
        }

        return loan;
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    public List<Loan> getLoansByClient(Client client) {
        List<Loan> result = new ArrayList<>();

        for (Loan loan : loans) {
            if (loan.getClient().getId().equals(client.getId())) {
                result.add(loan);
            }
        }

        return result;
    }

    public List<Loan> getActiveLoansByClient(Client client) {
        List<Loan> result = new ArrayList<>();

        for (Loan loan : loans) {
            if (loan.getClient().getId().equals(client.getId())
                    && loan.getStatus() == LoanStatus.ACTIVE) {
                result.add(loan);
            }
        }

        return result;
    }

    public Loan findClientLoanById(Client client, String loanId) {
        Loan loan = findById(loanId);

        if (!loan.getClient().equals(client)) {
            throw new IllegalStateException("Acest imprumut nu apartine clientului conectat.");
        }

        return loan;
    }

    public void payNextInstallment(String loanId) {
        Loan loan = findById(loanId);

        if (!loan.isActive()) {
            throw new IllegalStateException("Imprumutul nu este activ");
        }

        Installment installment = loan.getNextUnpaidInstallment();

        if (installment == null) {
            loan.setStatus(LoanStatus.CLOSED);
            return;
        }

        accountService.withdraw(
                loan.getLinkedAccountIBAN(),
                installment.getAmount()
        );

        loan.markNextInstallmentAsPaid(LocalDate.now());
    }
    public void payNextInstallmentForClient(Client client, String loanId) {
        Loan loan = findById(loanId);

        if (!loan.getClient().equals(client)) {
            throw new IllegalStateException("Acest imprumut nu apartine clientului conectat.");
        }

        if (!loan.isActive()) {
            throw new IllegalStateException("Imprumutul nu este activ");
        }

        Installment installment = loan.getNextUnpaidInstallment();

        if (installment == null) {
            loan.setStatus(LoanStatus.CLOSED);
            return;
        }

        accountService.withdrawLoanPayment(loan.getLinkedAccountIBAN(), installment.getAmount());

        loan.markNextInstallmentAsPaid(LocalDate.now());
    }

    public void deleteLoan(String id) {
        Loan loan = findById(id);

        loans.remove(loan);
        loansById.remove(id);
    }

    public void checkOverdueInstallments(LocalDate today) {
        for (Loan loan : loans) {
            for (Installment installment : loan.getInstallments()) {
                if (installment.isOverdue(today)) {
                    installment.markAsOverdue();
                }
            }
        }
    }

    public void applyPenaltyToOverdueInstallments(Double penaltyAmount) {
        for (Loan loan : loans) {
            for (Installment installment : loan.getInstallments()) {
                if (installment.getStatus() == InstallmentStatus.OVERDUE) {
                    installment.applyPenalty(penaltyAmount);
                }
            }
        }
    }
}