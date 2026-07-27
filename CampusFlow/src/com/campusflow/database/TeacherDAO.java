package com.campusflow.database;

import com.campusflow.model.Subject;
import com.campusflow.model.Teacher;
import com.campusflow.utils.PasswordHasher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TeacherDAO - Data access object for Teacher entity
 * Handles all database operations for teachers table
 */
public class TeacherDAO {
    
    /**
     * Validate teacher login
     */
    public Teacher validateLogin(String username, String password) {
        String sql = "SELECT * FROM teachers WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                
                if (PasswordHasher.verifyPassword(password, storedHash)) {
                    System.out.println(" Teacher login successful: " + username);
                    
                    return new Teacher(
                        rs.getString("teacher_id"),
                        rs.getString("name"),
                        null,
                        rs.getString("email"),
                        null,
                        rs.getString("username"),
                        storedHash
                    );
                } else {
                    System.out.println(" Invalid password");
                }
            } else {
                System.out.println(" Username not found");
            }
            
        } catch (SQLException e) {
            System.err.println(" Error during login: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get teacher by ID
     */
    public Teacher getTeacherById(String teacherId) {
        String sql = "SELECT * FROM teachers WHERE teacher_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, teacherId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Teacher(
                    rs.getString("teacher_id"),
                    rs.getString("name"),
                    null,
                    rs.getString("email"),
                    null,
                    rs.getString("username"),
                    rs.getString("password_hash")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching teacher: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get all teachers
     */
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers ORDER BY teacher_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                teachers.add(new Teacher(
                    rs.getString("teacher_id"),
                    rs.getString("name"),
                    null,
                    rs.getString("email"),
                    null,
                    rs.getString("username"),
                    rs.getString("password_hash")
                ));
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching teachers: " + e.getMessage());
        }
        
        return teachers;
    }
    
    /**
     * Search teachers by ID or name
     */
    public List<Teacher> searchTeachers(String query) {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers WHERE teacher_id LIKE ? OR name LIKE ? ORDER BY teacher_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + query + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                teachers.add(new Teacher(
                    rs.getString("teacher_id"),
                    rs.getString("name"),
                    null,
                    rs.getString("email"),
                    null,
                    rs.getString("username"),
                    rs.getString("password_hash")
                ));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching teachers: " + e.getMessage());
        }
        
        return teachers;
    }
    
    /**
     * Add new teacher
     */
    public boolean addTeacher(String id, String name, String email, String username, String passwordHash) {
        String sql = "INSERT INTO teachers VALUES (?, ?, ?, ?, ?, NOW())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, username);
            pstmt.setString(5, passwordHash);
            
            int rows = pstmt.executeUpdate();
            System.out.println(" Teacher added: " + id);
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println(" Error adding teacher: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update teacher info
     */
    public boolean updateTeacher(String id, String name, String email) {
        String sql = "UPDATE teachers SET name=?, email=? WHERE teacher_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, id);
            
            int rows = pstmt.executeUpdate();
            System.out.println(" Teacher updated: " + id);
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println(" Error updating teacher: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Reset teacher password
     */
    public boolean resetPassword(String teacherId, String newPasswordHash) {
        String sql = "UPDATE teachers SET password_hash=? WHERE teacher_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newPasswordHash);
            pstmt.setString(2, teacherId);
            
            int rows = pstmt.executeUpdate();
            System.out.println(" Password reset for: " + teacherId);
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println(" Error resetting password: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete teacher
     */
    public boolean deleteTeacher(String teacherId) {
        String sql = "DELETE FROM teachers WHERE teacher_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, teacherId);
            int rows = pstmt.executeUpdate();
            System.out.println(" Teacher deleted: " + teacherId);
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println(" Error deleting teacher: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get subjects assigned to teacher
     */
    public List<Subject> getSubjectsByTeacher(String teacherId) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects WHERE teacher_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, teacherId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                subjects.add(new Subject(
                    rs.getString("subject_code"),
                    rs.getString("subject_name"),
                    rs.getString("course"),
                    rs.getInt("semester"),
                    teacherId
                ));
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching subjects: " + e.getMessage());
        }
        
        return subjects;
    }

    /**
     * Add new teacher
     */
    public boolean addTeacher(String id, String name, String department, String email, String phone, String username, String passwordHash) {
        String sql = "INSERT INTO teachers (teacher_id, name, department, email, phone, username, password_hash, created_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, department);
            pstmt.setString(4, email);
            pstmt.setString(5, phone);
            pstmt.setString(6, username);
            pstmt.setString(7, passwordHash);
            
            int rows = pstmt.executeUpdate();
            System.out.println("Teacher added: " + id);
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding teacher: " + e.getMessage());
            return false;
        }
    }

    public boolean addTeacher(Teacher manoj, String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addTeacher'");
    }

}
