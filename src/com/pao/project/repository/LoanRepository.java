package com.pao.project.repository;

import com.pao.project.model.BusinessClient;
import com.pao.project.model.BusinessLoan;
import com.pao.project.model.Client;
import com.pao.project.model.ClientType;
import com.pao.project.model.Frequency;
import com.pao.project.model.IndividualClient;
import com.pao.project.model.Loan;
import com.pao.project.model.LoanStatus;
import com.pao.project.model.LoanType;
import com.pao.project.model.PersonalLoan;
import com.pao.project.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanRepository implements Repository<Loan, String> {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Loan loan) throws SQLException {
//        String loanSql = """
//                INSERT INTO loans
//                (id, loan_number, client_id, linked_account_iban, loan_type, requested_amount, interest_rate,
//                 number_of_months, frequency, remaining_amount, start_date, end_date, status)
//                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
//                """;
        String loanSql = """
                INSERT INTO loans
                (loan_number, client_id, linked_account_iban, loan_type, requested_amount, interest_rate,
                 number_of_months, frequency, remaining_amount, start_date, end_date, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

//        Connection connection = getConn();
        try (Connection connection = getConn()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement ps = connection.prepareStatement(loanSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
//                    ps.setString(1, loan.getId());
                    ps.setString(1, loan.getLoanNumber());
//                ps.setString(3, loan.getClient().getId());
                    ps.setLong(2, Long.parseLong(loan.getClient().getId()));
                    ps.setString(3, loan.getLinkedAccountIBAN());
                    ps.setString(4, loan.getLoanType().name());
                    ps.setDouble(5, loan.getRequestedAmount());
                    ps.setDouble(6, loan.getInterestRate());
                    ps.setInt(7, loan.getNumberOfMonths());
                    ps.setString(8, loan.getFrequency().name());
                    ps.setDouble(9, loan.getRemainingAmount());
                    ps.setDate(10, Date.valueOf(loan.getStartDate()));
                    ps.setDate(11, Date.valueOf(loan.getEndDate()));
                    ps.setString(12, loan.getStatus().name());

                    assertSingleRowAffected(ps.executeUpdate(), "Imprumutul nu a putut fi salvat.");
                    setGeneratedId(loan, ps);
                }

                if (loan instanceof PersonalLoan personalLoan) {
                    savePersonalLoan(connection, personalLoan);
                } else if (loan instanceof BusinessLoan businessLoan) {
                    saveBusinessLoan(connection, businessLoan);
                } else {
                    throw new SQLException("Tip de imprumut necunoscut.");
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

    private void savePersonalLoan(Connection connection, PersonalLoan loan) throws SQLException {
        String sql = """
                INSERT INTO personal_loans (loan_id, declared_monthly_income, max_allowed_debt_ratio)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setString(1, loan.getId());
            ps.setLong(1, Long.parseLong(loan.getId()));
            ps.setDouble(2, loan.getDeclaredMonthlyIncome());
            ps.setDouble(3, loan.getMaxAllowedDebtRatio());

            assertSingleRowAffected(ps.executeUpdate(), "Detaliile creditului personal nu au putut fi salvate.");
        }
    }

    private void saveBusinessLoan(Connection connection, BusinessLoan loan) throws SQLException {
        String sql = """
                INSERT INTO business_loans (loan_id, declared_monthly_revenue, declared_monthly_expenses, max_allowed_debt_ratio)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setString(1, loan.getId());
            ps.setLong(1, Long.parseLong(loan.getId()));
            ps.setDouble(2, loan.getDeclaredMonthlyRevenue());
            ps.setDouble(3, loan.getDeclaredMonthlyExpenses());
            ps.setDouble(4, loan.getMaxAllowedDebtRatio());

            assertSingleRowAffected(ps.executeUpdate(), "Detaliile creditului business nu au putut fi salvate.");
        }
    }

    @Override
    public Optional<Loan> findById(String id) throws SQLException {
        String sql = baseSelect() + """
                WHERE l.id = ?
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
    public List<Loan> findAll() throws SQLException {
        String sql = baseSelect() + """
                ORDER BY l.start_date DESC, l.id
                """;

        List<Loan> loans = new ArrayList<>();

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                loans.add(mapRow(rs));
            }
        }

        return loans;
    }

    public List<String> findActiveLoansReport() throws SQLException {
        String sql = """
                SELECT
                    l.id,
                    l.loan_number,
                    l.loan_type,
                    l.linked_account_iban,
                    l.requested_amount,
                    l.remaining_amount,
                    l.status,
                    COALESCE(CONCAT(i.first_name, ' ', i.last_name), b.company_name) AS client_name,
                    a.balance AS linked_account_balance,
                    a.currency AS linked_account_currency
                FROM loans l
                JOIN clients c ON l.client_id = c.id
                LEFT JOIN individual_clients i ON c.id = i.client_id
                LEFT JOIN business_clients b ON c.id = b.client_id
                JOIN accounts a ON l.linked_account_iban = a.iban
                WHERE l.status = 'ACTIVE'
                ORDER BY l.start_date DESC, l.id
                """;

        List<String> report = new ArrayList<>();

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                report.add(
                        "LoanId=" + rs.getLong("id") +
                                ", loanNumber=" + rs.getString("loan_number") +
                                ", client=" + rs.getString("client_name") +
                                ", tip=" + rs.getString("loan_type") +
                                ", cont=" + rs.getString("linked_account_iban") +
                                ", soldCont=" + rs.getDouble("linked_account_balance") + " " + rs.getString("linked_account_currency") +
                                ", sumaCeruta=" + rs.getDouble("requested_amount") +
                                ", sumaRamasa=" + rs.getDouble("remaining_amount") +
                                ", status=" + rs.getString("status")
                );
            }
        }

        return report;
    }

    @Override
    public void update(Loan loan) throws SQLException {
        String loanSql = """
                UPDATE loans
                SET loan_number = ?, client_id = ?, linked_account_iban = ?, requested_amount = ?, interest_rate = ?,
                    number_of_months = ?, frequency = ?, remaining_amount = ?, start_date = ?, end_date = ?, status = ?
                WHERE id = ?
                """;

//        Connection connection = getConn();
        try (Connection connection = getConn()) {
            connection.setAutoCommit(false);

            try {
                ensureLoanTypeUnchanged(connection, loan);

                try (PreparedStatement ps = connection.prepareStatement(loanSql)) {
                    ps.setString(1, loan.getLoanNumber());
//                ps.setString(2, loan.getClient().getId());
                    ps.setLong(2, Long.parseLong(loan.getClient().getId()));
                    ps.setString(3, loan.getLinkedAccountIBAN());
                    ps.setDouble(4, loan.getRequestedAmount());
                    ps.setDouble(5, loan.getInterestRate());
                    ps.setInt(6, loan.getNumberOfMonths());
                    ps.setString(7, loan.getFrequency().name());
                    ps.setDouble(8, loan.getRemainingAmount());
                    ps.setDate(9, Date.valueOf(loan.getStartDate()));
                    ps.setDate(10, Date.valueOf(loan.getEndDate()));
                    ps.setString(11, loan.getStatus().name());
//                    ps.setString(12, loan.getId());
                    ps.setLong(12, Long.parseLong(loan.getId()));

                    assertSingleRowAffected(ps.executeUpdate(), "Imprumutul nu a fost gasit pentru update.");
                }

                if (loan instanceof PersonalLoan personalLoan) {
                    updatePersonalLoan(connection, personalLoan);
                } else if (loan instanceof BusinessLoan businessLoan) {
                    updateBusinessLoan(connection, businessLoan);
                } else {
                    throw new SQLException("Tip de imprumut necunoscut.");
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

    private void updatePersonalLoan(Connection connection, PersonalLoan loan) throws SQLException {
        String sql = """
                UPDATE personal_loans
                SET declared_monthly_income = ?, max_allowed_debt_ratio = ?
                WHERE loan_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, loan.getDeclaredMonthlyIncome());
            ps.setDouble(2, loan.getMaxAllowedDebtRatio());
//            ps.setString(3, loan.getId());
            ps.setLong(3, Long.parseLong(loan.getId()));

            assertSingleRowAffected(ps.executeUpdate(), "Creditul personal nu a fost gasit pentru update.");
        }
    }

    private void updateBusinessLoan(Connection connection, BusinessLoan loan) throws SQLException {
        String sql = """
                UPDATE business_loans
                SET declared_monthly_revenue = ?, declared_monthly_expenses = ?, max_allowed_debt_ratio = ?
                WHERE loan_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, loan.getDeclaredMonthlyRevenue());
            ps.setDouble(2, loan.getDeclaredMonthlyExpenses());
            ps.setDouble(3, loan.getMaxAllowedDebtRatio());
//            ps.setString(4, loan.getId());
            ps.setLong(4, Long.parseLong(loan.getId()));

            assertSingleRowAffected(ps.executeUpdate(), "Creditul business nu a fost gasit pentru update.");
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String deletePersonalSql = """
                DELETE FROM personal_loans
                WHERE loan_id = ?
                """;

        String deleteBusinessSql = """
                DELETE FROM business_loans
                WHERE loan_id = ?
                """;

        String deleteLoanSql = """
                DELETE FROM loans
                WHERE id = ?
                """;

//        Connection connection = getConn();
        try (Connection connection = getConn()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement ps = connection.prepareStatement(deletePersonalSql)) {
//                    ps.setString(1, id);
                    ps.setLong(1, Long.parseLong(id));
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = connection.prepareStatement(deleteBusinessSql)) {
//                    ps.setString(1, id);
                    ps.setLong(1, Long.parseLong(id));
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = connection.prepareStatement(deleteLoanSql)) {
//                    ps.setString(1, id);
                    ps.setLong(1, Long.parseLong(id));
                    assertSingleRowAffected(ps.executeUpdate(), "Imprumutul nu a fost gasit pentru stergere.");
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

    public List<Loan> findByClientId(String clientId) throws SQLException {
        String sql = baseSelect() + """
                WHERE l.client_id = ?
                ORDER BY l.start_date DESC, l.id
                """;

        List<Loan> loans = new ArrayList<>();

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setString(1, clientId);
            ps.setLong(1, Long.parseLong(clientId));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    loans.add(mapRow(rs));
                }
            }
        }

        return loans;
    }

    private String baseSelect() {
        return """
                SELECT
                    l.id,
                    l.loan_number,
                    l.client_id,
                    l.linked_account_iban,
                    l.loan_type,
                    l.requested_amount,
                    l.interest_rate,
                    l.number_of_months,
                    l.frequency,
                    l.remaining_amount,
                    l.start_date,
                    l.end_date,
                    l.status,
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

                    pl.declared_monthly_income,
                    pl.max_allowed_debt_ratio AS personal_max_allowed_debt_ratio,

                    bl.declared_monthly_revenue,
                    bl.declared_monthly_expenses,
                    bl.max_allowed_debt_ratio AS business_max_allowed_debt_ratio

                FROM loans l
                JOIN clients c ON l.client_id = c.id
                LEFT JOIN individual_clients i ON c.id = i.client_id
                LEFT JOIN business_clients b ON c.id = b.client_id
                LEFT JOIN personal_loans pl ON l.id = pl.loan_id
                LEFT JOIN business_loans bl ON l.id = bl.loan_id
                """;
    }

    private Loan mapRow(ResultSet rs) throws SQLException {
        Client client = mapClient(rs);
        LoanType loanType = LoanType.valueOf(rs.getString("loan_type"));

        Loan loan;
        if (loanType == LoanType.PERSONAL) {
            loan = new PersonalLoan(
                    client,
                    rs.getDouble("requested_amount"),
                    rs.getString("linked_account_iban"),
                    rs.getInt("number_of_months"),
                    Frequency.valueOf(rs.getString("frequency")),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getDouble("interest_rate"),
                    rs.getDouble("declared_monthly_income"),
                    rs.getDouble("personal_max_allowed_debt_ratio")
            );
        } else if (loanType == LoanType.BUSINESS) {
            loan = new BusinessLoan(
                    client,
                    rs.getDouble("requested_amount"),
                    rs.getString("linked_account_iban"),
                    rs.getInt("number_of_months"),
                    Frequency.valueOf(rs.getString("frequency")),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getDouble("interest_rate"),
                    rs.getDouble("declared_monthly_revenue"),
                    rs.getDouble("declared_monthly_expenses"),
                    rs.getDouble("business_max_allowed_debt_ratio")
            );
        } else {
            throw new SQLException("Tip de imprumut invalid: " + loanType);
        }

//        loan.setId(rs.getString("id"));
        loan.setId(String.valueOf(rs.getLong("id")));
        loan.setLoanNumber(rs.getString("loan_number"));
        loan.setRemainingAmount(rs.getDouble("remaining_amount"));
        loan.setEndDate(rs.getDate("end_date").toLocalDate());
        loan.setStatus(LoanStatus.valueOf(rs.getString("status")));
        return loan;
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

        throw new SQLException("Tip de client invalid pentru imprumut: " + clientType);
    }

    private void ensureLoanTypeUnchanged(Connection connection, Loan loan) throws SQLException {
        String sql = """
                SELECT loan_type
                FROM loans
                WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setString(1, loan.getId());
            ps.setLong(1, Long.parseLong(loan.getId()));

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Imprumutul nu a fost gasit pentru update.");
                }

                LoanType existingType = LoanType.valueOf(rs.getString("loan_type"));
                if (existingType != loan.getLoanType()) {
                    throw new SQLException("Tipul imprumutului nu poate fi schimbat.");
                }
            }
        }
    }

    private void assertSingleRowAffected(int affectedRows, String errorMessage) throws SQLException {
        if (affectedRows != 1) {
            throw new SQLException(errorMessage);
        }
    }

    private void setGeneratedId(Loan loan, PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (!rs.next()) {
                throw new SQLException("ID-ul generat pentru imprumut nu a putut fi citit.");
            }
            loan.setId(String.valueOf(rs.getLong(1)));
        }
    }
}
