package com.pao.project.repository;

import com.pao.project.model.Currency;
import com.pao.project.model.Transaction;
import com.pao.project.model.TransactionType;
import com.pao.project.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionRepository implements Repository<Transaction, String> {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Transaction transaction) throws SQLException {
//        String sql = """
//                INSERT INTO transactions
//                (id, source_iban, destination_iban, amount, currency, transaction_type, timestamp, description)
//                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
//                """;
        String sql = """
                INSERT INTO transactions
                (source_iban, destination_iban, amount, currency, transaction_type, timestamp, description)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
//            ps.setString(1, transaction.getId());
//            ps.setString(2, transaction.getSourceIban());
//            ps.setString(3, transaction.getDestinationIban());
//            ps.setDouble(4, transaction.getAmount());
//            ps.setString(5, transaction.getCurrency().name());
//            ps.setString(6, transaction.getType().name());
//            ps.setTimestamp(7, Timestamp.valueOf(transaction.getTimestamp()));
//            ps.setString(8, transaction.getDescription());
            ps.setString(1, transaction.getSourceIban());
            ps.setString(2, transaction.getDestinationIban());
            ps.setDouble(3, transaction.getAmount());
            ps.setString(4, transaction.getCurrency().name());
            ps.setString(5, transaction.getType().name());
            ps.setTimestamp(6, Timestamp.valueOf(transaction.getTimestamp()));
            ps.setString(7, transaction.getDescription());

            assertSingleRowAffected(ps.executeUpdate(), "Tranzactia nu a putut fi salvata.");
            setGeneratedId(transaction, ps);
        }
    }

    @Override
    public Optional<Transaction> findById(String id) throws SQLException {
        String sql = """
                SELECT
                    t.id,
                    t.source_iban,
                    t.destination_iban,
                    t.amount,
                    t.currency,
                    t.transaction_type,
                    t.timestamp,
                    t.description
                FROM transactions t
                WHERE t.id = ?
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

    @Override
    public List<Transaction> findAll() throws SQLException {
        String sql = """
                SELECT
                    t.id,
                    t.source_iban,
                    t.destination_iban,
                    t.amount,
                    t.currency,
                    t.transaction_type,
                    t.timestamp,
                    t.description
                FROM transactions t
                ORDER BY t.timestamp DESC
                """;

        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                transactions.add(mapRow(rs));
            }
        }

        return transactions;
    }

    public List<String> findTransactionsWithAccountOwnersReport() throws SQLException {
        String sql = """
                SELECT
                    t.id,
                    t.source_iban,
                    t.destination_iban,
                    t.amount,
                    t.currency,
                    t.transaction_type,
                    t.timestamp,
                    COALESCE(CONCAT(si.first_name, ' ', si.last_name), sb.company_name) AS source_client_name,
                    COALESCE(CONCAT(di.first_name, ' ', di.last_name), db.company_name) AS destination_client_name
                FROM transactions t
                LEFT JOIN accounts sa ON t.source_iban = sa.iban
                LEFT JOIN clients sc ON sa.client_id = sc.id
                LEFT JOIN individual_clients si ON sc.id = si.client_id
                LEFT JOIN business_clients sb ON sc.id = sb.client_id
                LEFT JOIN accounts da ON t.destination_iban = da.iban
                LEFT JOIN clients dc ON da.client_id = dc.id
                LEFT JOIN individual_clients di ON dc.id = di.client_id
                LEFT JOIN business_clients db ON dc.id = db.client_id
                ORDER BY t.timestamp DESC
                """;

        List<String> report = new ArrayList<>();

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                report.add(
                        "TransactionId=" + rs.getLong("id") +
                                ", sursa=" + rs.getString("source_iban") +
                                " (" + fallbackName(rs.getString("source_client_name")) + ")" +
                                ", destinatie=" + rs.getString("destination_iban") +
                                " (" + fallbackName(rs.getString("destination_client_name")) + ")" +
                                ", suma=" + rs.getDouble("amount") + " " + rs.getString("currency") +
                                ", tip=" + rs.getString("transaction_type") +
                                ", timestamp=" + rs.getTimestamp("timestamp")
                );
            }
        }

        return report;
    }

    @Override
    public void update(Transaction transaction) throws SQLException {
        String sql = """
                UPDATE transactions
                SET source_iban = ?, destination_iban = ?, amount = ?, currency = ?, transaction_type = ?, timestamp = ?, description = ?
                WHERE id = ?
                """;

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, transaction.getSourceIban());
            ps.setString(2, transaction.getDestinationIban());
            ps.setDouble(3, transaction.getAmount());
            ps.setString(4, transaction.getCurrency().name());
            ps.setString(5, transaction.getType().name());
            ps.setTimestamp(6, Timestamp.valueOf(transaction.getTimestamp()));
            ps.setString(7, transaction.getDescription());
//            ps.setString(8, transaction.getId());
            ps.setLong(8, Long.parseLong(transaction.getId()));

            assertSingleRowAffected(ps.executeUpdate(), "Tranzactia nu a fost gasita pentru update.");
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = """
                DELETE FROM transactions
                WHERE id = ?
                """;

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setString(1, id);
            ps.setLong(1, Long.parseLong(id));

            assertSingleRowAffected(ps.executeUpdate(), "Tranzactia nu a fost gasita pentru stergere.");
        }
    }

    public List<Transaction> findByIban(String iban) throws SQLException {
        String sql = """
                SELECT
                    t.id,
                    t.source_iban,
                    t.destination_iban,
                    t.amount,
                    t.currency,
                    t.transaction_type,
                    t.timestamp,
                    t.description,
                    src.client_id AS source_client_id,
                    dst.client_id AS destination_client_id
                FROM transactions t
                LEFT JOIN accounts src ON t.source_iban = src.iban
                LEFT JOIN accounts dst ON t.destination_iban = dst.iban
                WHERE t.source_iban = ? OR t.destination_iban = ?
                ORDER BY t.timestamp DESC
                """;

        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, iban);
            ps.setString(2, iban);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapRow(rs));
                }
            }
        }

        return transactions;
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction(
                rs.getString("source_iban"),
                rs.getString("destination_iban"),
                rs.getDouble("amount"),
                Currency.valueOf(rs.getString("currency")),
                TransactionType.valueOf(rs.getString("transaction_type")),
                rs.getString("description")
        );

//        setField(transaction, "id", rs.getString("id"));
//        setField(transaction, "timestamp", rs.getTimestamp("timestamp").toLocalDateTime());
        transaction.setId(String.valueOf(rs.getLong("id")));
        transaction.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        return transaction;
    }

    private void assertSingleRowAffected(int affectedRows, String errorMessage) throws SQLException {
        if (affectedRows != 1) {
            throw new SQLException(errorMessage);
        }
    }

    private String fallbackName(String value) {
        return value == null ? "-" : value;
    }

    private void setGeneratedId(Transaction transaction, PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (!rs.next()) {
                throw new SQLException("ID-ul generat pentru tranzactie nu a putut fi citit.");
            }
            transaction.setId(String.valueOf(rs.getLong(1)));
        }
    }
}
