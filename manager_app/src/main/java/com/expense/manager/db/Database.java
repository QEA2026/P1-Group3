package com.expense.manager.db;

import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class Database {
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + getDatabasePath());
    }

    private static String getDatabasePath() {
        return "/app/data/expense_system.db";
    }
}