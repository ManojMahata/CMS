package com.campusflow.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.campusflow.model.*;
import com.campusflow.database.*;

/**
 * MarkAttendanceWindow - Window for marking student attendance
 * Supports multi-subject marking
 */
public class MarkAttendanceWindow extends JFrame {
    
    private Teacher loggedInTeacher;
    private List<Subject> teacherSubjects;
    private List<Student> students;
    
    // UI Components
    private JPanel subjectsPanel;
    private JPanel studentsPanel;
    private JLabel summaryLabel;
    private List<JCheckBox> subjectCheckboxes;
    private List<JCheckBox> studentCheckboxes;
    private LocalDate selectedDate;
    
    /**
     * Constructor
     */
    public MarkAttendanceWindow(Teacher teacher) {
        this.loggedInTeacher = teacher;
        this.selectedDate = LocalDate.now();
        this.subjectCheckboxes = new ArrayList<>();
        this.studentCheckboxes = new ArrayList<>();
        
        // Load data
        loadSubjects();
        loadStudents();
        
        // Setup window
        setTitle("Mark Attendance - " + teacher.getName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        
        setVisible(true);
    }
    
    /**
     * Load subjects assigned to teacher
     */
    private void loadSubjects() {
        SubjectDAO dao = new SubjectDAO();
        teacherSubjects = dao.getSubjectsByTeacher(loggedInTeacher.getTeacherId());
        
        if (teacherSubjects.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No subjects assigned to you!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }
    
    /**
     * Load students from teacher's classes
     */
    private void loadStudents() {
        StudentDAO dao = new StudentDAO();
        
        // Get students from first subject's course/semester
        if (!teacherSubjects.isEmpty()) {
            Subject firstSubject = teacherSubjects.get(0);
            students = dao.getStudentsByCourseAndSemester(
                firstSubject.getCourse(),
                firstSubject.getSemester()
            );
        }
        
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No students found in your classes!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    /**
 * Load subjects and students by semester
 */
private void loadSubjectsAndStudentsBySemester(int semester) {
    // Load subjects for the selected semester that this teacher teaches
    SubjectDAO subjectDAO = new SubjectDAO();
    teacherSubjects = new ArrayList<>();
    
    for (Subject s : subjectDAO.getSubjectsByTeacher(loggedInTeacher.getTeacherId())) {
        if (s.getSemester() == semester) {
            teacherSubjects.add(s);
        }
    }
    
    // Update subjects checkboxes
    subjectsPanel.removeAll();
    subjectCheckboxes.clear();
    
    for (Subject subject : teacherSubjects) {
        JCheckBox cb = new JCheckBox(subject.toString());
        cb.setFont(new Font("Arial", Font.PLAIN, 14));
        subjectCheckboxes.add(cb);
        subjectsPanel.add(cb);
        subjectsPanel.add(Box.createVerticalStrut(5));
    }
    subjectsPanel.revalidate();
    subjectsPanel.repaint();
    
    // Load students for this semester
    StudentDAO studentDAO = new StudentDAO();
    students = studentDAO.getStudentsBySemester(semester);
    
    // Update students checkboxes
    studentsPanel.removeAll();
    studentCheckboxes.clear();
    
    for (Student student : students) {
        JCheckBox cb = new JCheckBox(
            student.getRollNumber() + " - " + student.getName()
        );
        cb.setFont(new Font("Arial", Font.PLAIN, 14));
        cb.setSelected(true);
        cb.addActionListener(e -> updateSummary());
        
        studentCheckboxes.add(cb);
        studentsPanel.add(cb);
        studentsPanel.add(Box.createVerticalStrut(5));
    }
    studentsPanel.revalidate();
    studentsPanel.repaint();
    
    updateSummary();
    
    System.out.println("Loaded " + teacherSubjects.size() + " subjects and " + 
                       students.size() + " students for semester " + semester);
}
    
    /**
     * Initialize UI components
     */
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Top - Semester selector
        mainPanel.add(createSemesterPanel(), BorderLayout.PAGE_START);
        
        // Center - split into subjects and students
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createSubjectsPanel());
        splitPane.setRightComponent(createStudentsPanel());
        splitPane.setDividerLocation(250);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        // Footer - summary and buttons
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    /**
     * Create semester selector panel
     */
    private JPanel createSemesterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Select Semester"));
        
        JComboBox<Integer> semesterCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
        semesterCombo.setSelectedItem(1);  // Default to semester 
        JButton loadBtn = new JButton("Load Subjects & Students");
        loadBtn.addActionListener(e -> {
            int semester = (Integer) semesterCombo.getSelectedItem();
            loadSubjectsAndStudentsBySemester(semester);
        });
        
        panel.add(new JLabel("Semester:"));
        panel.add(semesterCombo);
        panel.add(loadBtn);
        
        return panel;
    }


    
    /**
     * Create header panel
     */
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 128, 185));
        header.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Mark Attendance");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel dateLabel = new JLabel("Date: " + selectedDate.toString());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setForeground(Color.WHITE);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(new Color(41, 128, 185));
        textPanel.add(titleLabel);
        textPanel.add(dateLabel);
        
        header.add(textPanel, BorderLayout.WEST);
        
        return header;
    }
    
    /**
     * Create subjects selection panel
     */
    private JPanel createSubjectsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Select Subjects"));
        
        subjectsPanel = new JPanel();
        subjectsPanel.setLayout(new BoxLayout(subjectsPanel, BoxLayout.Y_AXIS));
        
        // Add checkbox for each subject
        for (Subject subject : teacherSubjects) {
            JCheckBox cb = new JCheckBox(subject.toString());
            cb.setFont(new Font("Arial", Font.PLAIN, 14));
            subjectCheckboxes.add(cb);
            subjectsPanel.add(cb);
            subjectsPanel.add(Box.createVerticalStrut(5));
        }
        
        JScrollPane scrollPane = new JScrollPane(subjectsPanel);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create students list panel
     */
    private JPanel createStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Mark Attendance"));
        
        studentsPanel = new JPanel();
        studentsPanel.setLayout(new BoxLayout(studentsPanel, BoxLayout.Y_AXIS));
        
        // Add checkbox for each student
        for (Student student : students) {
            JCheckBox cb = new JCheckBox(
                student.getRollNumber() + " - " + student.getName()
            );
            cb.setFont(new Font("Arial", Font.PLAIN, 14));
            cb.setSelected(true);  // Default: all present
            
            // Add listener to update summary
            cb.addActionListener(e -> updateSummary());
            
            studentCheckboxes.add(cb);
            studentsPanel.add(cb);
            studentsPanel.add(Box.createVerticalStrut(5));
        }
        
        JScrollPane scrollPane = new JScrollPane(studentsPanel);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create footer with summary and buttons
     */
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Summary label
        summaryLabel = new JLabel();
        summaryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        updateSummary();
        footer.add(summaryLabel, BorderLayout.WEST);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton selectAllBtn = new JButton("Select All");
        selectAllBtn.addActionListener(e -> selectAll(true));
        
        JButton deselectAllBtn = new JButton("Deselect All");
        deselectAllBtn.addActionListener(e -> selectAll(false));
        
        JButton submitBtn = new JButton("Submit Attendance");
        submitBtn.setBackground(new Color(46, 204, 113));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 14));
        submitBtn.addActionListener(e -> submitAttendance());
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        buttonsPanel.add(selectAllBtn);
        buttonsPanel.add(deselectAllBtn);
        buttonsPanel.add(submitBtn);
        buttonsPanel.add(cancelBtn);
        
        footer.add(buttonsPanel, BorderLayout.EAST);
        
        return footer;
    }
    
    /**
     * Update attendance summary
     */
    private void updateSummary() {
        int present = 0;
        int total = studentCheckboxes.size();
        
        for (JCheckBox cb : studentCheckboxes) {
            if (cb.isSelected()) present++;
        }
        
        int absent = total - present;
        double percentage = (present * 100.0) / total;
        
        String text = String.format("Present: %d/%d (%.1f%%) | Absent: %d",
                                   present, total, percentage, absent);
        
        summaryLabel.setText(text);
        
        // Color code based on percentage
        if (percentage >= 75) {
            summaryLabel.setForeground(new Color(46, 204, 113));
        } else if (percentage >= 60) {
            summaryLabel.setForeground(new Color(230, 126, 34));
        } else {
            summaryLabel.setForeground(new Color(231, 76, 60));
        }
    }
    
    /**
     * Select/deselect all students
     */
    private void selectAll(boolean selected) {
        for (JCheckBox cb : studentCheckboxes) {
            cb.setSelected(selected);
        }
        updateSummary();
    }
    
    /**
     * Submit attendance to database
     */
    private void submitAttendance() {
        // Get selected subjects
        List<Subject> selectedSubjects = new ArrayList<>();
        for (int i = 0; i < subjectCheckboxes.size(); i++) {
            if (subjectCheckboxes.get(i).isSelected()) {
                selectedSubjects.add(teacherSubjects.get(i));
            }
        }
        
        if (selectedSubjects.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select at least one subject!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if already marked
        AttendanceDAO attendanceDAO = new AttendanceDAO();
        for (Subject subject : selectedSubjects) {
            if (attendanceDAO.isAttendanceMarked(subject.getSubjectCode(), selectedDate)) {
                int choice = JOptionPane.showConfirmDialog(this,
                    "Attendance already marked for " + subject.getSubjectName() + " today.\n" +
                    "Do you want to delete and re-mark?",
                    "Duplicate Attendance",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                
                if (choice == JOptionPane.YES_OPTION) {
                    attendanceDAO.deleteAttendance(subject.getSubjectCode(), selectedDate);
                } else {
                    return;
                }
            }
        }
        
        // Mark attendance
        int successCount = 0;
        int totalRecords = selectedSubjects.size() * students.size();
        
        for (Subject subject : selectedSubjects) {
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                boolean isPresent = studentCheckboxes.get(i).isSelected();
                
                AttendanceRecord record = new AttendanceRecord(
                    student.getRollNumber(),
                    student.getName(),
                    subject.getSubjectCode(),
                    selectedDate,
                    isPresent ? "Present" : "Absent",
                    loggedInTeacher.getTeacherId(),
                    null
                );
                
                if (attendanceDAO.markAttendance(record)) {
                    successCount++;
                }
            }
        }
        
        // Show result
        if (successCount == totalRecords) {
            JOptionPane.showMessageDialog(this,
                "✅ Attendance marked successfully!\n" +
                "Subjects: " + selectedSubjects.size() + "\n" +
                "Students: " + students.size() + "\n" +
                "Total records: " + successCount,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "⚠️ Partial success: " + successCount + "/" + totalRecords + " records saved",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
        }
    }
}