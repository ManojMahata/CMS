package com.campusflow.database;

import com.campusflow.model.Subject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * subjectDao - to handle subject database operations
 */

public class SubjectDAO {

    /**
     * get subjects assigned to a teacher
     */

    public List<Subject> getSubjectsByTeacher(String teacherId) {
        List<Subject> subjects  = new ArrayList<>();

        String sql = "select * from subjects where assigned_teacher = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setString(1, teacherId);
                    ResultSet rs = pstmt.executeQuery();

                    while(rs.next()) {
                        Subject subject = new Subject(
                            rs.getString("subject_code"),
                            rs.getString("subject_name"),
                            rs.getString("course"),
                            rs.getInt("semester"),
                            rs.getString("assigned_teacher")
                        );
                        subjects.add(subject);
                    }

                    System.out.println("Found " + subjects.size() + " subjects for teachers " + teacherId);
                } // try closed
                catch (SQLException e) {
                    System.err.println("Error fetching subjects: " + e.getMessage());
                    e.printStackTrace();
                }
                return subjects;

    }// getSubjectsByTeacherz method closed

    /**
    * Get all subjects
    */
    public List<Subject> getAllSubjects() {
    List<Subject> subjects = new ArrayList<>();
    String sql = "SELECT * FROM subjects ORDER BY subject_code";
    
    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            subjects.add(new Subject(
                rs.getString("subject_code"),
                rs.getString("subject_name"),
                rs.getString("course"),
                rs.getInt("semester"),
                rs.getString("assigned_teacher")  // CHANGED: teacher_id -> assigned_teacher
                ));
            }
        
        } catch (SQLException e) {
        System.err.println("Error fetching subjects: " + e. getMessage());
        }
    
        return subjects;
    }

    /**
 * Add new subject
 */
public boolean addSubject(String code, String name, String course, int semester, String teacherId) {
    String sql = "INSERT INTO subjects VALUES (?, ?, ?, ?, ?)";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, code);
        pstmt.setString(2, name);
        pstmt.setString(3, course);
        pstmt.setInt(4, semester);
        pstmt.setString(5, teacherId);
        
        int rows = pstmt.executeUpdate();
        System.out.println("Subject added: " + code);
        return rows > 0;
        
    } catch (SQLException e) {
        System.err.println("Error adding subject: " + e.getMessage());
        return false;
    }
}

/**
 * Reassign teacher to subject
 */
public boolean reassignTeacher(String subjectCode, String newTeacherId) {
    String sql = "UPDATE subjects SET assigned_teacher = ? WHERE subject_code = ?";  // CHANGED
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, newTeacherId);
        pstmt.setString(2, subjectCode);
        
        int rows = pstmt.executeUpdate();
        System.out.println("Teacher reassigned for: " + subjectCode);
        return rows > 0;
        
    } catch (SQLException e) {
        System.err.println("Error reassigning teacher: " + e.getMessage());
        return false;
    }
}

/**
 * Delete subject
 */
public boolean deleteSubject(String subjectCode) {
    String sql = "DELETE FROM subjects WHERE subject_code = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, subjectCode);
        int rows = pstmt.executeUpdate();
        System.out.println("✅ Subject deleted: " + subjectCode);
        return rows > 0;
        
    } catch (SQLException e) {
        System.err.println("❌ Error deleting subject: " + e.getMessage());
        return false;
    }
}




}// main class ens here