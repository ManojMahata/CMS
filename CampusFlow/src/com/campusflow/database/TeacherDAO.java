package com.campusflow.database;

import com.campusflow.model.Teacher;
import com.campusflow.utils.PasswordHasher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*  Data access object for Teacher entity
    Handles all database operations for teachers table
*/

public class TeacherDAO {

   // Create - Add new teacher to database
   // @param teacher Teacher object to insert
   // parameter plainPassword Plain text password (will be hashed)
   // @return true if successful, false otherwise

   public boolean addTeacher(Teacher teacher, String plainPassword) {

    String sql = "insert into teachers (teacher_id, name, department, email, " +
    "phone, username, password_hash) values (?, ?, ?, ?, ?, ?, ?)";

    try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, teacher.getTeacherId());

            pstmt.setString(2, teacher.getName());

            pstmt.setString(3, teacher.getDepartment());

            pstmt.setString(4, teacher.getEmail());

            pstmt.setString(5, teacher.getPhone());

            pstmt.setString(6, teacher.getUsername());

            pstmt.setString(7, PasswordHasher.hashPassword(plainPassword));

            int rowsAffected = pstmt.executeUpdate();

            if(rowsAffected > 0) {
                System.out.println("Teacher added: " + teacher.getTeacherId());
                return true;
            }// if ends 

        }// try ends here
        catch (SQLException e) {
            System.err.println("Error adding teacher: " + e.getMessage());
            e.printStackTrace();
        }

        return false;

   }// addTeacher method ends here


   /*
   Read get teacher by ID
   parameter teacherId Teacher's ID
   return Teacher object if found, null otherwise
    */

   public Teacher getTeacherById(String teacherId) {
        
        String sql = "select * from teachers where teacher_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, teacherId);
                ResultSet rs = pstmt.executeQuery();

                if(rs.next()) {
                    return new Teacher(
                        rs.getString("teacher_id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("username"),
                        rs.getString("password_hash")
                    );
                }// if closed

        } catch (SQLException e) {
            
            System.err.println("Error fetching teacher: " + e.getMessage());
            e.printStackTrace();

        }// try cathc ends here

        return null;

    }// getTeacherById method ends here

    /*
    Read - get all teacher
    return list of all teachers
     */
    
    public List<Teacher> getAllTeachers() {

        List<Teacher> teachers = new ArrayList<>();

        String sql = "select * from teachers order by teacher_id";

        try(Connection conn = DatabaseConnection.getConnection(); 
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Teacher teacher = new Teacher (
                    rs.getString("teacher_id"),
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("username"),
                    rs.getString("password_hash")
                );
                teachers.add(teacher);
            }// while ends here

            System.out.println("Retrived " + teachers.size() + "teachers");

        } catch(SQLException e){
            System.err.println("Error fetching teachers: " + e.getMessage());
            e.printStackTrace();
        }

        return teachers;

    }// getAllTeacher method ends here

    /*UPDATE - modify teacher record
    @param teacher Teacher object with update data
    @return ture if successful, false otherwise
     */

    public boolean updateTeacher(Teacher teacher) {

        String sql = "update teachers set name = ?, department = ?, email = ?, " +
        "phone = ? where teacher_id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, teacher.getName());

            pstmt.setString(2, teacher.getDepartment());

            pstmt.setString(3, teacher.getEmail());
            
            pstmt.setString(4, teacher.getPhone());

            pstmt.setString(5, teacher.getTeacherId());

            int roswAffected = pstmt.executeUpdate();

            if ( roswAffected > 0 ) {
                System.out.println("Teacher updated: " + teacher.getTeacherId());
                return true;
            } // if closed

        } catch (SQLException e) {
            System.err.println("Error updating teacher: " + e.getMessage());
            e.printStackTrace();
        }

        return false;

    }// updateTeacher method ends here


    /*
    Delete - remove teacher from database
    param teacherId Teacher ID to delete
    return ture if successful, false otherwise
    */

    public boolean deleteTeacher(String teacherId) {

        String sql = "delete from teachers where teacher_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, teacherId);
            
            int rowAffected  = pstmt.executeUpdate();

            if (rowAffected > 0) {
                System.out.println("Teacher deleted: " + teacherId);
                
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error deleting teacher: " + e.getMessage());
            e.printStackTrace();
        }// try-catch ends here

        return false;

    }// deleteTeacher method closed here

    /* 
    Authentication - validate login credentials
    @param username Username entered by user
    @param plain text password entered by user
    @return Teacher object if login successful, null otherwise
     */

    public Teacher validateLogin(String username, String password) {

        String sql = "select * from teachers where username = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1,  username);
            
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                String storedHash = rs.getString("password_hash");

                // verify password

                if (PasswordHasher.verifyPassword(password, storedHash)) {
                    
                    System.out.println("Login successful for: " + username);

                    return new Teacher(
                        rs.getString("teacher_id"), 
                        rs.getString("name"), 
                        rs.getString("department"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("username"),
                        null
                        );

                } else {
                    System.out.println("Invalid pass");
                }
            } else {
                System.out.println("Username not found: " + username);
            }

        } catch (SQLException e ) {
            System.err.println("Error during login: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // login failed
    }//validateLogin method ends here



}// main class ends here
