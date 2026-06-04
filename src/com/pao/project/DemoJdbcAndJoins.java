package com.pao.project;

import com.pao.project.model.BusinessClient;
import com.pao.project.model.BusinessLoan;
import com.pao.project.model.Client;
import com.pao.project.model.Currency;
import com.pao.project.model.CurrentAccount;
import com.pao.project.model.Frequency;
import com.pao.project.model.IndividualClient;
import com.pao.project.model.LoanStatus;
import com.pao.project.model.PersonalLoan;
import com.pao.project.model.SavingsAccount;
import com.pao.project.model.Transaction;
import com.pao.project.model.User;
import com.pao.project.repository.AccountRepository;
import com.pao.project.repository.ClientRepository;
import com.pao.project.repository.LoanRepository;
import com.pao.project.repository.TransactionRepository;
import com.pao.project.repository.UserRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class DemoJdbcAndJoins {
    public static void main(String[] args) {
        ClientRepository clientRepository = new ClientRepository();
        AccountRepository accountRepository = new AccountRepository();
        UserRepository userRepository = new UserRepository();
        LoanRepository loanRepository = new LoanRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

        long suffix = System.currentTimeMillis();

        try {
            System.out.println("=== 1. Inserare clienti ===");

            IndividualClient ana = new IndividualClient(
                    "Bucuresti, Strada Lalelelor 10",
                    "ana.demo." + suffix + "@gmail.com",
                    "0710000001",
                    "Ana",
                    "Popescu",
                    "5010101" + String.format("%06d", suffix % 1_000_000),
                    "Programator",
                    "2001-01-01",
                    7800.0
            );

            IndividualClient mihai = new IndividualClient(
                    "Cluj, Strada Memorandumului 21",
                    "mihai.demo." + suffix + "@gmail.com",
                    "0710000002",
                    "Mihai",
                    "Ionescu",
                    "5020202" + String.format("%06d", suffix % 1_000_000),
                    "Analist",
                    "2002-02-02",
                    6400.0
            );

            IndividualClient elena = new IndividualClient(
                    "Iasi, Bulevardul Independentei 15",
                    "elena.demo." + suffix + "@gmail.com",
                    "0710000003",
                    "Elena",
                    "Georgescu",
                    "5030303" + String.format("%06d", suffix % 1_000_000),
                    "Medic",
                    "2000-03-03",
                    9100.0
            );

            IndividualClient radu = new IndividualClient(
                    "Brasov, Strada Muresenilor 8",
                    "radu.demo." + suffix + "@gmail.com",
                    "0710000004",
                    "Radu",
                    "Dumitrescu",
                    "5040404" + String.format("%06d", suffix % 1_000_000),
                    "Profesor",
                    "1999-04-04",
                    5900.0
            );

            BusinessClient alpha = new BusinessClient(
                    "Bucuresti, Calea Victoriei 100",
                    "office.alpha." + suffix + "@firma.ro",
                    "0720000001",
                    "Alpha Tech SRL",
                    "ROALPHA" + suffix,
                    "Andreea Marin",
                    85000.0,
                    42000.0
            );

            BusinessClient beta = new BusinessClient(
                    "Timisoara, Strada Republicii 12",
                    "office.beta." + suffix + "@firma.ro",
                    "0720000002",
                    "Beta Logistics SRL",
                    "ROBETA" + suffix,
                    "Cristian Pavel",
                    120000.0,
                    73000.0
            );

            BusinessClient gamma = new BusinessClient(
                    "Constanta, Bulevardul Mamaia 77",
                    "office.gamma." + suffix + "@firma.ro",
                    "0720000003",
                    "Gamma Retail SRL",
                    "ROGAMMA" + suffix,
                    "Ioana Rusu",
                    99000.0,
                    54000.0
            );

            saveClients(clientRepository, ana, mihai, elena, radu, alpha, beta, gamma);
            System.out.println("Clientii au fost salvati.");

            System.out.println();
            System.out.println("=== 2. Inserare conturi ===");

            CurrentAccount anaCurrent = new CurrentAccount("RO49PAOO" + suffix + "001", LocalDate.now(), Currency.RON, 5000.0, ana, 10.0);
            SavingsAccount anaSavings = new SavingsAccount("RO49PAOO" + suffix + "002", LocalDate.now(), Currency.RON, 12000.0, ana, 0.05, 500.0, 1500.0);

            CurrentAccount mihaiCurrent = new CurrentAccount("RO49PAOO" + suffix + "003", LocalDate.now(), Currency.RON, 3200.0, mihai, 10.0);
            SavingsAccount mihaiSavings = new SavingsAccount("RO49PAOO" + suffix + "004", LocalDate.now(), Currency.EUR, 2100.0, mihai, 0.03, 100.0, 600.0);

            CurrentAccount elenaCurrent = new CurrentAccount("RO49PAOO" + suffix + "005", LocalDate.now(), Currency.RON, 8300.0, elena, 10.0);
            CurrentAccount raduCurrent = new CurrentAccount("RO49PAOO" + suffix + "006", LocalDate.now(), Currency.RON, 2700.0, radu, 10.0);

            CurrentAccount alphaCurrent = new CurrentAccount("RO49PAOO" + suffix + "007", LocalDate.now(), Currency.RON, 45000.0, alpha, 25.0);
            SavingsAccount alphaSavings = new SavingsAccount("RO49PAOO" + suffix + "008", LocalDate.now(), Currency.RON, 30000.0, alpha, 0.02, 1000.0, 5000.0);

            CurrentAccount betaCurrent = new CurrentAccount("RO49PAOO" + suffix + "009", LocalDate.now(), Currency.RON, 38000.0, beta, 25.0);
            CurrentAccount gammaCurrent = new CurrentAccount("RO49PAOO" + suffix + "010", LocalDate.now(), Currency.RON, 29000.0, gamma, 25.0);

            saveAccounts(accountRepository,
                    anaCurrent, anaSavings,
                    mihaiCurrent, mihaiSavings,
                    elenaCurrent, raduCurrent,
                    alphaCurrent, alphaSavings,
                    betaCurrent, gammaCurrent
            );
            System.out.println("Conturile au fost salvate.");

            System.out.println();
            System.out.println("=== 3. Inserare useri ===");

            User userAna = new User("ana_demo_" + (suffix % 100000), "Parola1@", "CLIENT", ana);
            User userMihai = new User("mihai_demo_" + (suffix % 100000), "Parola1@", "CLIENT", mihai);
            User userAlpha = new User("alpha_demo_" + (suffix % 100000), "Parola1@", "CLIENT", alpha);
            User userBeta = new User("beta_demo_" + (suffix % 100000), "Parola1@", "CLIENT", beta);

            saveUsers(userRepository, userAna, userMihai, userAlpha, userBeta);
            System.out.println("Userii au fost salvati.");

            System.out.println();
            System.out.println("=== 4. Inserare credite ===");

            PersonalLoan anaLoan = new PersonalLoan(
                    ana,
                    15000.0,
                    anaCurrent.getIBAN(),
                    24,
                    Frequency.MONTHLY,
                    LocalDate.now(),
                    0.12,
                    7800.0,
                    0.40
            );
//            anaLoan.setId("LOAN_DEMO_" + suffix + "_1");
            anaLoan.setLoanNumber("LN_DEMO_" + suffix + "_1");
            anaLoan.setStatus(LoanStatus.ACTIVE);

            BusinessLoan alphaLoan = new BusinessLoan(
                    alpha,
                    50000.0,
                    alphaCurrent.getIBAN(),
                    36,
                    Frequency.MONTHLY,
                    LocalDate.now(),
                    0.10,
                    85000.0,
                    42000.0,
                    0.35
            );
//            alphaLoan.setId("LOAN_DEMO_" + suffix + "_2");
            alphaLoan.setLoanNumber("LN_DEMO_" + suffix + "_2");
            alphaLoan.setStatus(LoanStatus.ACTIVE);

            PersonalLoan raduLoan = new PersonalLoan(
                    radu,
                    8000.0,
                    raduCurrent.getIBAN(),
                    12,
                    Frequency.MONTHLY,
                    LocalDate.now(),
                    0.11,
                    5900.0,
                    0.40
            );
//            raduLoan.setId("LOAN_DEMO_" + suffix + "_3");
            raduLoan.setLoanNumber("LN_DEMO_" + suffix + "_3");
            raduLoan.setStatus(LoanStatus.REJECTED);

            loanRepository.save(anaLoan);
            loanRepository.save(alphaLoan);
            loanRepository.save(raduLoan);
            System.out.println("Creditele au fost salvate.");

            System.out.println();
            System.out.println("=== 5. Tranzactie JDBC reusita ===");
            double sourceBefore = accountRepository.findByIban(anaCurrent.getIBAN()).orElseThrow().getBalance();
            double destinationBefore = accountRepository.findByIban(alphaCurrent.getIBAN()).orElseThrow().getBalance();

            Transaction successTransaction = accountRepository.transferWithTransaction(
                    anaCurrent.getIBAN(),
                    alphaCurrent.getIBAN(),
                    350.0,
                    "Transfer demo JDBC reusit"
            );

            double sourceAfter = accountRepository.findByIban(anaCurrent.getIBAN()).orElseThrow().getBalance();
            double destinationAfter = accountRepository.findByIban(alphaCurrent.getIBAN()).orElseThrow().getBalance();

            System.out.println("Tranzactie salvata cu id=" + successTransaction.getId());
            System.out.println("Sold sursa inainte=" + sourceBefore + ", dupa=" + sourceAfter);
            System.out.println("Sold destinatie inainte=" + destinationBefore + ", dupa=" + destinationAfter);

            System.out.println();
            System.out.println("=== 6. Tranzactie JDBC cu rollback ===");
            double rollbackSourceBefore = accountRepository.findByIban(mihaiCurrent.getIBAN()).orElseThrow().getBalance();

            try {
                accountRepository.transferWithTransaction(
                        mihaiCurrent.getIBAN(),
                        "RO49PAOO_CONT_INEXISTENT",
                        500.0,
                        "Transfer demo JDBC esuat"
                );
                System.out.println("EROARE: transferul trebuia sa esueze.");
            } catch (SQLException e) {
                double rollbackSourceAfter = accountRepository.findByIban(mihaiCurrent.getIBAN()).orElseThrow().getBalance();
                System.out.println("Transfer esuat corect: " + e.getMessage());
                System.out.println("Sold sursa inainte rollback=" + rollbackSourceBefore + ", dupa rollback=" + rollbackSourceAfter);
            }

            System.out.println();
            System.out.println("=== 7. JOIN 1: clienti + numar conturi ===");
            printLines(clientRepository.findClientsWithAccountCountReport());

            System.out.println();
            System.out.println("=== 8. JOIN 2: credite active + client + cont asociat ===");
            printLines(loanRepository.findActiveLoansReport());

            System.out.println();
            System.out.println("=== 9. JOIN 3: tranzactii + titulari cont sursa/destinatie ===");
            printLines(transactionRepository.findTransactionsWithAccountOwnersReport());

            System.out.println();
            System.out.println("=== 10. Verificare rapida CRUD ===");
            System.out.println("Numar clienti in DB: " + clientRepository.findAll().size());
            System.out.println("Numar conturi in DB: " + accountRepository.findAll().size());
            System.out.println("Numar useri in DB: " + userRepository.findAll().size());
            System.out.println("Numar credite in DB: " + loanRepository.findAll().size());
            System.out.println("Numar tranzactii in DB: " + transactionRepository.findAll().size());

            System.out.println();
            System.out.println("Demo finalizat. Datele raman in baza de date pentru verificare manuala.");

        } catch (SQLException e) {
            System.out.println("Eroare SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void saveClients(ClientRepository repository, Client... clients) throws SQLException {
        for (Client client : clients) {
            repository.save(client);
        }
    }

    private static void saveAccounts(AccountRepository repository, com.pao.project.model.Account... accounts) throws SQLException {
        for (com.pao.project.model.Account account : accounts) {
            repository.save(account);
        }
    }

    private static void saveUsers(UserRepository repository, User... users) throws SQLException {
        for (User user : users) {
            repository.save(user);
        }
    }

    private static void printLines(List<String> lines) {
        if (lines.isEmpty()) {
            System.out.println("(fara rezultate)");
            return;
        }

        for (String line : lines) {
            System.out.println(line);
        }
    }
}
