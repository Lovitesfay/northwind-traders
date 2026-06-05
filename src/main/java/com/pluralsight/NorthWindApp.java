package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.Scanner;

public class NorthWindApp {

    public static void main(String[] args) throws SQLException {


        if (args.length != 2) {
            System.out.println("Application needs two args to run: A username and a password for the db");
            System.exit(1);

        }

        String username = args[0];
        String password = args[1];

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/northwind");
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        Scanner input = new Scanner(System.in);

        try
                (Connection connection = dataSource.getConnection()) {

            while (true) {
                // menu
                System.out.println("""
                        What do you want to do?
                            1) Display All Products
                            0) Exit application
                        """);

                // user's chose
                switch (input.nextInt()) {
                    case 1:
                        displayAllProducts(connection);
                        break;
                    case 0:
                        System.out.println("See you Later");
                        System.exit(0);
                    default:
                        System.out.println("Invalid Selection");
                }
            }


        } catch (SQLException e) {
            System.out.println("unable to connect to the database ");
            System.exit(1);
        }

    }

    public static void displayAllProducts(Connection connection) {

        String sql = """
                         select
                             ProductID,
                             ProductName,
                             UnitPrice,
                             UnitsInStock,
                             UnitsInOrder
                         from
                             products;
                """;


        try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet results = stmt.executeQuery();
        ){  printResults(results);


        } catch (SQLException e) {
            System.out.println("can't get all Products " + e.getMessage());
        }}

    //this method will be used in the displayMethods to actually print the results to the screen
    public static void printResults(ResultSet results) throws SQLException {
        //get the meta data so we have access to the field names
        ResultSetMetaData metaData = results.getMetaData();
        //get the number of rows returned
        int columnCount = metaData.getColumnCount();

        //this is looping over all the results from the DB
        while (results.next()) {

            //loop over each column in the rown and display the data
            for (int i = 1; i <= columnCount; i++) {
                //gets the current colum name
                String columnName = metaData.getColumnName(i);
                //get the current column value
                String value = results.getString(i);
                //print out the column name and column value
                System.out.println(columnName + ": " + value + " ");
            }

            //print an empty line to make the results prettier
            System.out.println();

        }}}
