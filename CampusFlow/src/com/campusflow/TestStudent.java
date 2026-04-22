package com.campusflow;

import com.campusflow.model.Student;
import java.time.LocalDate;

public class TestStudent {
    public static void main(String[] args) {

        // student object to call student construcotr of Student.java file.

        Student s1 = new Student(
            "bca001",
            "Manoj Mahata",
            "Humanities and Social Science",
            "BCA",
            4,
            "A",
            "manojmahata402@gmail.com",
            "9806421179",
            "Pending",
            LocalDate.of(2026, 2, 15)
        );

        // print student details
        System.out.println(s1);

        System.out.println("Student Name: " + s1.getName());

        System.out.println("Roll Number: " + s1.getRollNumber());

        System.out.println("Fee Status: " + s1.getFeeStatus());

        // lets test setter
        s1.setFeeStatus("Paid");
        System.out.println("Updated Fee Status: " + s1.getFeeStatus());

    }// main method ends here
}// main class ends here

