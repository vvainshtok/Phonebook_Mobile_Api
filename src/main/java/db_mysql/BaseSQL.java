package db_mysql;

import java.sql.*;
import java.util.Random;

public class BaseSQL {

    static Connection connection;
    static Statement statement;


    static final String URL = "jdbc:mysql://localhost:3306/qa44";
    static final String USER = "root";
    static final String PASSWORD = "gfyxbo0A!";

    public static void main(String[] args) {
        startConnect();
        try {
            int quantity;
            statement = connection.createStatement();


            int id = new Random().nextInt(100) + 20;
            quantity = statement.executeUpdate("INSERT INTO employees (id, name) " +
                    "VALUES ('" + id + "','John');");
            System.out.println(quantity);

            ResultSet resultSet = statement.executeQuery("select * from employees");
            while (resultSet.next()) {
                System.out.print(resultSet.getString("id") + " ");
                System.out.print(resultSet.getString(2) + " ");
                System.out.println(resultSet.getString(3) + " ");
            }
            connection.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    static void startConnect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (!connection.isClosed()) {
                System.out.println("create new connection");
            }
            else {
                System.out.println("connection not created");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
