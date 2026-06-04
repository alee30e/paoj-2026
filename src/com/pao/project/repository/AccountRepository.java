package com.pao.project.repository;

import com.pao.project.model.Account;
import com.pao.project.model.AccountType;
import com.pao.project.model.BusinessClient;
import com.pao.project.model.Client;
import com.pao.project.model.ClientType;
import com.pao.project.model.Currency;
import com.pao.project.model.CurrentAccount;
import com.pao.project.model.IndividualClient;
import com.pao.project.model.SavingsAccount;
import com.pao.project.model.Transaction;
import com.pao.project.model.TransactionType;
import com.pao.project.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepository implements Repository<Account, String> {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Account account) throws SQLException {
//        String accountSql = """
//                INSERT INTO accounts (id, iban, client_id, account_type, opened_date, currency, balance)
//                VALUES (?, ?, ?, ?, ?, ?, ?)
//                """;
        String accountSql = """
                INSERT INTO accounts (iban, client_id, account_type, opened_date, currency, balance)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        //        Connection connection = getConn();
        try (Connection connection = getConn()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement ps = connection.prepareStatement(accountSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
//                ps.setString(1, account.getId());
//                ps.setString(2, account.getIBAN());
//                ps.setString(3, account.getOwner().getId());
//                ps.setString(4, account.getAccountType().name());
//                ps.setDate(5, Date.valueOf(account.getOpenedDate()));
//                ps.setString(6, account.getCurrency().name());
//                ps.setDouble(7, account.getBalance());
                    ps.setString(1, account.getIBAN());
                    ps.setLong(2, Long.parseLong(account.getOwner().getId()));
                    ps.setString(3, account.getAccountType().name());
                    ps.setDate(4, Date.valueOf(account.getOpenedDate()));
                    ps.setString(5, account.getCurrency().name());
                    ps.setDouble(6, account.getBalance());

                    assertSingleRowAffected(ps.executeUpdate(), "Contul nu a putut fi salvat.");
                    setGeneratedId(account, ps);
                }

                if (account instanceof CurrentAccount currentAccount) {
                    saveCurrentAccount(connection, currentAccount);
                } else if (account instanceof SavingsAccount savingsAccount) {
                    saveSavingsAccount(connection, savingsAccount);
                } else {
                    throw new SQLException("Tip de cont necunoscut.");
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private void saveCurrentAccount(Connection connection, CurrentAccount account) throws SQLException {
        String sql = """
                INSERT INTO current_accounts (account_id, monthly_fee)
                VALUES (?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setString(1, account.getId());
            ps.setLong(1, Long.parseLong(account.getId()));
            ps.setDouble(2, account.getMonthlyFee());

            assertSingleRowAffected(ps.executeUpdate(), "Detaliile contului curent nu au putut fi salvate.");
        }
    }

    private void saveSavingsAccount(Connection connection, SavingsAccount account) throws SQLException {
        String sql = """
                INSERT INTO savings_accounts (account_id, interest_rate, minimum_balance, withdrawal_limit)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setString(1, account.getId());
            ps.setLong(1, Long.parseLong(account.getId()));
            ps.setDouble(2, account.getInterestRate());
            ps.setDouble(3, account.getMinimumBalance());
            ps.setDouble(4, account.getWithdrawalLimit());

            assertSingleRowAffected(ps.executeUpdate(), "Detaliile contului de economii nu au putut fi salvate.");
        }
    }

    @Override
    public Optional<Account> findById(String id) throws SQLException {
        String sql = """
                SELECT
                    a.id,
                    a.iban,
                    a.client_id,
                    a.account_type,
                    a.opened_date,
                    a.currency,
                    a.balance,
                    c.client_type,
                    c.address,
                    c.email,
                    c.phone,
                
                    i.first_name,
                    i.last_name,
                    i.cnp,
                    i.occupation,
                    i.date_of_birth,
                    i.monthly_income,
                
                    b.company_name,
                    b.cui,
                    b.contact_person,
                    b.monthly_revenue,
                    b.monthly_expenses,
                
                    ca.monthly_fee,
                
                    sa.interest_rate,
                    sa.minimum_balance,
                    sa.withdrawal_limit
                
                FROM accounts a
                JOIN clients c ON a.client_id = c.id
                LEFT JOIN individual_clients i ON c.id = i.client_id
                LEFT JOIN business_clients b ON c.id = b.client_id
                LEFT JOIN current_accounts ca ON a.id = ca.account_id
                LEFT JOIN savings_accounts sa ON a.id = sa.account_id
                WHERE a.id = ?
                """;

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setString(1, id);
            ps.setLong(1, Long.parseLong(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }

                return Optional.empty();
            }
        }
    }

    public Optional<Account> findByIban(String iban) throws SQLException {
        String sql = """
                SELECT
                    a.id,
                    a.iban,
                    a.client_id,
                    a.account_type,
                    a.opened_date,
                    a.currency,
                    a.balance,
                    c.client_type,
                    c.address,
                    c.email,
                    c.phone,
                
                    i.first_name,
                    i.last_name,
                    i.cnp,
                    i.occupation,
                    i.date_of_birth,
                    i.monthly_income,
                
                    b.company_name,
                    b.cui,
                    b.contact_person,
                    b.monthly_revenue,
                    b.monthly_expenses,
                
                    ca.monthly_fee,
                
                    sa.interest_rate,
                    sa.minimum_balance,
                    sa.withdrawal_limit
                
                FROM accounts a
                JOIN clients c ON a.client_id = c.id
                LEFT JOIN individual_clients i ON c.id = i.client_id
                LEFT JOIN business_clients b ON c.id = b.client_id
                LEFT JOIN current_accounts ca ON a.id = ca.account_id
                LEFT JOIN savings_accounts sa ON a.id = sa.account_id
                WHERE a.iban = ?
                """;

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, iban);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }

                return Optional.empty();
            }
        }
    }

    @Override
    public List<Account> findAll() throws SQLException {
        String sql = """
                SELECT
                    a.id,
                    a.iban,
                    a.client_id,
                    a.account_type,
                    a.opened_date,
                    a.currency,
                    a.balance,
                    c.client_type,
                    c.address,
                    c.email,
                    c.phone,
                
                    i.first_name,
                    i.last_name,
                    i.cnp,
                    i.occupation,
                    i.date_of_birth,
                    i.monthly_income,
                
                    b.company_name,
                    b.cui,
                    b.contact_person,
                    b.monthly_revenue,
                    b.monthly_expenses,
                
                    ca.monthly_fee,
                
                    sa.interest_rate,
                    sa.minimum_balance,
                    sa.withdrawal_limit
                
                FROM accounts a
                JOIN clients c ON a.client_id = c.id
                LEFT JOIN individual_clients i ON c.id = i.client_id
                LEFT JOIN business_clients b ON c.id = b.client_id
                LEFT JOIN current_accounts ca ON a.id = ca.account_id
                LEFT JOIN savings_accounts sa ON a.id = sa.account_id
                ORDER BY a.id
                """;

        List<Account> accounts = new ArrayList<>();

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                accounts.add(mapRow(rs));
            }
        }

        return accounts;
    }

    @Override
    public void update(Account account) throws SQLException {
        String accountSql = """
                UPDATE accounts
                SET iban = ?, client_id = ?, opened_date = ?, currency = ?, balance = ?
                WHERE id = ?
                """;

//        Connection connection = getConn();
        try (Connection connection = getConn()) {
            connection.setAutoCommit(false);

            try {
                ensureAccountTypeUnchanged(connection, account);

                try (PreparedStatement ps = connection.prepareStatement(accountSql)) {
                    ps.setString(1, account.getIBAN());
//                ps.setString(2, account.getOwner().getId());
                    ps.setLong(2, Long.parseLong(account.getOwner().getId()));
                    ps.setDate(3, Date.valueOf(account.getOpenedDate()));
                    ps.setString(4, account.getCurrency().name());
                    ps.setDouble(5, account.getBalance());
//                ps.setString(6, account.getId());
                    ps.setLong(6, Long.parseLong(account.getId()));

                    assertSingleRowAffected(ps.executeUpdate(), "Contul nu a fost gasit pentru update.");
                }

                if (account instanceof CurrentAccount currentAccount) {
                    updateCurrentAccount(connection, currentAccount);
                } else if (account instanceof SavingsAccount savingsAccount) {
                    updateSavingsAccount(connection, savingsAccount);
                } else {
                    throw new SQLException("Tip de cont necunoscut.");
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private void updateCurrentAccount(Connection connection, CurrentAccount account) throws SQLException {
        String sql = """
                UPDATE current_accounts
                SET monthly_fee = ?
                WHERE account_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, account.getMonthlyFee());
//            ps.setString(2, account.getId());
            ps.setLong(2, Long.parseLong(account.getId()));

            assertSingleRowAffected(ps.executeUpdate(), "Contul curent nu a fost gasit pentru update.");
        }
    }

    private void updateSavingsAccount(Connection connection, SavingsAccount account) throws SQLException {
        String sql = """
                UPDATE savings_accounts
                SET interest_rate = ?, minimum_balance = ?, withdrawal_limit = ?
                WHERE account_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, account.getInterestRate());
            ps.setDouble(2, account.getMinimumBalance());
            ps.setDouble(3, account.getWithdrawalLimit());
//            ps.setString(4, account.getId());
            ps.setLong(4, Long.parseLong(account.getId()));

            assertSingleRowAffected(ps.executeUpdate(), "Contul de economii nu a fost gasit pentru update.");
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String deleteCurrentSql = """
                DELETE FROM current_accounts
                WHERE account_id = ?
                """;

        String deleteSavingsSql = """
                DELETE FROM savings_accounts
                WHERE account_id = ?
                """;

        String deleteAccountSql = """
                DELETE FROM accounts
                WHERE id = ?
                """;

//        Connection connection = getConn();
        try (Connection connection = getConn()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement ps = connection.prepareStatement(deleteCurrentSql)) {
//                ps.setString(1, id);
                    ps.setLong(1, Long.parseLong(id));
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = connection.prepareStatement(deleteSavingsSql)) {
//                ps.setString(1, id);
                    ps.setLong(1, Long.parseLong(id));
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = connection.prepareStatement(deleteAccountSql)) {
//                ps.setString(1, id);
                    ps.setLong(1, Long.parseLong(id));
                    assertSingleRowAffected(ps.executeUpdate(), "Contul nu a fost gasit pentru stergere.");
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public Transaction transferWithTransaction(String sourceIban, String destinationIban, double amount, String description) throws SQLException {
        String selectSql = """
                SELECT iban, balance, currency
                FROM accounts
                WHERE iban = ?
                """;

        String updateSql = """
                UPDATE accounts
                SET balance = ?
                WHERE iban = ?
                """;

        String insertTransactionSql = """
                INSERT INTO transactions
                (source_iban, destination_iban, amount, currency, transaction_type, timestamp, description)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

//        Connection connection = getConn();
        try (Connection connection = getConn()) {
            connection.setAutoCommit(false);

            try {
                AccountSnapshot source = loadAccountSnapshot(connection, selectSql, sourceIban);
                AccountSnapshot destination = loadAccountSnapshot(connection, selectSql, destinationIban);

                validateTransfer(source, destination, amount);

                updateBalance(connection, updateSql, source.balance - amount, sourceIban);
                updateBalance(connection, updateSql, destination.balance + amount, destinationIban);

                Transaction transaction = new Transaction(
                        sourceIban,
                        destinationIban,
                        amount,
                        source.currency,
                        TransactionType.TRANSFER,
                        description
                );

                try (PreparedStatement ps = connection.prepareStatement(insertTransactionSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, transaction.getSourceIban());
                    ps.setString(2, transaction.getDestinationIban());
                    ps.setDouble(3, transaction.getAmount());
                    ps.setString(4, transaction.getCurrency().name());
                    ps.setString(5, transaction.getType().name());
                    ps.setTimestamp(6, java.sql.Timestamp.valueOf(transaction.getTimestamp()));
                    ps.setString(7, transaction.getDescription());

                    assertSingleRowAffected(ps.executeUpdate(), "Tranzactia de transfer nu a putut fi salvata.");
                    setGeneratedTransactionId(transaction, ps);
                }

                connection.commit();
                return transaction;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private Account mapRow(ResultSet rs) throws SQLException {
//        String id = rs.getString("id");
        String id = String.valueOf(rs.getLong("id"));
        String iban = rs.getString("iban");
        AccountType accountType = AccountType.valueOf(rs.getString("account_type"));
        Currency currency = Currency.valueOf(rs.getString("currency"));
        Client owner = mapClient(rs);

        Account account;

        if (accountType == AccountType.CURRENT) {
            CurrentAccount currentAccount = new CurrentAccount(
                    iban,
                    rs.getDate("opened_date").toLocalDate(),
                    currency,
                    rs.getDouble("balance"),
                    owner,
                    rs.getDouble("monthly_fee")
            );

            currentAccount.setId(id);
            account = currentAccount;

        } else if (accountType == AccountType.SAVINGS) {
            SavingsAccount savingsAccount = new SavingsAccount(
                    iban,
                    rs.getDate("opened_date").toLocalDate(),
                    currency,
                    rs.getDouble("balance"),
                    owner,
                    rs.getDouble("interest_rate"),
                    rs.getDouble("minimum_balance"),
                    rs.getDouble("withdrawal_limit")
            );

            savingsAccount.setId(id);
            account = savingsAccount;

        } else {
            throw new SQLException("Tip de cont invalid: " + accountType);
        }

        owner.addAccount(account);
        return account;
    }

    private Client mapClient(ResultSet rs) throws SQLException {
//        String id = rs.getString("client_id");
        String id = String.valueOf(rs.getLong("client_id"));
        String address = rs.getString("address");
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        ClientType clientType = ClientType.valueOf(rs.getString("client_type"));

        if (clientType == ClientType.INDIVIDUAL) {
            IndividualClient client = new IndividualClient(
                    address,
                    email,
                    phone,
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("cnp"),
                    rs.getString("occupation"),
                    rs.getString("date_of_birth"),
                    rs.getDouble("monthly_income")
            );
            client.setId(id);
            return client;
        }

        if (clientType == ClientType.BUSINESS) {
            BusinessClient client = new BusinessClient(
                    address,
                    email,
                    phone,
                    rs.getString("company_name"),
                    rs.getString("cui"),
                    rs.getString("contact_person"),
                    rs.getDouble("monthly_revenue"),
                    rs.getDouble("monthly_expenses")
            );
            client.setId(id);
            return client;
        }

        throw new SQLException("Tip de client invalid pentru cont: " + clientType);
    }

    private void ensureAccountTypeUnchanged(Connection connection, Account account) throws SQLException {
        String sql = """
                SELECT account_type
                FROM accounts
                WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setString(1, account.getId());
            ps.setLong(1, Long.parseLong(account.getId()));

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Contul nu a fost gasit pentru update.");
                }

                AccountType existingType = AccountType.valueOf(rs.getString("account_type"));
                if (existingType != account.getAccountType()) {
                    throw new SQLException("Tipul contului nu poate fi schimbat.");
                }
            }
        }
    }

    private void assertSingleRowAffected(int affectedRows, String errorMessage) throws SQLException {
        if (affectedRows != 1) {
            throw new SQLException(errorMessage);
        }
    }

    private AccountSnapshot loadAccountSnapshot(Connection connection, String sql, String iban) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, iban);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Contul cu IBAN-ul " + iban + " nu a fost gasit.");
                }

                return new AccountSnapshot(
                        rs.getString("iban"),
                        rs.getDouble("balance"),
                        Currency.valueOf(rs.getString("currency"))
                );
            }
        }
    }

    private void updateBalance(Connection connection, String sql, double balance, String iban) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, balance);
            ps.setString(2, iban);
            assertSingleRowAffected(ps.executeUpdate(), "Soldul contului nu a putut fi actualizat pentru IBAN-ul " + iban + ".");
        }
    }

    private void validateTransfer(AccountSnapshot source, AccountSnapshot destination, double amount) throws SQLException {
        if (source.iban.equals(destination.iban)) {
            throw new SQLException("Transferul intre acelasi cont nu este permis.");
        }

        if (amount <= 0) {
            throw new SQLException("Suma transferata trebuie sa fie pozitiva.");
        }

        if (source.balance < amount) {
            throw new SQLException("Fonduri insuficiente pentru transfer.");
        }

        if (source.currency != destination.currency) {
            throw new SQLException("Transferul intre conturi cu valute diferite nu este permis.");
        }
    }

    private void setGeneratedId(Account account, PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (!rs.next()) {
                throw new SQLException("ID-ul generat pentru cont nu a putut fi citit.");
            }
            account.setId(String.valueOf(rs.getLong(1)));
        }
    }

    private void setGeneratedTransactionId(Transaction transaction, PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (!rs.next()) {
                throw new SQLException("ID-ul generat pentru tranzactia de transfer nu a putut fi citit.");
            }
            transaction.setId(String.valueOf(rs.getLong(1)));
        }
    }

    private static final class AccountSnapshot {
        private final String iban;
        private final double balance;
        private final Currency currency;

        private AccountSnapshot(String iban, double balance, Currency currency) {
            this.iban = iban;
            this.balance = balance;
            this.currency = currency;
        }
    }
}





