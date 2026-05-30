package com.pao.laboratory12;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {

        try {

            Properties props = new Properties();

            FileInputStream fis = new FileInputStream(
                    "src/com/pao/laboratory12/resources/db.properties.template"
            );

            props.load(fis);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            Connection connection =
                    DriverManager.getConnection(url, user, password);

            System.out.println("CONNECTED SUCCESSFULLY!");
            var statement = connection.createStatement();

            var rs = statement.executeQuery("SHOW TABLES");

            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}