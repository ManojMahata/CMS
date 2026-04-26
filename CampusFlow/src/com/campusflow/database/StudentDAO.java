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

    public boolean addStudent(Student student) {
        
        String sql = "insert into students (roll_number, name, faculty, course, " + 
        "semester, section, email, phone, fee_status, date_of_admission) " +
        "values (?, ?, ?,  ?, ?, ?, ?, ?, ?, ?)";

        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // setting parameters // palceholders
            pstmt.setString(1, student.getRollNumber());
            
            pstmt.setString(2, student.getName());

            pstmt.setString(3, student.getFaculty());

            pstmt.setString(4, student.getCourse());

            pstmt.setInt(5, student.getSemester());

            pstmt.setString(6, student.getSection());

            pstmt.setString(7, student.getEmail());

            pstmt.setString(8, student.getPhone());

            pstmt.setString(9, student.getFeeStatus());

            pstmt.setDate(10, Date.valueOf(student.getDateOfAdmission()));

            // execute insert
            int rowsAffected = pstmt.executeUpdate();

            if(rowsAffected > 0){
                System.out.println("Student added: " + student.getRollNumber());
                return true;
            }

        } // try closed 
        catch (SQLException e) {
            System.err.println("Error adding studnet: " + e.getMessage());
            e.printStackTrace();
        } // catch closed
        
        return false;

    }// addStudent method ends here

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
                System.out.println("Retrieved " + students.size() + " students");

            } catch (SQLException e) {
                System.err.println("Error fetching all students: " + e.getMessage());

                e.printStackTrace();
            } 

            return students;

    }// List<Student> getAllStudent method closed her

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
            
            System.out.println("✅ Found " + students.size() + " students in " + 
                             course + " Semester " + semester);
            
        } catch (SQLException e) {
            System.err.println("❌ Error searching students: " + e.getMessage());
            e.printStackTrace();
        }
        
        return students;
    }

    /*
    update - modifying existing student record
    @param student Student object updated data 
    @return true if successful, false otherwise*/

    public boolean updateStudent(Student student) {
        String sql = "update students set name = ?, faculty = ?, course = ?, " + "semester = ?, section = ?, email = ?, phone = ?, " + "fee_status = ? where roll_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, student.getName());
                
                pstmt.setString(2, student.getFaculty());

                pstmt.setString(3, student.getCourse());

                pstmt.setInt(4, student.getSemester());

                pstmt.setString(5, student.getSection());

                pstmt.setString(6, student.getEmail());

                pstmt.setString(7, student.getPhone());

                pstmt.setString(8, student.getFeeStatus());

                pstmt.setString(9, student.getRollNumber());

                int rowsAffected = pstmt.executeUpdate();

                if ( rowsAffected > 0 ) {
                    System.out.println("Student update :" + student.getRollNumber());
                    return true;
                } else {
                    System.out.println("No student found wiht roll number.");
                } // if else ends here

        } // tyr ends here
        catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            e.printStackTrace();
        }

        return false;

    }// updateStudent method ended

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
public boolean deleteStudent(String rollNumber) {
    String sql = "DELETE FROM students WHERE roll_number = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, rollNumber);
        int rowsAffected = pstmt.executeUpdate();
        
        if (rowsAffected > 0) {
            System.out.println("✅ Student deleted: " + rollNumber);
            return true;
        } else {
            System.out.println("⚠️ No student found with roll number: " + rollNumber);
        }
        
    } catch (SQLException e) {
        System.err.println("❌ Error deleting student: " + e.getMessage());
        e.printStackTrace();
    }
    
    return false;
}

}// main class closed