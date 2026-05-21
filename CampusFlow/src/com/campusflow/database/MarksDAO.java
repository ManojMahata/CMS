package com.campusflow.database;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    /** 
     * get marks by subject and exam type
     */
    /**
 * Get marks bbby subject and exam type
 */
public List<Object[]> getMarksBySubjectAndExam(String subjectCode, String examType) {
    List<Object[]> marksList = new ArrayList<>();
    
    String sql = "SELECT s.roll_number, s.name, m.marks_obtained, m.total_marks " +
                 "FROM marks m " +
                 "JOIN students s ON m.student_roll = s.roll_number " +
                 "WHERE m.subject_code = ? AND m.exam_type = ? " +
                 "ORDER BY s.roll_number";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, subjectCode);
        pstmt.setString(2, examType);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            Object[] row = new Object[]{
                rs.getString("roll_number"),
                rs.getString("name"),
                rs.getDouble("marks_obtained"),
                rs.getDouble("total_marks")
            };
            marksList.add(row);
        }
        
        System.out.println("✅ Found " + marksList.size() + " marks records");
        
    } catch (SQLException e) {
        System.err.println("❌ Error fetching marks: " + e.getMessage());
        e.printStackTrace();
    }
    
    return marksList;
}
}// main class ends here