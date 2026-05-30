package com.campusflow.database;

import com.campusflow.model.Student;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
StudentDAO : data access boject for student entity/student table to handle all database ooperations for studnets talbe
 */

public class StudentDAO {

    /*to add new student to database
    @param staudent stuent object to insert
    @return true if successful, false otherwise */
    public boolean addStudent(String roll, String name, String course, int semester,
                         String faculty, String email, String phone, String feeStatus) {
        String sql = "INSERT INTO students (roll_number, name, course, semester, faculty, email, phone, fee_status, section, date_of_admission) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'A', CURDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, roll);
            pstmt.setString(2, name);
            pstmt.setString(3, course);
            pstmt.setInt(4, semester);
            pstmt.setString(5, faculty);
            pstmt.setString(6, email);
            pstmt.setString(7, phone);
            pstmt.setString(8, feeStatus);
            
            int rows = pstmt.executeUpdate();
            System.out.println("Student added: " + roll);
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
            return false;
        }
    }
    // addStudent method ends here

    /**
     * Overloaded addStudent method accepting a Student object
     */
    public boolean addStudent(Student student) {
        return addStudent(
            student.getRollNumber(),
            student.getName(),
            student.getCourse(),
            student.getSemester(),
            student.getFaculty(),
            student.getEmail(),
            student.getPhone(),
            student.getFeeStatus()
        );
    }

    /*
    read get student by roll number;
    @param rollNumber Student's roll number
    @return Student object if found, null otherwise
    */

    public Student getStudentByRollNumber(String rollNumber) {

        String sql = "select * from students where roll_Number = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, rollNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // convert database row to Studnet object
                return new Student(
                    rs.getString("roll_number"),
                    rs.getString("name"),
                    rs.getString("faculty"),
                    rs.getString("course"),
                    rs.getInt("semester"),
                    rs.getString("section"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("fee_status"),
                    rs.getDate("date_of_admission").toLocalDate()

                );
            }


        } catch (Exception e) {
            // jjk
            System.err.println("Error fetching student: " + e.getMessage());
            e.printStackTrace();
        }// try catch ends here

        return null;

    }// getStudentByRollNumber method is closed

    /*
    read - get all studnets 
    return list of all students in database*/

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "select * from students order by roll_number";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                students.add(new Student(
                    rs.getString("roll_number"),
                    rs.getString("name"),
                    rs.getString("faculty"),
                    rs.getString("course"),
                    rs.getInt("semester"),
                    rs.getString("section"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("fee_status"),
                    rs.getDate("date_of_admission") != null ? rs.getDate("date_of_admission").toLocalDate() : null
                ));
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching students: " + e.getMessage());
        }
        
        return students;
    }


    // List<Student> getAllStudent method closed her

    /**
     * READ - Get students by course and semester
     * @param course Course name (e.g., "BCA")
     * @param semester Semester number
     * @return List of matching students
     */
    public List<Student> getStudentsByCourseAndSemester(String course, int semester) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE course = ? AND semester = ? ORDER BY roll_number";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, course);
            pstmt.setInt(2, semester);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Student student = new Student(
                    rs.getString("roll_number"),
                    rs.getString("name"),
                    rs.getString("faculty"),
                    rs.getString("course"),
                    rs.getInt("semester"),
                    rs.getString("section"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("fee_status"),
                    rs.getDate("date_of_admission").toLocalDate()
                );
                students.add(student);
            }
            
            System.out.println("Found " + students.size() + " students in " + 
                             course + " Semester " + semester);
            
        } catch (SQLException e) {
            System.err.println("Error searching students: " + e.getMessage());
            e.printStackTrace();
        }
        
        return students;
    }

    /*
    update - modifying existing student record
    @param student Student object updated data 
    @return true if successful, false otherwise*/
    /**
 * Update student
 */
public boolean updateStudent(String roll, String name, String course, int semester,
                            String faculty, String email, String phone, String feeStatus) {
    String sql = "UPDATE students SET name=?, course=?, semester=?, faculty=?, " +
                 "email=?, phone=?, fee_status=? WHERE roll_number=?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, name);
        pstmt.setString(2, course);
        pstmt.setInt(3, semester);
        pstmt.setString(4, faculty);
        pstmt.setString(5, email);
        pstmt.setString(6, phone);
        pstmt.setString(7, feeStatus);
        pstmt.setString(8, roll);
        
        int rows = pstmt.executeUpdate();
        System.out.println("Student updated: " + roll);
        return rows > 0;
        
    } catch (SQLException e) {
        System.err.println("Error updating student: " + e.getMessage());
        return false;
    }
}
    // updateStudent method ended

    /*
    method to get total number of students */
    public int getStudentCount() {

        String sql = "select count(*) from students";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql) ) {

            if(rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error counting studdent: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;

    }// getStudentCount method ends here
    
    /**
     * DELETE - Remove student from database
     * @param rollNumber Roll number of student to delete
     * @return true if successful, false otherwise
     */
    /**
 * Delete student
 */
public boolean deleteStudent(String rollNumber) {
    String sql = "DELETE FROM students WHERE roll_number = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, rollNumber);
        int rows = pstmt.executeUpdate();
        System.out.println("Student deleted: " + rollNumber);
        return rows > 0;
        
    } catch (SQLException e) {
        System.err.println("Error deleting student: " + e.getMessage());
        return false;
    }
}
    // deleteStudent method ends here

    
    /**
     * Search students by roll/name
     */
    public List<Student> searchStudents(String query) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE " +
                     "roll_number LIKE ? OR name LIKE ? " +
                     "ORDER BY roll_number";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + query + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                students.add(new Student(
                    rs.getString("roll_number"),
                    rs.getString("name"),
                    rs.getString("faculty"),
                    rs.getString("course"),
                    rs.getInt("semester"),
                    rs.getString("section"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("fee_status"),
                    rs.getDate("date_of_admission") != null ? rs.getDate("date_of_admission").toLocalDate() : null
                ));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching students: " + e.getMessage());
        }
        
        return students;    
    } // searchStudents method ends here

    /**
     * Overloaded updateStudent method accepting a Student object
     */
    public boolean updateStudent(Student student) {
        return updateStudent(
            student.getRollNumber(),
            student.getName(),
            student.getCourse(),
            student.getSemester(),
            student.getFaculty(),
            student.getEmail(),
            student.getPhone(),
            student.getFeeStatus()
        );
    }



}// main class closed