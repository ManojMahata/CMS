package com.campusflow.database;

import java.sql.*;

/**
 * QuestionPaperDAO - Handle question paper uploads
 */
public class QuestionPaperDAO {
    
    /**
     * Save question paper upload
     */
    /**
     * Save question paper upload
     */
    public boolean uploadPaper(String subjectCode, String filePath, String uploadedBy) {
        String sql = "INSERT INTO question_papers (subject_code, file_path, uploaded_by, academic_year, upload_date) " +
                    "VALUES (?, ?, ?, ?, NOW())";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, subjectCode);
            pstmt.setString(2, filePath);
            pstmt.setString(3, uploadedBy);
            pstmt.setString(4, "2025-2026");  // Current academic year
            
            int rows = pstmt.executeUpdate();
            System.out.println("Paper uploaded for: " + subjectCode);
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error uploading paper: " + e.getMessage());
            return false;
        }
    }
}