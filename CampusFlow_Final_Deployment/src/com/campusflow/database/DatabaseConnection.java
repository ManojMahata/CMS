package com.campusflow.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
    DatabaseConnection - Singleton pattern
    mysql database connection fot the entire application
    only one connection instance exists (efficient resourse usage)
 */

public class DatabaseConnection {

   // database credentials
   private static final String URL = "jdbc:mysql://localhost:3306/campusflow_db";

   private static final String USERNAME = "campususer";
   private static final String PASSWORD = "Campus@123";
   
   // single connection instance "singleton pattern"
   private static Connection connection = null;

   // private constructor (to prevents external instantiation)
   private DatabaseConnection(){

   }

   /*
   database connection 
   */

   public static Connection getConnection(){
    
    try {
        
        if(connection == null || connection.isClosed()){
            // load mysql driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // establish connection
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            System.out.println("Database connected successfully.");

        }// if ends here
    } catch (ClassNotFoundException e){
        System.err.println("MySQL JDBC Driver is not fulnd");
        System.err.println("Make sure mysql-connector..jar is in lib/ folder.");
        e.printStackTrace();
    } catch (SQLException e){
        System.err.println("Database connection failed!!");
        System.err.println("Check: mysql is running or not, credentials are correct, database exists");
        e.printStackTrace();
    }

        return connection;
   }// getConnection method ends here

   // close database connection when application is closed

   public static void closeConnection() {
    try {
        if(connection != null && !connection.isClosed()){
            connection.close();
            System.out.println("Database connection closed.");
        }// if ends here
    }
    catch (SQLException e){
        System.err.println("Error closing connection");
        e.printStackTrace();
    }
   }// closeConnection method ends here
   
}// main class ends here