package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;

public class App {

    public static void main(String[] args) {

        // connect to the database
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/northwind");
        dataSource.setUsername("root");
        dataSource.setPassword("yearup26");

        //the query
        String sql = """
                select
                   ProductName
                from
                    products
                """;


        // Try-with-resources to ensure the connection and statement close automatically
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("Product Names from Northwind:");
            System.out.println("-----------------------------");

            // Loop through the result set and display each product name
            while (rs.next()) {
                String productName = rs.getString("ProductName");
                System.out.println(productName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }

