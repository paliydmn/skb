package com.palii.skb.utils;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class CRUDHelper {
    public static ResultSet readAllFromMain() {
        try {
            Statement statement = Objects.requireNonNull(SQLiteDB.connect()).createStatement();
            return statement.executeQuery("SELECT * FROM 'main'");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet findAllInDb() {
        Statement statement = null;
        try {
            statement = Objects.requireNonNull(SQLiteDB.connect()).createStatement();
            return statement.executeQuery("SELECT * FROM main;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet findAllLikeBy(String searchStr, String byColumn) {
        Statement statement = null;
        try {
            statement = Objects.requireNonNull(SQLiteDB.connect()).createStatement();
            String sql = "SELECT * FROM main Where ";

            PreparedStatement ps = null;
            switch (byColumn) {
                case "byTitle":
                    sql = sql + " title Like '%" +
                            searchStr +
                            "%';";
                    break;
                case "byBody":
                    sql = sql + " body Like '%" +
                            searchStr +
                            "%'";
                    break;
                case "byBody&Title":
                    sql = sql + " title Like '%" +
                            searchStr +
                            "%' OR body Like '%" +
                            searchStr +
                            "%';";
                    break;
                default:
                    sql = sql + " body Like '%" + searchStr + "%'";
            }

            assert statement != null;
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addNewItem(String title, String body) {
        Statement statement = null;
        try {
            statement = Objects.requireNonNull(SQLiteDB.connect()).createStatement();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String ts = sdf.format(timestamp);
            String sql = "INSERT INTO main (title, body, _date, use_count) VALUES ('" + title +
                    "', '" + body +
                    "','" + ts + "'," + 0 +
                    ");";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteItem(Integer id) {
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

    public static void updateUsageCount(int id, int useC) {
        Statement statement = null;
        try {
            statement = Objects.requireNonNull(SQLiteDB.connect()).createStatement();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String ts = sdf.format(timestamp);
            String sql = "UPDATE main SET use_count=" +
                    useC +
                    " WHERE id=" + id + (";");
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int updateBody(String body, int id, String eDate) {
        Statement statement = null;
        try {
            statement = Objects.requireNonNull(SQLiteDB.connect()).createStatement();

//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//            String ts = sdf.format(timestamp);
            String sql = "UPDATE main SET body='" +
                    body + "', edit_date='" + eDate +
                    "' WHERE id=" + id + (";");
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


