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
                System.out.println(" ");
                System.out.print("""
                        What do you want to do?
                        
                        1) Display All Products
                        2) Display all Customers
                        3) Display all Categories
                        0) Exit application
                        Select an option : 
                        """);

                // user's chose
                switch (input.nextInt()) {
                    case 1:
                        displayAllProducts(connection);
                        break;
                    case 2:
                        displayAllCustomers(connection);
                        break;
                    case 3:
                        displayAllCategories(connection);
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
                             UnitsInStock
                         from
                             products;
                """;


        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet results = stmt.executeQuery()) {
            System.out.printf("%-5s %-35s %-10s %-10s%n",
                    "ID", "Product Name", "Price", "Stock");
            System.out.println("----------------------------------------------------------------");

            while (results.next()) {
                System.out.printf("%-5d %-35s $%-9.2f %-10d%n",
                        results.getInt("ProductID"),
                        results.getString("ProductName"),
                        results.getDouble("UnitPrice"),
                        results.getInt("UnitsInStock"));

            }
            } catch(SQLException e){
                System.out.println("can't get all Products " + e.getMessage());
            }
        }

    public static void displayAllCustomers(Connection connection) {

        String sql = """
                         select
                             ContactName,
                             CompanyName,
                             City,
                             Country,
                             Phone
                         from
                             customers
                             order by 
                                    Country;
                      """;


        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet results = stmt.executeQuery()) {
            System.out.printf("%-25s %-35s %-20s %-20s %-20s%n",
                    "ContactName", "CompanyName", "City", "Country", "Phone");
            System.out.println("----------------------------------------------------------------");

            while (results.next()) {
                System.out.printf("%-25s %-35s %-20s %-20s %-20s%n",
                        results.getString("ContactName"),
                        results.getString("CompanyName"),
                        results.getString("City"),
                        results.getString("Country"),
                        results.getString("Phone")
                );

            }
        } catch(SQLException e){
            System.out.println("can't get all Customers " + e.getMessage());
        }
    }

    public static void displayAllCategories(Connection connection){

        String sql = """
                         select
                             CategoryID,
                             CategoryName,
                             Description,
                             Picture
                         from
                             categories
                      """;


        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet results = stmt.executeQuery()) {
            System.out.printf("%-25s %-35s %-20s %-20s",
                    "CategoryID", "CategoryName", "Description", "Picture");
            System.out.println("----------------------------------------------------------------");

            while (results.next()) {
                System.out.printf("%-25s %-35s %-20s %-20s",
                        results.getString("CategoryID"),
                        results.getString("CategoryName"),
                        results.getString("Description"),
                        results.getString("Picture")
                );

            }
        } catch(SQLException e){
            System.out.println("can't get all Customers " + e.getMessage());
        }
    }

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

        }}
}



