package com.campusflow.database;

import com.campusflow.model.Subject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * subjectDao - to handle subject database operations
 */

public class SubjectDAO {

    /**
     * get subjects assigned to a teacher
     */

    public List<Subject> getSubjectsByTeacher(String teacherId) {
        List<Subject> subjects  = new ArrayList<>();

        String sql = "select * from subjects where assigned_teacher = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setString(1, teacherId);
                    ResultSet rs = pstmt.executeQuery();

                    while(rs.next()) {
                        Subject subject = new Subject(
                            rs.getString("subject_code"),
                            rs.getString("subject_name"),
                            rs.getString("course"),
                            rs.getInt("semester"),
                            rs.getString("assigned_teacher")
                        );
                        subjects.add(subject);
                    }

                    System.out.println("Found " + subjects.size() + " subjects for teachers " + teacherId);
                } // try closed
                catch (SQLException e) {
                    System.err.println("Error fetching subjects: " + e.getMessage());
                    e.printStackTrace();
                }
                return subjects;

    }// getSubjectsByTeacherz method closed

}// main class ens here