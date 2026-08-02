package com.campusflow.database;

import com.campusflow.model.Administrator;
import com.campusflow.utils.PasswordHasher;
import java.sql.*;

/**
 * AdminDOA = database operations for administrators
 */

public class AdminDAO {

    // validate admin login
    public Administrator validateLogin(String username, String password) {
        
        String sql = "select * from administrators where username =?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                String storedHash = rs.getString("password_hash");

                // verifyPassword
                if (PasswordHasher.verifyPassword(password, storedHash)) {
                    System.out.println("Admin Login successfull: " + username);

                    return new Administrator(
                    rs.getString("admin_id"), 
                    rs.getString("name"), 
                    rs.getString("email"), 
                    rs.getString("username"),
                    null
                    );
                } else {
                    System.out.println("Invalid admin password");
                }
                
            } else {
                System.out.println("Admin username ont found");
            }


        } catch (SQLException e) {
            System.err.println("Error during admin login: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

}// main class ends here