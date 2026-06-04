package com.pao.project.repository;

import com.pao.project.model.BusinessClient;
import com.pao.project.model.Client;
import com.pao.project.model.ClientType;
import com.pao.project.model.IndividualClient;
import com.pao.project.model.User;
import com.pao.project.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements Repository<User, String> {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(User user) throws SQLException {
//        String sql = """
//                INSERT INTO users (id, username, password, role, client_id)
//                VALUES (?, ?, ?, ?, ?)
//                """;
        String sql = """
                INSERT INTO users (username, password, role, client_id)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
//            ps.setString(1, user.getId());
//            ps.setString(2, user.getUsername());
//            ps.setString(3, user.getPassword());
//            ps.setString(4, user.getRole());
//            ps.setString(5, user.getClient().getId());
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setLong(4, Long.parseLong(user.getClient().getId()));

            assertSingleRowAffected(ps.executeUpdate(), "Utilizatorul nu a putut fi salvat.");
            setGeneratedId(user, ps);
        }
    }

    @Override
    public Optional<User> findById(String id) throws SQLException {
        String sql = baseSelect() + """
                WHERE u.id = ?
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

    public Optional<User> findByUsername(String username) throws SQLException {
        String sql = baseSelect() + """
                WHERE u.username = ?
                """;

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<User> findAll() throws SQLException {
        String sql = baseSelect() + """
                ORDER BY u.id
                """;

        List<User> users = new ArrayList<>();

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapRow(rs));
            }
        }

        return users;
    }

    @Override
    public void update(User user) throws SQLException {
        String sql = """
                UPDATE users
                SET username = ?, password = ?, role = ?, client_id = ?
                WHERE id = ?
                """;

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
//            ps.setString(4, user.getClient().getId());
//            ps.setString(5, user.getId());
            ps.setLong(4, Long.parseLong(user.getClient().getId()));
            ps.setLong(5, Long.parseLong(user.getId()));

            assertSingleRowAffected(ps.executeUpdate(), "Utilizatorul nu a fost gasit pentru update.");
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = """
                DELETE FROM users
                WHERE id = ?
                """;

        try (Connection connection = getConn();
             PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setString(1, id);
            ps.setLong(1, Long.parseLong(id));

            assertSingleRowAffected(ps.executeUpdate(), "Utilizatorul nu a fost gasit pentru stergere.");
        }
    }

    private String baseSelect() {
        return """
                SELECT
                    u.id,
                    u.username,
                    u.password,
                    u.role,
                    u.client_id,
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
                
                FROM users u
                JOIN clients c ON u.client_id = c.id
                LEFT JOIN individual_clients i ON c.id = i.client_id
                LEFT JOIN business_clients b ON c.id = b.client_id
                """;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        Client client = mapClient(rs);

        User user = new User(
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role"),
                client
        );
//        user.setId(rs.getString("id"));
        user.setId(String.valueOf(rs.getLong("id")));
        return user;
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

        throw new SQLException("Tip de client invalid pentru utilizator: " + clientType);
    }

    private void assertSingleRowAffected(int affectedRows, String errorMessage) throws SQLException {
        if (affectedRows != 1) {
            throw new SQLException(errorMessage);
        }
    }

    private void setGeneratedId(User user, PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (!rs.next()) {
                throw new SQLException("ID-ul generat pentru utilizator nu a putut fi citit.");
            }
            user.setId(String.valueOf(rs.getLong(1)));
        }
    }
}
