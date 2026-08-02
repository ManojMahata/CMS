package com.campusflow;

import com.campusflow.model.Teacher;
import com.campusflow.database.TeacherDAO;

public class AddMyself {

    public static void main(String[] args) {

        TeacherDAO dao = new TeacherDAO();

        Teacher manoj = new Teacher(

            "T003",
            "Manoj Mahata",
            "Computer Science",
            "manoj@email.com",
            "9806421179",
            "manojmahata",
            null
        );

        boolean added = dao.addTeacher(manoj, "manoj123");

        if (added) {
            System.out.println("Acoount created");
            System.out.println("Username: manojmahata");
            System.out.println("Password: manoj123");

            Teacher test = dao.validateLogin("manojmahata", "manoj123");
            if (test != null){
                System.out.println("Login Works..");
            }
        }

    }// main method ends here
    
}// main class ends here