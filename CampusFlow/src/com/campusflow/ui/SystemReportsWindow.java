package com.campusflow.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import com.campusflow.model.*;
import com.campusflow.database.*;

/**
 * SystemReportsWindow - Display system-wide statistics
 */
public class SystemReportsWindow extends JFrame {
    
    private Administrator loggedInAdmin;
    
    /**
     * Constructor
     */
    public SystemReportsWindow(Administrator admin) {
        this.loggedInAdmin = admin;
        
        setTitle("System Reports - " + admin.getName());
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        
        setVisible(true);
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 240, 240));
        
        // Header
        JLabel headerLabel = new JLabel("System Statistics");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        
        // Statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 30, 30));
        statsPanel.setBackground(new Color(240, 240, 240));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Get statistics
        StudentDAO studentDAO = new StudentDAO();
        TeacherDAO teacherDAO = new TeacherDAO();
        SubjectDAO subjectDAO = new SubjectDAO();
        AttendanceDAO attendanceDAO = new AttendanceDAO();
        MarksDAO marksDAO = new MarksDAO();
        
        List<Student> allStudents = studentDAO.getAllStudents();
        List<Teacher> allTeachers = teacherDAO.getAllTeachers();
        List<Subject> allSubjects = subjectDAO.getAllSubjects();
        
        int totalStudents = allStudents.size();
        int totalTeachers = allTeachers.size();
        int totalSubjects = allSubjects.size();
        
        // Count fee status
        int feePaid = 0;
        int feePending = 0;
        for (Student s : allStudents) {
            if (s.getFeeStatus().equals("Paid")) {
                feePaid++;
            } else {
                feePending++;
            }
        }
        
        // Add stat cards
        statsPanel.add(createStatCard("Total Students", String.valueOf(totalStudents), new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Total Teachers", String.valueOf(totalTeachers), new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Total Subjects", String.valueOf(totalSubjects), new Color(155, 89, 182)));
        statsPanel.add(createStatCard("Fee Status: Paid", String.valueOf(feePaid), new Color(39, 174, 96)));
        statsPanel.add(createStatCard("Fee Status: Pending", String.valueOf(feePending), new Color(230, 126, 34)));
        
        double paidPercentage = (feePaid * 100.0) / totalStudents;
        statsPanel.add(createStatCard("Fee Collection Rate", String.format("%.1f%%", paidPercentage), new Color(41, 128, 185)));
        
        mainPanel.add(statsPanel, BorderLayout.CENTER);
        
        // Close button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        bottomPanel.add(closeBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Create a stat card with title, value, and color
     */
    private JPanel createStatCard(String title, String value, Color bgColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 32));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setAlignmentX(CENTER_ALIGNMENT);
        
        card.add(Box.createVerticalStrut(10));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(15));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(10));
        
        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(bgColor);
            }
        });
        
        return card;
    }
}