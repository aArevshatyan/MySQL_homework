package com.paypal.desk;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbHelper {

    private static Connection connection = getConnection();

    private static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/paypal",
                    "root",
                    "root"
            );

            System.out.println("Connection successful");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static int createUser(String firstName, String lastName) {
        String sql = "insert into users " +
                "(first_name, last_name)" +
                " values (" +
                "'" + firstName + "'" +
                ", " +
                "'" + lastName + "'" +
                ")";

        try {

            Statement statement = connection.createStatement();
            statement.execute(sql);

            String idSql = "select max(id) from users";
            Statement idStatement = connection.createStatement();
            ResultSet resultSet = idStatement.executeQuery(idSql);

            resultSet.next();

            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Updates the user balance in database
     * Sets balance = balance + amount
     *
     * @param userId id of the user in users table
     * @param amount double value of the amount to insert
     */
    static void cashFlow(int userId, double amount) {
        try {
            String sqlBalance = "select balance from users where id = " + userId;

            Statement idStatement = connection.createStatement();
            ResultSet resultSet = idStatement.executeQuery(sqlBalance);

            resultSet.next();

            double oldBalance = resultSet.getInt(1);
            String sqlUpdate = "update users set balance = " + (oldBalance + amount) + "where id = " + userId;

            Statement statement = connection.createStatement();
            statement.execute(sqlUpdate);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Emulates a transaction between 2 users
     * Takes money from one account and adds to another account
     *
     * @param userFrom source user id
     * @param userTo   target user id
     * @param amount   transaction amount
     */
    static int transaction(int userFrom, int userTo, double amount) {
        String sql = "insert into transactions " +
                "(user_from, user_to, transaction_amount)" +
                " values (" +
                "'" + userFrom + "'" +
                ", " +
                "'" + userTo + "'" +
                ", " +
                "'" + amount + "'" +
                ")";

        try {

            Statement statement = connection.createStatement();
            statement.execute(sql);

            cashFlow(userFrom, -amount);
            cashFlow(userTo, amount);


            String idSql = "select max(id) from users";
            Statement idStatement = connection.createStatement();
            ResultSet resultSet = idStatement.executeQuery(idSql);


            resultSet.next();

            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    static List<User> listUsers() {
        String sql = "select * from users";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            List<User> userList = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                double balance = resultSet.getDouble("balance");

                userList.add(new User(
                        id, firstName, lastName, balance
                ));
            }
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
