package org.example;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/student_management_system";
        String user = "root";
        String password = "root"; // <-- change this

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected successfully");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}