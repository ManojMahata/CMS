package com.campusflow.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

import com.campusflow.model.*;
import com.campusflow.database.*;

/**
 * ViewAttendanceWindow - Admin view all attendance records
 */
public class ViewAttendanceWindow extends JFrame {
    
    private Administrator loggedInAdmin;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    
    /**
     * Constructor
     */
    public ViewAttendanceWindow(Administrator admin) {
        this.loggedInAdmin = admin;
        
        setTitle("View Attendance - " + admin.getName());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        loadAllAttendance();
        
        setVisible(true);
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Top panel
        mainPanel.add(createTopPanel(), BorderLayout.NORTH);
        
        // Center - table
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        // Bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        bottomPanel.add(closeBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Create top panel with filters
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        panel.add(new JLabel("Subject:"));
        SubjectDAO subjectDAO = new SubjectDAO();
        List<Subject> subjects = subjectDAO.getAllSubjects();
        JComboBox<Subject> subjectCombo = new JComboBox<>();
        subjectCombo.addItem(new Subject("ALL", "All Subjects", "", 0, ""));
        for (Subject s : subjects) {
            subjectCombo.addItem(s);
        }
        panel.add(subjectCombo);
        
        JButton filterBtn = new JButton("Filter");
        filterBtn.addActionListener(e -> {
            Subject selected = (Subject) subjectCombo.getSelectedItem();
            if (selected.getSubjectCode().equals("ALL")) {
                loadAllAttendance();
            } else {
                loadAttendanceBySubject(selected.getSubjectCode());
            }
        });
        panel.add(filterBtn);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadAllAttendance());
        panel.add(refreshBtn);
        
        return panel;
    }
    
    /**
     * Create table panel
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"Roll No", "Student Name", "Subject Code", "Date", "Status"};
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        attendanceTable = new JTable(tableModel);
        attendanceTable.setFont(new Font("Arial", Font.PLAIN, 13));
        attendanceTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Load all attendance records
     */
    private void loadAllAttendance() {
        tableModel.setRowCount(0);
        
        // Note: This requires a method in AttendanceDAO to get all records
        // For now, we'll load attendance for each subject
        SubjectDAO subjectDAO = new SubjectDAO();
        StudentDAO studentDAO = new StudentDAO();
        AttendanceDAO attendanceDAO = new AttendanceDAO();
        
        List<Subject> subjects = subjectDAO.getAllSubjects();
        List<Student> students = studentDAO.getAllStudents();
        
        int recordCount = 0;
        for (Subject subject : subjects) {
            for (Student student : students) {
                int[] stats = attendanceDAO.getAttendanceStats(
                    student.getRollNumber(),
                    subject.getSubjectCode()
                );
                
                if (stats[1] > 0) {  // If attendance records exist
                    // Add entry to table
                    tableModel.addRow(new Object[]{
                        student.getRollNumber(),
                        student.getName(),
                        subject.getSubjectCode(),
                        "Multiple Dates",
                        stats[0] + "/" + stats[1] + " Present"
                    });
                    recordCount++;
                }
            }
        }
        
        System.out.println("Loaded " + recordCount + " attendance records");
    }
    
    /**
     * Load attendance by subject
     */
    private void loadAttendanceBySubject(String subjectCode) {
        tableModel.setRowCount(0);
        
        StudentDAO studentDAO = new StudentDAO();
        AttendanceDAO attendanceDAO = new AttendanceDAO();
        
        List<Student> students = studentDAO.getAllStudents();
        
        int recordCount = 0;
        for (Student student : students) {
            int[] stats = attendanceDAO.getAttendanceStats(
                student.getRollNumber(),
                subjectCode
            );
            
            if (stats[1] > 0) {
                tableModel.addRow(new Object[]{
                    student.getRollNumber(),
                    student.getName(),
                    subjectCode,
                    "Multiple Dates",
                    stats[0] + "/" + stats[1] + " Present"
                });
                recordCount++;
            }
        }
        
        System.out.println("Loaded " + recordCount + " attendance records for " + subjectCode);
    }
}