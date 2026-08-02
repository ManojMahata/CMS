package com.campusflow;

import com.campusflow.model.Teacher;
import com.campusflow.database.TeacherDAO;

public class TestTeacherLogin {

    public static void main(String[] args) {
        TeacherDAO dao = new TeacherDAO();

        System.out.println("=== Testing Teacher Login ===\n");

        // test 1: correct credentials
        System.out.println("Test 1:  Correct Login");

        Teacher t1 = dao.validateLogin("sita.devi", "teacher123");

        if( t1 != null) {
            System.out.println("Success! Logged in as: " + t1.getName());
        } else {
            System.out.println("Failed");
        }
        System.out.println();

        // test 2: wrong password
        System.out.println("Lets enter wrong password");

        Teacher t2 = dao.validateLogin("sita.devi", "wrongpassword");

        if ( t2 == null) {
            System.out.println("Correctly rejected");
        }
        System.out.println();

        // test 3 : wrong username

        System.out.println("Test 3: Wrong username");

        Teacher t3 = dao.validateLogin("sita.devii", "teacher234");

        if (t3 == null) {
            System.out.println("Correctly rejected");
        }


    }// main method ends here
    
}//  main class ends here