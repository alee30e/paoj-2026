package com.pao.project.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {
    private static DatabaseConnection instance;

    private final String url;
    private final String user;
    private final String password;
//    private Connection connection;

    private DatabaseConnection() {
        Properties properties = loadProperties();
        this.url = properties.getProperty("db.url");
        this.user = properties.getProperty("db.user");
        this.password = properties.getProperty("db.password");

        if (url == null || user == null || password == null) {
            throw new IllegalStateException("Fisierul db.properties nu contine toate cheile necesare.");
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public synchronized Connection getConnection() {
        try {
//            if (connection == null || connection.isClosed()) {
//                connection = DriverManager.getConnection(url, user, password);
//            }
//            return connection;
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Conexiunea la baza de date nu a putut fi creata.", e);
        }
    }

    private Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream = openPropertiesStream()) {
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Fisierul db.properties nu a putut fi citit.", e);
        }
    }

    private InputStream openPropertiesStream() throws IOException {
        InputStream inputStream = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties");

        if (inputStream != null) {
            return inputStream;
        }

        return new FileInputStream("resources/db.properties");
    }
}
