package com.pao.project.repository;

import com.pao.project.model.BusinessClient;
import com.pao.project.model.Client;
import com.pao.project.model.ClientType;
import com.pao.project.model.IndividualClient;
import com.pao.project.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements Repository<Client, String> {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Client client) throws SQLException {
//        String clientSql = """
//                INSERT INTO clients (id, client_type, address, email, phone)
//                VALUES (?, ?, ?, ?, ?)
//                """;
        String clientSql = """
                INSERT INTO clients (client_type, address, email, phone)
                VALUES (?, ?, ?, ?)
                """;

//        Connection connection = getConn();
        try (Connection connection = getConn()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement ps = connection.prepareStatement(clientSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
//                ps.setString(1, client.getId());
//                ps.setString(2, client.getClientType().name());
//                ps.setString(3, client.getAddress());
//                ps.setString(4, client.getEmail());
//                ps.setString(5, client.getPhone());
                    ps.setString(1, client.getClientType().name());
                    ps.setString(2, client.getAddress());
                    ps.setString(3, client.getEmail());
                    ps.setString(4, client.getPhone());

                    assertSingleRowAffected(ps.executeUpdate(), "Clientul nu a putut fi salvat.");
                    setGeneratedId(client, ps);
                }

                if (client instanceof IndividualClient individualClient) {
                    saveIndividualClient(connection, individualClient);
                } else if (client instanceof BusinessClient businessClient) {
                    saveBusinessClient(connection, businessClient);
                } else {
                    throw new SQLException("Tip de client necunoscut.");
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

    private void saveIndividualClient(Connection connection, IndividualClient client) throws SQLException {
        String sql = """
                INSERT INTO individual_clients
                (client_id, first_name, last_name, cnp, occupation, date_of_birth, monthly_income)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setString(1, client.getId());
            ps.setLong(1, Long.parseLong(client.getId()));
            ps.setString(2, client.getFirstName());
            ps.setString(3, client.getLastName());
            ps.setString(4, client.getCNP());
            ps.setString(5, client.getOccupation());
            ps.setString(6, client.getDateOfBirth());
            ps.setDouble(7, client.getMonthlyIncome());

            assertSingleRowAffected(ps.executeUpdate(), "Detaliile clientului individual nu au putut fi salvate.");
        }
    }

    private void saveBusinessClient(Connection connection, BusinessClient client) throws SQLException {
        String sql = """
                INSERT INTO business_clients
                (client_id, company_name, cui, contact_person, monthly_revenue, monthly_expenses)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setString(1, client.getId());
            ps.setLong(1, Long.parseLong(client.getId()));
            ps.setString(2, client.getCompanyName());
            ps.setString(3, client.getCUI());
            ps.setString(4, client.getContactPerson());
            ps.setDouble(5, client.getMonthlyRevenue());
            ps.setDouble(6, client.getMonthlyExpenses());

            assertSingleRowAffected(ps.executeUpdate(), "Detaliile clientului business nu au putut fi salvate.");
        }
    }

    @Override
    public Optional<Client> findById(String id) throws SQLException {
        String sql = """
                SELECT
                    c.id,
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
                    b.monthly_expenses

                FROM clients c
                LEFT JOIN individual_clients i ON c.id = i.client_id
                LEFT JOIN business_clients b ON c.id = b.client_id
                WHERE c.id = ?
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
    public List<Client> findAll() throws SQLException {
        String sql = """
                SELECT
                    c.id,
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
                    b.monthly_expenses

                FROM clients c
                LEFT JOIN individual_clients i ON c.id = i.client_id
                LEFT JOIN business_clients b ON c.id = b.client_id
                ORDER BY c.id
                """;

        List<Client> clients = new ArrayList<>();

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clients.add(mapRow(rs));
            }
        }

        return clients;
    }

    public List<String> findClientsWithAccountCountReport() throws SQLException {
        String sql = """
                SELECT
                    c.id,
                    c.client_type,
                    COALESCE(CONCAT(i.first_name, ' ', i.last_name), b.company_name) AS client_name,
                    COUNT(a.id) AS account_count
                FROM clients c
                LEFT JOIN individual_clients i ON c.id = i.client_id
                LEFT JOIN business_clients b ON c.id = b.client_id
                LEFT JOIN accounts a ON c.id = a.client_id
                GROUP BY c.id, c.client_type, client_name
                ORDER BY account_count DESC, c.id
                """;

        List<String> report = new ArrayList<>();

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                report.add(
                        "ClientId=" + rs.getLong("id") +
                                ", nume=" + rs.getString("client_name") +
                                ", tip=" + rs.getString("client_type") +
                                ", numarConturi=" + rs.getLong("account_count")
                );
            }
        }

        return report;
    }

    @Override
    public void update(Client client) throws SQLException {
        String clientSql = """
                UPDATE clients
                SET address = ?, email = ?, phone = ?
                WHERE id = ?
                """;

//        Connection connection = getConn();
        try (Connection connection = getConn()) {
            connection.setAutoCommit(false);

            try {
                ensureClientTypeUnchanged(connection, client);

                try (PreparedStatement ps = connection.prepareStatement(clientSql)) {
                    ps.setString(1, client.getAddress());
                    ps.setString(2, client.getEmail());
                    ps.setString(3, client.getPhone());
//                ps.setString(4, client.getId());
                    ps.setLong(4, Long.parseLong(client.getId()));

                    assertSingleRowAffected(ps.executeUpdate(), "Clientul nu a fost gasit pentru update.");
                }

                if (client instanceof IndividualClient individualClient) {
                    updateIndividualClient(connection, individualClient);
                } else if (client instanceof BusinessClient businessClient) {
                    updateBusinessClient(connection, businessClient);
                } else {
                    throw new SQLException("Tip de client necunoscut.");
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

    private void updateIndividualClient(Connection connection, IndividualClient client) throws SQLException {
        String sql = """
                UPDATE individual_clients
                SET first_name = ?, last_name = ?, cnp = ?, occupation = ?, date_of_birth = ?, monthly_income = ?
                WHERE client_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, client.getFirstName());
            ps.setString(2, client.getLastName());
            ps.setString(3, client.getCNP());
            ps.setString(4, client.getOccupation());
            ps.setString(5, client.getDateOfBirth());
            ps.setDouble(6, client.getMonthlyIncome());
//            ps.setString(7, client.getId());
            ps.setLong(7, Long.parseLong(client.getId()));

            assertSingleRowAffected(ps.executeUpdate(), "Clientul individual nu a fost gasit pentru update.");
        }
    }

    private void updateBusinessClient(Connection connection, BusinessClient client) throws SQLException {
        String sql = """
                UPDATE business_clients
                SET company_name = ?, cui = ?, contact_person = ?, monthly_revenue = ?, monthly_expenses = ?
                WHERE client_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, client.getCompanyName());
            ps.setString(2, client.getCUI());
            ps.setString(3, client.getContactPerson());
            ps.setDouble(4, client.getMonthlyRevenue());
            ps.setDouble(5, client.getMonthlyExpenses());
//            ps.setString(6, client.getId());
            ps.setLong(6, Long.parseLong(client.getId()));

            assertSingleRowAffected(ps.executeUpdate(), "Clientul business nu a fost gasit pentru update.");
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String deleteIndividualSql = """
                DELETE FROM individual_clients
                WHERE client_id = ?
                """;

        String deleteBusinessSql = """
                DELETE FROM business_clients
                WHERE client_id = ?
                """;

        String deleteClientSql = """
                DELETE FROM clients
                WHERE id = ?
                """;

//        Connection connection = getConn();
        try (Connection connection = getConn()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement ps = connection.prepareStatement(deleteIndividualSql)) {
//                ps.setString(1, id);
                    ps.setLong(1, Long.parseLong(id));
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = connection.prepareStatement(deleteBusinessSql)) {
//                ps.setString(1, id);
                    ps.setLong(1, Long.parseLong(id));
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = connection.prepareStatement(deleteClientSql)) {
//                ps.setString(1, id);
                    ps.setLong(1, Long.parseLong(id));
                    assertSingleRowAffected(ps.executeUpdate(), "Clientul nu a fost gasit pentru stergere.");
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

    private Client mapRow(ResultSet rs) throws SQLException {
//        String id = rs.getString("id");
        String id = String.valueOf(rs.getLong("id"));
        String clientTypeText = rs.getString("client_type");
        String address = rs.getString("address");
        String email = rs.getString("email");
        String phone = rs.getString("phone");

        ClientType clientType = ClientType.valueOf(clientTypeText);

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

        throw new SQLException("Tip de client invalid: " + clientTypeText);
    }

    private void ensureClientTypeUnchanged(Connection connection, Client client) throws SQLException {
        String sql = """
                SELECT client_type
                FROM clients
                WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setString(1, client.getId());
            ps.setLong(1, Long.parseLong(client.getId()));

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Clientul nu a fost gasit pentru update.");
                }

                ClientType existingType = ClientType.valueOf(rs.getString("client_type"));
                if (existingType != client.getClientType()) {
                    throw new SQLException("Tipul clientului nu poate fi schimbat.");
                }
            }
        }
    }

    private void assertSingleRowAffected(int affectedRows, String errorMessage) throws SQLException {
        if (affectedRows != 1) {
            throw new SQLException(errorMessage);
        }
    }

    private void setGeneratedId(Client client, PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (!rs.next()) {
                throw new SQLException("ID-ul generat pentru client nu a putut fi citit.");
            }
            client.setId(String.valueOf(rs.getLong(1)));
        }
    }

}
