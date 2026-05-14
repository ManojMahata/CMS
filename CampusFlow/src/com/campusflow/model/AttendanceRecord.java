package com.campusflow.model;

import java.time.LocalDate;

/**
 * AttendanceRecord - Represent a single attendance entry
 * Maps to attendance_record table
 */

public class AttendanceRecord {
    
    // attributes and incapsulation
    private int attendanceId;
    private String studentRoll;
    private String studentName;
    private String subjectCode;
    private LocalDate attendanceDate;
    private String status;
    private String markedBy;
    private String notes;

    // constructor - full

    public AttendanceRecord(int attendanceId, String studentRoll, String studentName, String subjectCode, LocalDate attendanceDate, String status, String markedBy, String notes) {

        this.attendanceId = attendanceId;
        this.studentRoll = studentRoll;
        this.studentName = studentName;
        this.subjectCode = subjectCode;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.markedBy = markedBy;
        this.notes =  notes;

    }// AttendanceRecord constructor closed


    // constructor for new records (no ID yet)
    public AttendanceRecord(String studentRoll, String studentName, String subjectcode, LocalDate attendanceDate, String status, String markedBy, String notes){

        this.studentRoll = studentRoll;
        this.studentName = studentName;
        this.subjectCode = subjectcode;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.markedBy = markedBy;
        this.notes = notes;

    }// constructor for new records

    
    // getters
    
    public int getAttendanceId() {
        return attendanceId;
    }

    public String getStudentRoll() {
        return studentRoll;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public String getStatus() {
        return status;
    }

    public String getMarkedBy() {
        return markedBy;
    }

    public String getNotes() {
        return notes;
    }

    
    // Setters
    public void setStatus(String status) {
        this.status = status;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "AttendanceRecord{" + 
                "studentRoll='" + studentRoll + '\'' +
                ", date=" + attendanceDate + 
                ", status='" + status + '\'' + 
                '}';
    }


} // main class ends here
