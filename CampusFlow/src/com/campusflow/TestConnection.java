package com.campusflow;

import com.campusflow.database.DatabaseConnection;
import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {
        
        System.out.println("Testing database connection...");

        // get connection
        Connection conn = DatabaseConnection.getConnection();

        if(conn != null){
            System.out.println("Successfully connected to MySQL.");
            
            // close connection when done
            DatabaseConnection.closeConnection();
        } else {
            System.out.println("Failed to connect with database;");
        }

    }// main method ends here
}// main class ends here