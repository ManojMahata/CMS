package com.campusflow;

import com.campusflow.model.Teacher;

public class TestTeacher {

    public static void main(String[] args) {
        
        // creting a teacher object (similar to database)
        Teacher t1 = new Teacher(
            "t001",
            "Diwakar Thapa",
            "Humanites and Social Science",
            "diwakarsir@gmail.com",
            "980XXXXXXX",
            "diwakar.sir",
            "ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f"
        );

        // print the details
        System.out.println(t1);
        System.out.println("Teacher Name: " + t1.getName());
        System.out.println("Username: " + t1.getUsername());
        System.out.println("Department: " + t1.getDepartment());

        // Test setter (changeable fields only)
        // t1.setEmail("ram.sharma@newcollege.edu");
        // System.out.println("Updated Email: " + t1.getEmail());

        // here t1.setUserName("diwakar"); // can't change UserName, for security reason.

    }// main method ends here
}// main class ends here