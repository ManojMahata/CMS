package com.campusflow;

import com.campusflow.model.Student;
import com.campusflow.database.StudentDAO;
import java.time.LocalDate;
import java.util.List;


public class TestStudentDAO {
    public static void main(String[] args) {
        
        StudentDAO dao = new StudentDAO();

        // create - add new student
        System.out.println("=== Testing StudentDAO CURD Operations ===\n");

        System.out.println("Test 1: Adding new student... ");

        Student newStudent = new Student(
            "bca002",
            "sita pal",
            "Management",
            "BBS",
            5,
            "A",
            "sita@gmail.com",
            "9806421179",
            "Pending",

            LocalDate.of(2026, 4, 13)

        );

        boolean added = dao.addStudent(newStudent);
        System.out.println("Result: " + (added ? "SUCCESS"  : " FAILED"));
        System.out.println();

        // test 2 : read 
        System.out.println("Test 2: Fetching student bca002...");
        
        Student fetched = dao.getStudentByRollNumber("bca002");

        if(fetched != null) {
            System.out.println("Found: " + fetched);
        } else {
            System.out.println("Student not found");
        }
        System.out.println();

        // test 3: read get all students

        System.out.println("Test 3: Fetching all students...");

        List<Student> allStudent = dao.getAllStudents();

        for(Student s : allStudent) {
            System.out.println(" - " + s.getRollNumber() + ": " + s.getName());
        }
        System.out.println();

        // test 4: read search by courese and semester

        System.out.println("Test 4: Searching BCA semester 5 students...");
        List<Student> bcaStudents = dao.getStudentsByCourseAndSemester("BCA", 5);
        System.out.println("Found " + bcaStudents.size() + " students");
        System.out.println();

        // Test 5: update = modify existing dtudent record

        System.out.println("Test 5: Updating student bca002...");

        if(fetched != null) {
            fetched.setPhone("9800000000");
            fetched.setFeeStatus("Paid");
            
            boolean update = dao.updateStudent(fetched);
            System.out.println("Result: " + (update ? "SUCCESS" : "FAILED"));

            // verify update
            Student updateStudent = dao.getStudentByRollNumber("bca002");
            System.out.println("New phone: " + updateStudent.getPhone());
            System.out.println("New fee status: " + updateStudent);
        }
        System.out.println();

        // test 6: count total students
        System.out.println("Test 6: Counting total students...");
        int count = dao.getStudentCount();
        System.out.println("Total students in database: " + count);
        System.out.println();

        // test 7: delete remove student
        System.out.println("Test 7: Deleting student bca002...");
        boolean deleted = dao.deleteStudent("bca002");
        System.out.println("Result: " + (deleted ? "SUCCESS" : "FAILED"));

        // VERIFY DELETION
        Student shouldBeNull = dao.getStudentByRollNumber("bca002");
        System.out.println("Verification: " + (shouldBeNull == null ? "Deleted successfully" : "Still exists!"));

        System.out.println("\n=== All Test Complete ===");
        

    }// main method ends here
    
}// main class ends here