package com.pao.project;

import com.pao.project.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDBConnection {
    public static void main(String[] args) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();

            System.out.println("Conexiunea la baza de date functioneaza.");

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SHOW TABLES")) {

                System.out.println("Tabele gasite:");

                while (resultSet.next()) {
                    System.out.println(resultSet.getString(1));
                }
            }

        } catch (SQLException e) {
            System.out.println("Eroare SQL: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Eroare: " + e.getMessage());
            e.printStackTrace();
        }
    }
}