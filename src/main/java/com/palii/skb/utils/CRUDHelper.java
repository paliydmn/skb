package com.palii.skb.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class CRUDHelper {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static ResultSet readAllFromMain() {
        try {
            Statement statement = Objects.requireNonNull(SQLiteDB.connect()).createStatement();
            return statement.executeQuery("SELECT * FROM 'main'");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet findAllInDb() {
        Statement statement;
        try {
            statement = Objects.requireNonNull(SQLiteDB.connect()).createStatement();
            return statement.executeQuery("SELECT * FROM main;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet findAllLikeBy(String searchStr, String byColumn) {
        Statement statement;
        try {
            statement = Objects.requireNonNull(SQLiteDB.connect()).createStatement();
            String sql = "SELECT * FROM main Where ";

//            sql = switch (byColumn) {
//                case "byTitle" -> sql + " title Like '%" +
//                        searchStr +
//                        "%';";
//                case "byBody" -> sql + " body Like '%" +
//                        searchStr +
//                        "%'";
//                case "byBody&Title" -> sql + " title Like '%" +
//                        searchStr +
//                        "%' OR body Like '%" +
//                        searchStr +
//                        "%';";
//                default -> sql + " body Like '%" + searchStr + "%'";
//            };
            switch (byColumn) {
                case "byTitle": sql =  sql + " title Like '%" +
                        searchStr +
                        "%';";
                case "byBody": sql =  sql + " body Like '%" +
                        searchStr +
                        "%'";
                case "byBody&Title": sql =  sql + " title Like '%" +
                        searchStr +
                        "%' OR body Like '%" +
                        searchStr +
                        "%';";
                default: sql =  sql + " body Like '%" + searchStr + "%'";
            };

            assert statement != null;
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addNewItem(String title, String body) {
        Statement statement;
        try {
            statement = Objects.requireNonNull(SQLiteDB.connect()).createStatement();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String ts = sdf.format(timestamp);
            String sql = "INSERT INTO main (title, body, _date, use_count) VALUES ('" + title.replace("'", "''") +
                    "', '" + body.replace("'", "''") +
                    "','" + ts + "'," + 0 +
                    ");";

            statement.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteItem(Integer id) {
        Statement statement;
        try {
            statement = Objects.requireNonNull(SQLiteDB.connect()).createStatement();
            String sql = "DELETE FROM main WHERE id=" + id + (";");
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateUsageCount(int id, int useC) {
        Statement statement;
        try {
            statement = Objects.requireNonNull(SQLiteDB.connect()).createStatement();
            String sql = "UPDATE main SET use_count=" +
                    useC +
                    " WHERE id=" + id + (";");
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int updateBody(String body, int id, String eDate) {
        Statement statement;
        try {
            statement = Objects.requireNonNull(SQLiteDB.connect()).createStatement();
            String sql = "UPDATE main SET body='" +
                    body.replace("'", "''") + "', edit_date='" + eDate +
                    "' WHERE id=" + id + (";");
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


