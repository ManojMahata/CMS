package com.campusflow.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

import com.campusflow.model.*;
import com.campusflow.database.*;

/**
 * ViewReportsWindow - Display attendance and marks reports
 */
public class ViewReportsWindow extends JFrame {
    
    private Teacher loggedInTeacher;
    private JTabbedPane tabbedPane;
    
    /**
     * Constructor
     */
    public ViewReportsWindow(Teacher teacher) {
        this.loggedInTeacher = teacher;
        
        setTitle("View Reports - " + teacher.getName());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        
        setVisible(true);
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Add tabs
        tabbedPane.addTab("Attendance Report", createAttendanceReportPanel());
        tabbedPane.addTab("Marks Report", createMarksReportPanel());
        tabbedPane.addTab("Student Search", createStudentSearchPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Close button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        bottomPanel.add(closeBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
 * Create attendance report panel
 */
private JPanel createAttendanceReportPanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    // Top panel - semester and subject selector
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    
    topPanel.add(new JLabel("Select Semester:"));
    JComboBox<Integer> semesterCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
    semesterCombo.setSelectedItem(5);
    topPanel.add(semesterCombo);
    
    topPanel.add(new JLabel("Select Subject:"));
    
    SubjectDAO subjectDAO = new SubjectDAO();
    JComboBox<Subject> subjectCombo = new JComboBox<>();
    topPanel.add(subjectCombo);
    
    JButton loadBtn = new JButton("Load Report");
    topPanel.add(loadBtn);
    
    panel.add(topPanel, BorderLayout.NORTH);
    
    // Center - table
    String[] columns = {"Roll No", "Student Name", "Total Classes", "Present", "Absent", "Percentage", "Status"};
    DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    JTable table = new JTable(tableModel);
    table.setFont(new Font("Arial", Font.PLAIN, 13));
    table.setRowHeight(25);
    
    // Color renderer for status
    table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (!isSelected) {
                String status = value.toString();
                if (status.equals("Good")) {
                    c.setBackground(new Color(220, 255, 220));
                    c.setForeground(new Color(0, 128, 0));
                } else if (status.equals("Low")) {
                    c.setBackground(new Color(255, 240, 200));
                    c.setForeground(new Color(200, 100, 0));
                } else if (status.equals("Critical")) {
                    c.setBackground(new Color(255, 220, 220));
                    c.setForeground(new Color(200, 0, 0));
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
            }
            
            return c;
        }
    });
    
    JScrollPane scrollPane = new JScrollPane(table);
    panel.add(scrollPane, BorderLayout.CENTER);
    
    // Load button action
    loadBtn.addActionListener(e -> {
        int semester = (Integer) semesterCombo.getSelectedItem();
        Subject selectedSubject = (Subject) subjectCombo.getSelectedItem();
        
        if (selectedSubject == null) return;
        
        tableModel.setRowCount(0);
        
        StudentDAO studentDAO = new StudentDAO();
        List<Student> students = studentDAO.getStudentsBySemester(semester);
        
        AttendanceDAO attendanceDAO = new AttendanceDAO();
        
        for (Student student : students) {
            int[] stats = attendanceDAO.getAttendanceStats(
                student.getRollNumber(),
                selectedSubject.getSubjectCode()
            );
            
            int present = stats[0];
            int total = stats[1];
            int absent = total - present;
            double percentage = total > 0 ? (present * 100.0 / total) : 0.0;
            
            String status;
            if (percentage >= 75) {
                status = "Good";
            } else if (percentage >= 60) {
                status = "Low";
            } else {
                status = "Critical";
            }
            
            tableModel.addRow(new Object[]{
                student.getRollNumber(),
                student.getName(),
                total,
                present,
                absent,
                String.format("%.1f%%", percentage),
                status
            });
        }
    });
    
    // When semester changes, load subjects for that semester
    semesterCombo.addActionListener(e -> {
        int semester = (Integer) semesterCombo.getSelectedItem();
        subjectCombo.removeAllItems();
        
        List<Subject> subjects = subjectDAO.getSubjectsByTeacher(loggedInTeacher.getTeacherId());
        for (Subject s : subjects) {
            if (s.getSemester() == semester) {
                subjectCombo.addItem(s);
            }
        }
    });
    
    // Load initial subjects
    semesterCombo.setSelectedItem(5);
    List<Subject> initialSubjects = subjectDAO.getSubjectsByTeacher(loggedInTeacher.getTeacherId());
    for (Subject s : initialSubjects) {
        if (s.getSemester() == 5) {
            subjectCombo.addItem(s);
        }
    }
    
    return panel;
}
    
    /**
 * Create marks report panel
 */
private JPanel createMarksReportPanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    // Top panel - filters
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    
    topPanel.add(new JLabel("Semester:"));
    JComboBox<Integer> semesterCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
    semesterCombo.setSelectedItem(5);
    topPanel.add(semesterCombo);
    
    topPanel.add(new JLabel("Subject:"));
    SubjectDAO subjectDAO = new SubjectDAO();
    JComboBox<Subject> subjectCombo = new JComboBox<>();
    topPanel.add(subjectCombo);
    
    topPanel.add(new JLabel("Exam:"));
    JComboBox<String> examCombo = new JComboBox<>(new String[]{"Internal", "Final", "Assignment"});
    topPanel.add(examCombo);
    
    JButton loadBtn = new JButton("Load Marks");
    topPanel.add(loadBtn);
    
    panel.add(topPanel, BorderLayout.NORTH);
    
    // Center - table
    String[] columns = {"Roll No", "Student Name", "Marks Obtained", "Total Marks", "Percentage", "Grade"};
    DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    JTable table = new JTable(tableModel);
    table.setFont(new Font("Arial", Font.PLAIN, 13));
    table.setRowHeight(25);
    
    JScrollPane scrollPane = new JScrollPane(table);
    panel.add(scrollPane, BorderLayout.CENTER);
    
    // Load button action
    loadBtn.addActionListener(e -> {
        Subject selectedSubject = (Subject) subjectCombo.getSelectedItem();
        String examType = (String) examCombo.getSelectedItem();
        
        if (selectedSubject == null) return;
        
        tableModel.setRowCount(0);
        
        MarksDAO marksDAO = new MarksDAO();
        List<Object[]> marksList = marksDAO.getMarksBySubjectAndExam(
            selectedSubject.getSubjectCode(),
            examType
        );
        
        for (Object[] row : marksList) {
            String rollNo = (String) row[0];
            String name = (String) row[1];
            double obtained = (Double) row[2];
            double total = (Double) row[3];
            
            double percentage = (obtained / total) * 100;
            String grade = getGrade(percentage);
            
            tableModel.addRow(new Object[]{
                rollNo,
                name,
                obtained,
                total,
                String.format("%.1f%%", percentage),
                grade
            });
        }
    });
    
    // When semester changes, load subjects for that semester
    semesterCombo.addActionListener(e -> {
        int semester = (Integer) semesterCombo.getSelectedItem();
        subjectCombo.removeAllItems();
        
        List<Subject> subjects = subjectDAO.getSubjectsByTeacher(loggedInTeacher.getTeacherId());
        for (Subject s : subjects) {
            if (s.getSemester() == semester) {
                subjectCombo.addItem(s);
            }
        }
    });
    
    // Load initial subjects
    semesterCombo.setSelectedItem(5);
    List<Subject> initialSubjects = subjectDAO.getSubjectsByTeacher(loggedInTeacher.getTeacherId());
    for (Subject s : initialSubjects) {
        if (s.getSemester() == 5) {
            subjectCombo.addItem(s);
        }
    }
    
    return panel;
}
    
    /**
     * Create student search panel
     */
    private JPanel createStudentSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Enter Roll Number:"));
        
        JTextField rollField = new JTextField(15);
        searchPanel.add(rollField);
        
        JButton searchBtn = new JButton("Search");
        searchPanel.add(searchBtn);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Result area
        JTextArea resultArea = new JTextArea();
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Search action
        searchBtn.addActionListener(e -> {
            String rollNumber = rollField.getText().trim().toUpperCase();
            
            if (rollNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a roll number",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            StudentDAO studentDAO = new StudentDAO();
            Student student = studentDAO.getStudentByRollNumber(rollNumber);
            
            if (student == null) {
                resultArea.setText("Student not found: " + rollNumber);
                return;
            }
            
            StringBuilder report = new StringBuilder();
            report.append("\n");
            report.append("       STUDENT PERFORMANCE REPORT\n");
            report.append("\n");
            
            report.append("Roll Number: ").append(student.getRollNumber()).append("\n");
            report.append("Name: ").append(student.getName()).append("\n");
            report.append("Course: ").append(student.getCourse()).append(" Semester ").append(student.getSemester()).append("\n");
            report.append("Faculty: ").append(student.getFaculty()).append("\n");
            report.append("Fee Status: ").append(student.getFeeStatus()).append("\n\n");
            
            report.append("\n");
            report.append("ATTENDANCE SUMMARY\n");
            report.append("\n\n");
            
            SubjectDAO subjectDAO = new SubjectDAO();
            List<Subject> subjects = subjectDAO.getSubjectsByTeacher(loggedInTeacher.getTeacherId());
            
            AttendanceDAO attendanceDAO = new AttendanceDAO();
            for (Subject subject : subjects) {
                int[] stats = attendanceDAO.getAttendanceStats(rollNumber, subject.getSubjectCode());
                int present = stats[0];
                int total = stats[1];
                double percentage = total > 0 ? (present * 100.0 / total) : 0.0;
                
                report.append(String.format("%-25s: %d/%d (%.1f%%)\n",
                    subject.getSubjectName(), present, total, percentage));
            }
            
            resultArea.setText(report.toString());
        });
        
        return panel;
    }
    
    /**
     * Calculate grade from percentage
     */
    private String getGrade(double percentage) {
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B+";
        if (percentage >= 60) return "B";
        if (percentage >= 50) return "C";
        if (percentage >= 40) return "D";
        return "F";
    }
}