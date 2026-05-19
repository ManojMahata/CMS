package com.campusflow.database;

import java.sql.*;

/**
 * MarksDAO to handle marks database operations
 */

public class MarksDAO {

    /**
     * add marks for a student
     */

    public boolean addMarks(String studentRoll, String subjectCode, String exampType, double marksObtained, double totalMarks, String enteredBy) {
        
        String sql = "insert into marks( student_roll, subject_code, exam_type, " +
        "marks_obtained, total_marks, entered_by, entry_date) " +
        "values (?, ?, ?, ?, ?, ?, now())";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
                pstmt.setString(1, studentRoll);
                pstmt.setString(2, subjectCode);
                pstmt.setString(3, exampType);
                pstmt.setDouble(4, marksObtained);
                pstmt.setDouble(5, totalMarks);
                pstmt.setString(6, enteredBy);

                int rowsAffected = pstmt.executeUpdate();

                if(rowsAffected > 0) {
                    System.out.println("Marks added for: " + studentRoll);
                    return true;
                }

            } catch (SQLException e) {
                System.err.println("Error adding marks: " + e.getMessage());
                e.printStackTrace();
            }

            return false;
    
    }// addMarks method ends here



}