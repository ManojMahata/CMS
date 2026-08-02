package com.campusflow;

import com.campusflow.model.*;
import com.campusflow.database.*;
import java.time.LocalDate;
import java.util.List;

public class TestAttendance {

    public static void main(String[] args) {
        
        // test 1: get subjects for teacher t003
    SubjectDAO subjectDAO = new SubjectDAO();

    List<Subject> subjects = subjectDAO.getSubjectsByTeacher("T003");

    System.out.println("   Subjects for T003   ");
    
    for (Subject s : subjects) {
        System.out.println(s);
    }

    System.out.println();

    //test 2: mark attendance for one student 
    AttendanceDAO attendanceDAO = new AttendanceDAO();
    AttendanceRecord record = new AttendanceRecord("BCA001", "Manoj Mahata", "BCA501", LocalDate.now(), "Present", "T003", null);

    boolean marked = attendanceDAO.markAttendance(record);
    System.out.println("Attendance marked: " + marked);

    // test 3: check if already marked
    boolean exists = attendanceDAO.isAttendanceMarked("BCA501", LocalDate.now());
    System.out.println("Already marked today: " + exists);

    // test 4: get precentage
    double percentage = attendanceDAO.getAttendancePercentage("BCA001", "BCA501");
    System.out.println("BCA001 attendance: " + percentage + "%");
    

    } // main method closed

    
}// main class ends here