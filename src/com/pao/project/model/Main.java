package com.pao.project.model;

import com.pao.project.model.*;
import com.pao.project.service.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static final ClientService clientService = ClientService.getInstance();
    private static final UserService userService = UserService.getInstance();
    private static final AccountService accountService = AccountService.getInstance();
    private static final LoanService loanService = LoanService.getInstance();
    private static final TransactionService transactionService = TransactionService.getInstance();
    private static final MerchantService merchantService = MerchantService.getInstance();
    private static final RecurringPaymentService recurringPaymentService = RecurringPaymentService.getInstance();

    public static void main(String[] args) {
        seedData();

        boolean running = true;

        while (running) {
            printMainMenu();

            int option = readInt("Alege optiunea: ");

            try {
                switch (option) {
                    case 1:
                        registerIndividualClient();
                        break;
                    case 2:
                        registerBusinessClient();
                        break;
                    case 3:
                        loginMenu();
                        break;
                    case 4:
                        showAllClients();
                        break;
                    case 5:
                        showAllAccountsSorted();
                        break;
                    case 6:
                        showAllMerchants();
                        break;
                    case 7:
                        executeAllDueRecurringPayments();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Aplicatia s-a inchis.");
                        break;
                    default:
                        System.out.println("Optiune invalida.");
                }
            } catch (RuntimeException e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        }
    }

    private static void seedData() {
        IndividualClient ana = clientService.addIndividualClient("Bucuresti, Strada Ciresi 10", "ana.popescu@gmail.com", "0712345678",
                "Ana", "Popescu", "5010101123456", "Programator", "2001-01-01", 6000.0);

        BusinessClient firma = clientService.addBusinessClient("Bucuresti, Calea Business 20", "office@demoit.ro", "0722222222", "Demo IT SRL",
                "RO12345678", "Mihai Ionescu", 50000.0, 25000.0);

        CurrentAccount anaCurrent = accountService.addCurrentAccount(LocalDate.now(), Currency.RON, 2500.0,
                ana, 10.0);

        SavingsAccount anaSavings = accountService.addSavingsAccount(LocalDate.now(), Currency.RON, 1000.0, ana,
                0.05, 100.0, 500.0);

        CurrentAccount firmaCurrent = accountService.addCurrentAccount(LocalDate.now(), Currency.RON, 15000.0,
                firma, 25.0);

        userService.addUser("ana.user", "Parola1@", "CLIENT", ana);

        userService.addUser("firma.user", "Parola1@", "CLIENT", firma);

        Merchant merchant = merchantService.addMerchant("Demo Telecom", MerchantCategory.TELECOM, firma, firmaCurrent);

        merchantService.addMerchant("StreamPlus", MerchantCategory.STREAMING, firma, firmaCurrent);

        merchantService.addMerchant("City Utilities", MerchantCategory.UTILITIES, firma, firmaCurrent);

        recurringPaymentService.addRecurringPayment(ana, anaCurrent, merchant, 75.0, Currency.RON,
                Frequency.MONTHLY, LocalDate.now(), null, "Abonament telefonie Demo Telecom");

        loanService.createPersonalLoan(ana, 1000.0, anaCurrent.getIBAN(), 12, Frequency.MONTHLY,
                0.10, 6000.0);
    }

    private static void printMainMenu() {
        System.out.println();
        System.out.println("========== MENIU PRINCIPAL ==========");
        System.out.println("1. Inregistreaza client persoana fizica");
        System.out.println("2. Inregistreaza client business");
        System.out.println("3. Login");
        System.out.println("4. Afiseaza toti clientii");
        System.out.println("5. Afiseaza toate conturile sortate dupa sold");
        System.out.println("6. Afiseaza toti merchantii");
        System.out.println("7. Executa toate platile recurente scadente azi");
        System.out.println("0. Iesire");
    }

    private static void printClientMenu(Client client) {
        System.out.println();
        System.out.println("========== MENIU CLIENT ==========");
        System.out.println("Client conectat: " + client.getDisplayName());
        System.out.println("1. Vezi datele mele");
        System.out.println("2. Vezi conturile mele");
        System.out.println("3. Creeaza cont de economii");
        System.out.println("4. Depunere bani");
        System.out.println("5. Retragere bani");
        System.out.println("6. Transfer bani");
        System.out.println("7. Cerere credit");
        System.out.println("8. Vezi imprumuturile mele");
        System.out.println("9. Vezi ratele unui imprumut");
        System.out.println("10. Plateste urmatoarea rata");
        System.out.println("11. Vezi tranzactiile mele");
        System.out.println("12. Creeaza merchant pentru client business");
        System.out.println("13. Creeaza plata recurenta");
        System.out.println("14. Vezi platile mele recurente");
        System.out.println("15. Executa plata recurenta dupa id");
        System.out.println("16. Dezactiveaza plata recurenta");
        System.out.println("17. Activeaza plata recurenta");
        System.out.println("18. Schimba parola");
        System.out.println("19. Logout");
    }

    private static void loginMenu() {
        System.out.println();
        System.out.println("--- Login ---");

        String username = readLine("Username: ");
        String password = readLine("Parola: ");

        User user = userService.login(username, password);
        Client client = user.getClient();

        System.out.println("Login reusit. Bine ai venit, " + client.getDisplayName() + "!");

        clientMenu(user);
    }

    private static void clientMenu(User loggedUser) {
        Client loggedClient = loggedUser.getClient();
        boolean loggedIn = true;

        while (loggedIn) {
            printClientMenu(loggedClient);

            int option = readInt("Alege optiunea: ");

            try {
                switch (option) {
                    case 1:
                        System.out.println(loggedClient);
                        break;
                    case 2:
                        showAccountsForClient(loggedClient);
                        break;
                    case 3:
                        createSavingsAccount(loggedClient);
                        break;
                    case 4:
                        depositMoney(loggedClient);
                        break;
                    case 5:
                        withdrawMoney(loggedClient);
                        break;
                    case 6:
                        transferMoney(loggedClient);
                        break;
                    case 7:
                        createLoan(loggedClient);
                        break;
                    case 8:
                        showLoansForClient(loggedClient);
                        break;
                    case 9:
                        showInstallmentsForLoan(loggedClient);
                        break;
                    case 10:
                        payNextInstallment(loggedClient);
                        break;
                    case 11:
                        showTransactionsForClient(loggedClient);
                        break;
                    case 12:
                        createMerchant(loggedClient);
                        break;
                    case 13:
                        createRecurringPayment(loggedClient);
                        break;
                    case 14:
                        showRecurringPaymentsForClient(loggedClient);
                        break;
                    case 15:
                        executeRecurringPaymentById(loggedClient);
                        break;
                    case 16:
                        deactivateRecurringPayment(loggedClient);
                        break;
                    case 17:
                        activateRecurringPayment(loggedClient);
                        break;
                    case 18:
                        changePassword(loggedUser);
                        break;
                    case 19:
                        loggedIn = false;
                        System.out.println("Logout reusit.");
                        break;
                    default:
                        System.out.println("Optiune invalida.");
                }
            } catch (RuntimeException e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        }
    }

    private static void registerIndividualClient() {
        System.out.println();
        System.out.println("--- Inregistrare client persoana fizica ---");

        String address = readLine("Adresa: ");
        String email = readLine("Email: ");
        String phone = readLine("Telefon: ");
        String firstName = readLine("Prenume: ");
        String lastName = readLine("Nume: ");
        String cnp = readLine("CNP: ");
        String occupation = readLine("Ocupatie: ");
        String dateOfBirth = readLine("Data nasterii yyyy-mm-dd: ");
        Double monthlyIncome = readDouble("Venit lunar: ");

        String username = readLine("Username pentru login: ");
        String password = readLine("Parola pentru login: ");

        IndividualClient client = clientService.addIndividualClient(address, email, phone, firstName, lastName,
                cnp, occupation, dateOfBirth, monthlyIncome);

        CurrentAccount account = accountService.addCurrentAccount(LocalDate.now(), Currency.RON, 0.0,
                client, 10.0);

        userService.addUser(username, password, "CLIENT", client);

        System.out.println("Client creat cu succes.");
        System.out.println("Id client: " + client.getId());
        System.out.println("Nume client: " + client.getDisplayName());
        System.out.println("Cont curent creat automat: " + account.getIBAN());
    }

    private static void registerBusinessClient() {
        System.out.println();
        System.out.println("--- Inregistrare client business ---");

        String address = readLine("Adresa: ");
        String email = readLine("Email: ");
        String phone = readLine("Telefon: ");
        String companyName = readLine("Nume companie: ");
        String cui = readLine("CUI: ");
        String contactPerson = readLine("Persoana contact: ");
        Double monthlyRevenue = readDouble("Venit lunar firma: ");
        Double monthlyExpenses = readDouble("Cheltuieli lunare firma: ");

        String username = readLine("Username pentru login: ");
        String password = readLine("Parola pentru login: ");

        BusinessClient client = clientService.addBusinessClient(address, email, phone, companyName, cui,
                contactPerson, monthlyRevenue, monthlyExpenses);

        CurrentAccount account = accountService.addCurrentAccount(LocalDate.now(), Currency.RON, 0.0, client, 25.0);

        userService.addUser(username, password, "CLIENT", client);

        System.out.println("Client business creat cu succes.");
        System.out.println("Id client: " + client.getId());
        System.out.println("Nume client: " + client.getDisplayName());
        System.out.println("Cont curent creat automat: " + account.getIBAN());
    }

    private static void showAllClients() {
        System.out.println();
        System.out.println("--- Toti clientii ---");

        List<Client> clients = clientService.getAllClients();

        if (clients.isEmpty()) {
            System.out.println("Nu exista clienti.");
            return;
        }

        for (Client client : clients) {
            System.out.println(client);
        }
    }

    private static void showAllAccountsSorted() {
        System.out.println();
        System.out.println("--- Toate conturile sortate descrescator dupa sold ---");

        List<Account> accounts = accountService.getAllAccounts();
        Collections.sort(accounts);

        if (accounts.isEmpty()) {
            System.out.println("Nu exista conturi.");
            return;
        }

        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    private static void showAccountsForClient(Client client) {
        System.out.println();
        System.out.println("--- Conturile clientului " + client.getDisplayName() + " ---");

        List<Account> accounts = accountService.getAccountsByClient(client);

        if (accounts.isEmpty()) {
            System.out.println("Clientul nu are conturi.");
            return;
        }

        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    private static void createSavingsAccount(Client client) {
        System.out.println();
        System.out.println("--- Creare cont de economii ---");

        Currency currency = readCurrency();
        Double balance = readDouble("Sold initial: ");
        Double interestRate = readDouble("Dobanda ex 0.05: ");
        Double minimumBalance = readDouble("Sold minim: ");
        Double withdrawalLimit = readDouble("Limita retragere: ");

        SavingsAccount account = accountService.addSavingsAccount(LocalDate.now(), currency, balance, client, interestRate, minimumBalance, withdrawalLimit);

        System.out.println("Cont de economii creat cu succes.");
        System.out.println(account);
    }

    private static void depositMoney(Client client) {
        System.out.println();
        System.out.println("--- Depunere bani ---");

        showAccountsForClient(client);

        String iban = readLine("IBAN cont: ");
        Double amount = readDouble("Suma: ");

        accountService.depositForClient(client, iban, amount);

        System.out.println("Depunere realizata cu succes.");
    }

    private static void withdrawMoney(Client client) {
        System.out.println();
        System.out.println("--- Retragere bani ---");

        showAccountsForClient(client);

        String iban = readLine("IBAN cont: ");
        Double amount = readDouble("Suma: ");

        accountService.withdrawForClient(client, iban, amount);

        System.out.println("Retragere realizata cu succes.");
    }

    private static void transferMoney(Client client) {
        System.out.println();
        System.out.println("--- Transfer bani ---");

        showAccountsForClient(client);

        String sourceIban = readLine("IBAN sursa: ");
        String destinationIban = readLine("IBAN destinatie: ");
        Double amount = readDouble("Suma: ");

        accountService.transferFromClient(client, sourceIban, destinationIban, amount);

        System.out.println("Transfer realizat cu succes.");
    }

    private static void createLoan(Client client) {
        System.out.println();
        System.out.println("--- Cerere credit ---");

        showAccountsForClient(client);

        String iban = readLine("IBAN cont virare/plata rate: ");
        Double requestedAmount = readDouble("Suma ceruta: ");
        int numberOfMonths = readInt("Numar luni: ");
        Frequency frequency = readFrequency();
        Double interestRate = readDouble("Dobanda ex 0.10: ");

        if (client instanceof IndividualClient) {
            Double declaredIncome = readDouble("Venit lunar declarat: ");

            PersonalLoan loan = loanService.createPersonalLoan(client, requestedAmount, iban, numberOfMonths,
                    frequency, interestRate, declaredIncome);

            System.out.println("Cerere procesata.");
            System.out.println(loan);
        } else if (client instanceof BusinessClient) {
            Double revenue = readDouble("Venit lunar declarat firma: ");
            Double expenses = readDouble("Cheltuieli lunare declarate firma: ");

            BusinessLoan loan = loanService.createBusinessLoan(client, requestedAmount, iban, numberOfMonths, frequency,
                    interestRate, revenue, expenses);

            System.out.println("Cerere procesata.");
            System.out.println(loan);
        }
    }

    private static void showLoansForClient(Client client) {
        System.out.println();
        System.out.println("--- Imprumuturile clientului ---");

        List<Loan> loans = loanService.getLoansByClient(client);

        if (loans.isEmpty()) {
            System.out.println("Clientul nu are imprumuturi.");
            return;
        }

        for (Loan loan : loans) {
            System.out.println(loan);
        }
    }

    private static void showInstallmentsForLoan(Client client) {
        System.out.println();
        System.out.println("--- Rate imprumut ---");

        showLoansForClient(client);

        String loanId = readLine("Id imprumut: ");

        Loan loan = loanService.findClientLoanById(client, loanId);

        if (loan.getInstallments().isEmpty()) {
            System.out.println("Imprumutul nu are rate generate.");
            return;
        }

        for (Installment installment : loan.getInstallments()) {
            System.out.println(installment);
        }
    }

    private static void payNextInstallment(Client client) {
        System.out.println();
        System.out.println("--- Plata urmatoarei rate ---");

        showLoansForClient(client);

        String loanId = readLine("Id imprumut: ");

        loanService.payNextInstallmentForClient(client, loanId);

        System.out.println("Rata platita cu succes.");
    }

    private static void showTransactionsForClient(Client client) {
        System.out.println();
        System.out.println("--- Tranzactiile clientului ---");

        List<Transaction> transactions = transactionService.getTransactionsByClient(client);

        if (transactions.isEmpty()) {
            System.out.println("Clientul nu are tranzactii.");
            return;
        }

        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    private static void createMerchant(Client client) {
        System.out.println();
        System.out.println("--- Creare merchant ---");

        if (!(client instanceof BusinessClient)) {
            throw new IllegalStateException("Doar clientii business pot crea merchant.");
        }

        showAccountsForClient(client);

        String name = readLine("Nume merchant: ");
        MerchantCategory category = readMerchantCategory();
        String iban = readLine("IBAN cont decontare: ");

        Account settlementAccount = accountService.findClientAccountByIban(client, iban);

        Merchant merchant = merchantService.addMerchant(name, category, (BusinessClient) client, settlementAccount);

        System.out.println("Merchant creat cu succes.");
        System.out.println(merchant);
    }

    private static void showAllMerchants() {
        System.out.println();
        System.out.println("--- Toti merchantii ---");

        List<Merchant> merchants = merchantService.getAllMerchants();

        if (merchants.isEmpty()) {
            System.out.println("Nu exista merchanti.");
            return;
        }

        for (Merchant merchant : merchants) {
            System.out.println(merchant);
        }
    }

//    private static void createRecurringPayment(Client client) {
//        System.out.println();
//        System.out.println("--- Creare plata recurenta ---");
//
//        showAccountsForClient(client);
//
//        String sourceIban = readLine("IBAN cont sursa: ");
//        Account sourceAccount = accountService.findClientAccountByIban(client, sourceIban);
//
//        showAllMerchants();
//
//        String merchantId = readLine("Id merchant: ");
//        Merchant merchant = merchantService.findById(merchantId);
//
//        Double amount = readDouble("Suma plata recurenta: ");
//        Frequency frequency = readFrequency();
//
//        String hasEndDate = readLine("Are data finala? da/nu: ");
//        LocalDate endDate = null;
//
//        if (hasEndDate.equalsIgnoreCase("da")) {
//            String endDateText = readLine("Data finala yyyy-mm-dd: ");
//            endDate = LocalDate.parse(endDateText);
//        }
//
//        String description = readLine("Descriere: ");
//
//        RecurringPayment payment = recurringPaymentService.addRecurringPayment(
//                client,
//                sourceAccount,
//                merchant,
//                amount,
//                sourceAccount.getCurrency(),
//                frequency,
//                LocalDate.now(),
//                endDate,
//                description
//        );
//
//        System.out.println("Plata recurenta creata cu succes.");
//        System.out.println(payment);
//    }

    private static void createRecurringPayment(Client client) {
        System.out.println();
        System.out.println("--- Creare plata recurenta ---");

        showAccountsForClient(client);

        String sourceIban = readLine("IBAN cont sursa: ");
        Account sourceAccount = accountService.findClientAccountByIban(client, sourceIban);

        showAllMerchants();

        String merchantId = readLine("Id merchant: ");
        Merchant merchant = merchantService.findById(merchantId);

        if (!merchant.isActive()) {
            throw new IllegalStateException("Merchantul selectat nu este activ.");
        }

        Double amount = getDefaultAmountForMerchant(merchant);
        Frequency frequency = getDefaultFrequencyForMerchant(merchant);
        String description = getDefaultDescriptionForMerchant(merchant);

        System.out.println();
        System.out.println("Abonament detectat pentru merchant:");
        System.out.println("Merchant: " + merchant.getName());
        System.out.println("Categorie: " + merchant.getCategory());
        System.out.println("Suma lunara: " + amount + " " + sourceAccount.getCurrency());
        System.out.println("Frecventa: " + frequency);
        System.out.println("Descriere: " + description);

        String confirm = readLine("Confirmi crearea platii recurente? da/nu: ");

        if (!confirm.equalsIgnoreCase("da")) {
            System.out.println("Crearea platii recurente a fost anulata.");
            return;
        }

        RecurringPayment payment = recurringPaymentService.addRecurringPayment(client, sourceAccount, merchant, amount,
                sourceAccount.getCurrency(), frequency, LocalDate.now(), null, description);

        System.out.println("Plata recurenta creata cu succes.");
        System.out.println(payment);
    }

    private static void showRecurringPaymentsForClient(Client client) {
        System.out.println();
        System.out.println("--- Platile recurente ale clientului ---");

        List<RecurringPayment> payments = recurringPaymentService.getRecurringPaymentsByClient(client);

        if (payments.isEmpty()) {
            System.out.println("Clientul nu are plati recurente.");
            return;
        }

        for (RecurringPayment payment : payments) {
            System.out.println(payment);
        }
    }

    private static void executeRecurringPaymentById(Client client) {
        System.out.println();
        System.out.println("--- Executare plata recurenta ---");

        showRecurringPaymentsForClient(client);

        String id = readLine("Id plata recurenta: ");

        RecurringPayment payment = recurringPaymentService.findById(id);

        if (!payment.getClient().equals(client)) {
            throw new IllegalStateException("Aceasta plata recurenta nu apartine clientului conectat.");
        }

        recurringPaymentService.executePayment(id, LocalDate.now());

        System.out.println("Plata recurenta executata cu succes.");
    }

    private static void executeAllDueRecurringPayments() {
        System.out.println();
        System.out.println("--- Executare plati recurente scadente azi ---");

        recurringPaymentService.executeDuePayments(LocalDate.now());

        System.out.println("Procesare finalizata.");
    }

    private static void deactivateRecurringPayment(Client client) {
        System.out.println();
        System.out.println("--- Dezactivare plata recurenta ---");

        showRecurringPaymentsForClient(client);

        String id = readLine("Id plata recurenta: ");

        RecurringPayment payment = recurringPaymentService.findById(id);

        if (!payment.getClient().equals(client)) {
            throw new IllegalStateException("Aceasta plata recurenta nu apartine clientului conectat.");
        }

        recurringPaymentService.deactivateRecurringPayment(id);

        System.out.println("Plata recurenta dezactivata.");
    }

    private static void activateRecurringPayment(Client client) {
        System.out.println();
        System.out.println("--- Activare plata recurenta ---");

        showRecurringPaymentsForClient(client);

        String id = readLine("Id plata recurenta: ");

        RecurringPayment payment = recurringPaymentService.findById(id);

        if (!payment.getClient().equals(client)) {
            throw new IllegalStateException("Aceasta plata recurenta nu apartine clientului conectat.");
        }

        recurringPaymentService.activateRecurringPayment(id);

        System.out.println("Plata recurenta activata.");
    }

    private static void changePassword(User loggedUser) {
        System.out.println();
        System.out.println("--- Schimbare parola ---");

        String oldPassword = readLine("Parola veche: ");
        String newPassword = readLine("Parola noua: ");

        userService.changePassword(loggedUser.getUsername(), oldPassword, newPassword);

        System.out.println("Parola schimbata cu succes.");
    }

    private static Double getDefaultAmountForMerchant(Merchant merchant) {
        switch (merchant.getCategory()) {
            case TELECOM:
                return 75.0;
            case STREAMING:
                return 49.99;
            case UTILITIES:
                return 150.0;
            case INSURANCE:
                return 120.0;
            case SHOPPING:
                return 100.0;
            case OTHER:
                return 50.0;
            default:
                return 50.0;
        }
    }

    private static Frequency getDefaultFrequencyForMerchant(Merchant merchant) {
        switch (merchant.getCategory()) {
            case TELECOM:
            case STREAMING:
            case UTILITIES:
            case INSURANCE:
            case SHOPPING:
            case OTHER:
                return Frequency.MONTHLY;
            default:
                return Frequency.MONTHLY;
        }
    }

    private static String getDefaultDescriptionForMerchant(Merchant merchant) {
        switch (merchant.getCategory()) {
            case TELECOM:
                return "Abonament telefonie - " + merchant.getName();
            case STREAMING:
                return "Abonament streaming - " + merchant.getName();
            case UTILITIES:
                return "Factura utilitati - " + merchant.getName();
            case INSURANCE:
                return "Prima asigurare - " + merchant.getName();
            case SHOPPING:
                return "Abonament cumparaturi - " + merchant.getName();
            case OTHER:
                return "Plata recurenta - " + merchant.getName();
            default:
                return "Plata recurenta - " + merchant.getName();
        }
    }

    private static Currency readCurrency() {
        System.out.println("Valuta:");
        System.out.println("1. RON");
        System.out.println("2. EUR");

        int option = readInt("Alege valuta: ");

        switch (option) {
            case 1:
                return Currency.RON;
            case 2:
                return Currency.EUR;
            default:
                throw new IllegalArgumentException("Valuta invalida.");
        }
    }

    private static Frequency readFrequency() {
        System.out.println("Frecventa:");
        System.out.println("1. WEEKLY");
        System.out.println("2. MONTHLY");
        System.out.println("3. YEARLY");

        int option = readInt("Alege frecventa: ");

        switch (option) {
            case 1:
                return Frequency.WEEKLY;
            case 2:
                return Frequency.MONTHLY;
            case 3:
                return Frequency.YEARLY;
            default:
                throw new IllegalArgumentException("Frecventa invalida.");
        }
    }

    private static MerchantCategory readMerchantCategory() {
        System.out.println("Categorie merchant:");
        System.out.println("1. STREAMING");
        System.out.println("2. TELECOM");
        System.out.println("3. UTILITIES");
        System.out.println("4. INSURANCE");
        System.out.println("5. SHOPPING");
        System.out.println("6. OTHER");

        int option = readInt("Alege categoria: ");

        switch (option) {
            case 1:
                return MerchantCategory.STREAMING;
            case 2:
                return MerchantCategory.TELECOM;
            case 3:
                return MerchantCategory.UTILITIES;
            case 4:
                return MerchantCategory.INSURANCE;
            case 5:
                return MerchantCategory.SHOPPING;
            case 6:
                return MerchantCategory.OTHER;
            default:
                throw new IllegalArgumentException("Categorie invalida.");
        }
    }

    private static String readLine(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    private static int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Te rog introdu un numar intreg valid.");
            }
        }
    }

    private static Double readDouble(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Te rog introdu un numar valid.");
            }
        }
    }
}