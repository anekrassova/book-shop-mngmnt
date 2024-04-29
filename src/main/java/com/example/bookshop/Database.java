package com.example.bookshop;

import java.sql.*;

public class Database {

    private static final String URL = "jdbc:postgresql://localhost:5432/book-shop";
    private static final String USER = "postgres";
    private static final String PASSWORD = "00000000";

    public static Connection connectDb() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("DB connection failed:");
            e.printStackTrace();
        }
        return connection;
    }
}

