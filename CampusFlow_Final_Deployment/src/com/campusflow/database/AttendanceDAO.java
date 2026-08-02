package com.campusflow.database;

import com.campusflow.model.AttendanceRecord;
import java.sql.*;
import java.time.LocalDate;

/**
 * AttendanceDAO - Handles attendance database operations
 */
public class AttendanceDAO {
    
    /**
     * Mark attendance for one student in one subject
     */
    public boolean markAttendance(AttendanceRecord record) {
        String sql = "INSERT INTO attendance_records " +
                     "(student_roll, student_name, subject_code, attendance_date, " +
                     "status, marked_by, timestamp) " +
                     "VALUES (?, ?, ?, ?, ?, ?, NOW())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, record.getStudentRoll());
            pstmt.setString(2, record.getStudentName());
            pstmt.setString(3, record.getSubjectCode());
            pstmt.setDate(4, Date.valueOf(record.getAttendanceDate()));
            pstmt.setString(5, record.getStatus());
            pstmt.setString(6, record.getMarkedBy());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Attendance marked: " + record.getStudentRoll());
                return true;
            }
            
        } catch (SQLException e) {
            // Check if duplicate entry
            if (e.getMessage().contains("Duplicate entry")) {
                System.err.println("⚠️ Attendance already marked for " + 
                                 record.getStudentRoll() + " on " + record.getAttendanceDate());
                return false;
            }
            System.err.println("Error marking attendance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        
        return false;
    }
    
    /**
     * Check if attendance already marked for subject/date
     */
    public boolean isAttendanceMarked(String subjectCode, LocalDate date) {
        String sql = "SELECT COUNT(*) FROM attendance_records " +
                     "WHERE subject_code = ? AND attendance_date = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, subjectCode);
            pstmt.setDate(2, Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking attendance: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Delete attendance for specific subject/date (for re-marking)
     */
    public boolean deleteAttendance(String subjectCode, LocalDate date) {
        String sql = "DELETE FROM attendance_records " +
                     "WHERE subject_code = ? AND attendance_date = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, subjectCode);
            pstmt.setDate(2, Date.valueOf(date));
            int rowsAffected = pstmt.executeUpdate();
            
            System.out.println("Deleted " + rowsAffected + " attendance records");
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting attendance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get attendance percentage for student in subject
     */
    public double getAttendancePercentage(String studentRoll, String subjectCode) {
        String sql = "SELECT " +
                     "COUNT(*) as total, " +
                     "SUM(CASE WHEN status = 'Present' THEN 1 ELSE 0 END) as present " +
                     "FROM attendance_records " +  //PLURAL: attendance_records
                     "WHERE student_roll = ? AND subject_code = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentRoll);
            pstmt.setString(2, subjectCode);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int total = rs.getInt("total");
                int present = rs.getInt("present");
                
                if (total == 0) return 0.0;
                return (present * 100.0) / total;
            }
            
        } catch (SQLException e) {
            System.err.println("Error calculating percentage: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0.0;
    }
    
    /**
     * Get total classes and present count for a student
     */
    public int[] getAttendanceStats(String studentRoll, String subjectCode) {
        String sql = "SELECT " +
                     "COUNT(*) as total, " +
                     "SUM(CASE WHEN status = 'Present' THEN 1 ELSE 0 END) as present " +
                     "FROM attendance_records " +
                     "WHERE student_roll = ? AND subject_code = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentRoll);
            pstmt.setString(2, subjectCode);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int total = rs.getInt("total");
                int present = rs.getInt("present");
                return new int[]{present, total};  // [present, total]
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting stats: " + e.getMessage());
            e.printStackTrace();
        }
        
        return new int[]{0, 0};
    }
}