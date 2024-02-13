package com.palii.skb.utils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLiteDB {

    private static final String requiredTable = "main";
    private static final String dbPrefix = "jdbc:sqlite:";
    private static final String DB_NAME = Objects.requireNonNull("kdb.db");
//    private static final String CREATE_DB = "CREATE TABLE \"main\" (\n" +
//            "\t\"id\"\tINTEGER NOT NULL UNIQUE,\n" +
//            "\t\"title\"\tTEXT NOT NULL,\n" +
//            "\t\"body\"\tTEXT,\n" +
//            "\t\"_date\"\tINTEGER NOT NULL,\n" +
//            "\t\"use_count\"\tINTEGER NOT NULL,\n" +
//            "\t\"edit_date\"\tINTEGER,\n" +
//            "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
//            ")";

     private static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS \"main\" (\n" +
             "\t\"id\"\tINTEGER NOT NULL UNIQUE,\n" +
             "\t\"title\"\tTEXT NOT NULL,\n" +
             "\t\"body\"\tTEXT,\n" +
             "\t\"_date\"\tTEXT NOT NULL,\n" +
             "\t\"use_count\"\tINTEGER NOT NULL,\n" +
             "\t\"edit_date\"\tTEXT,\n" +
             "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
             ")";


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
    public static void createNewDatabase() {

        String url = dbPrefix + DB_NAME;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void createNewTable() {
        // SQLite connection string
        String url = dbPrefix + DB_NAME;
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(CREATE_DB);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    protected static Connection connect() {
        Connection connection;
        try {
            //Протокол jdbc для SQLite. Идентификатор jdbc:sqlite: плюс размещение БД
            connection = DriverManager.getConnection(dbPrefix + DB_NAME);
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(Level.SEVERE,
                    LocalDateTime.now() + ": Could not connect to SQLite DB at " + DB_NAME);
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
