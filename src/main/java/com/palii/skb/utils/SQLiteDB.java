package com.example.knowledge_db.utils;

import com.example.knowledge_db.MainApplication;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLiteDB {

    private static final String location = Objects.requireNonNull(MainApplication.class.getResource("/database/kdb.db")).toExternalForm();
    private static final String requiredTable = "main";

    private static boolean checkDrivers() {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new org.sqlite.JDBC());
            return true;
        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not start SQLite Drivers");
            return false;
        }
    }

    protected static Connection connect() {
        String dbPrefix = "jdbc:sqlite:";
        Connection connection;
        try {
            //Протокол jdbc для SQLite. Идентификатор jdbc:sqlite: плюс размещение БД
            connection = DriverManager.getConnection(dbPrefix + location);
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(Level.SEVERE,
                    LocalDateTime.now() + ": Could not connect to SQLite DB at " + location);
            return null;
        }
        return connection;
    }

    //Проверка наличия файла
    private static boolean checkConnection() {
        try (Connection connection = connect()) {
            return connection != null;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not connect to     database");
            return false;
        }
    }

    private static boolean checkTables() {
        String checkTables =
                "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + requiredTable + "'";
        try (Connection connection = SQLiteDB.connect()) {
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(checkTables);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (rs.getString("tbl_name").equals(requiredTable)) return true;
            }
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not find tables in database");
            return false;
        }
        return false;
    }

    public static boolean isOK() {
        if (!checkDrivers()) return false; //driver errors
        if (!checkConnection()) return false; //can't connect to db
        return checkTables(); //tables didn't exist
    }
}
