package com.example.knowledge_db.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class CRUDHelper {
    public static ResultSet readAllFromMain() throws SQLException {
        Statement statement = null;
        try {
            statement = Objects.requireNonNull(SQLiteDB.connect()).createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return statement.executeQuery("SELECT * FROM 'main'");
    }

    public static ResultSet findAllLike(String str) throws SQLException {
        Statement statement = null;
        try {
            statement = Objects.requireNonNull(SQLiteDB.connect()).createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return statement.executeQuery("SELECT * FROM main Where body LIKE '%" + str + "%';");
    }

    public static void addNewItem(String title, String body) throws SQLException {
        Statement statement = null;
        try {
            statement = Objects.requireNonNull(SQLiteDB.connect()).createStatement();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String ts = sdf.format(timestamp);
            String sql = "INSERT INTO main (title, body, _date) VALUES ('" + title +
                    "', '" + body +
                    "'," + ts + ");";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void deleteItem(Integer id) throws SQLException {
        Statement statement = null;
        try {
            statement = Objects.requireNonNull(SQLiteDB.connect()).createStatement();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String ts = sdf.format(timestamp);
            String sql = "DELETE FROM main WHERE id=" + id + (";");
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


