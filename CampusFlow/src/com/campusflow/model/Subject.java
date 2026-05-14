package com.campusflow.model;

/**
 * subject - represent a coureses and subject
 */

public class Subject {

    // encapsulation and variables
    private String subjectCode;
    private String subjectName;
    private String course;
    private int semester;
    private String assignedTeacher;

    // constructor
    public Subject (String subjectCode, String subjectName, String course, int semester, String assignedTeacher) {

        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.course = course;
        this.semester = semester;
        this.assignedTeacher = assignedTeacher;
    }

    // getters
    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectName() {return subjectName;}
    public String getCourse() {return course;}
    public int getSemester() {return semester;}
    public String getAssignedTeacher() {return assignedTeacher;}

    @Override
    public String toString() {
        return subjectCode + " - " + subjectName;
    }

}// main class closed 