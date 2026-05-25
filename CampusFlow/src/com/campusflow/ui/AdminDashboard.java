package com.campusflow.ui;

import javax.swing.*;
import java.awt.*;
import com.campusflow.model.Administrator;

/**
 * AdminDashboard - Main menu for administrators
 */
public class AdminDashboard extends JFrame {
    
    private Administrator loggedInAdmin;
    
    /**
     * Constructor
     */
    public AdminDashboard(Administrator admin) {
        this.loggedInAdmin = admin;
        
        setTitle("CampusFlow - Admin Dashboard");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        initComponents();
        
        setLocationRelativeTo(null);
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
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(142, 68, 173));  // Purple for admin
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInAdmin.getName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        
        JLabel roleLabel = new JLabel("System Administrator");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        roleLabel.setForeground(Color.WHITE);
        
        JPanel headerTextPanel = new JPanel();
        headerTextPanel.setLayout(new BoxLayout(headerTextPanel, BoxLayout.Y_AXIS));
        headerTextPanel.setBackground(new Color(142, 68, 173));
        headerTextPanel.add(welcomeLabel);
        headerTextPanel.add(Box.createVerticalStrut(5));
        headerTextPanel.add(roleLabel);
        
        headerPanel.add(headerTextPanel, BorderLayout.WEST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(4, 2, 20, 20));
        buttonsPanel.setBackground(new Color(240, 240, 240));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Manage Students Button
        JButton studentsBtn = createDashboardButton("Manage Students", new Color(52, 152, 219));
        studentsBtn.addActionListener(e -> openManageStudents());
        buttonsPanel.add(studentsBtn);
        
        // Manage Teachers Button
        JButton teachersBtn = createDashboardButton("Manage Teachers", new Color(46, 204, 113));
        teachersBtn.addActionListener(e -> openManageTeachers());
        buttonsPanel.add(teachersBtn);
        
        // Manage Subjects Button
        JButton subjectsBtn = createDashboardButton(" Manage Subjects", new Color(155, 89, 182));
        subjectsBtn.addActionListener(e -> openManageSubjects());
        buttonsPanel.add(subjectsBtn);
        
        // Manage Fees Button
        JButton feesBtn = createDashboardButton("Manage Fees", new Color(230, 126, 34));
        feesBtn.addActionListener(e -> openManageFees());
        buttonsPanel.add(feesBtn);
        
        // View All Reports Button
        JButton reportsBtn = createDashboardButton("System Reports", new Color(41, 128, 185));
        reportsBtn.addActionListener(e -> openSystemReports());
        buttonsPanel.add(reportsBtn);
        
        // View Attendance Button
        JButton attendanceBtn = createDashboardButton("View Attendance", new Color(26, 188, 156));
        attendanceBtn.addActionListener(e -> openViewAttendance());
        buttonsPanel.add(attendanceBtn);
        
        // My Profile Button
        JButton profileBtn = createDashboardButton("My Profile", new Color(149, 165, 166));
        profileBtn.addActionListener(e -> showProfile());
        buttonsPanel.add(profileBtn);
        
        // Logout Button
        JButton logoutBtn = createDashboardButton("Logout", new Color(231, 76, 60));
        logoutBtn.addActionListener(e -> logout());
        buttonsPanel.add(logoutBtn);
        
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        
        // Footer
        JLabel footerLabel = new JLabel("CampusFlow v1.0 | Admin Panel", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        footerLabel.setForeground(Color.GRAY);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Create styled button
     */
    private JButton createDashboardButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 80));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    /**
     * Open Manage Students window
     */
    private void openManageStudents() {
        System.out.println("Opening Manage Students...");
        //new ManageStudentsWindow(loggedInAdmin);
        JOptionPane.showMessageDialog(this,
            "Manage Student coming soon!!"
        );
    }
    
    /**
     * Open Manage Teachers window
     */
    private void openManageTeachers() {
        System.out.println("Opening Manage Teachers...");
        JOptionPane.showMessageDialog(this, "Manage Teachers coming soon!");
    }
    
    /**
     * Open Manage Subjects window
     */
    private void openManageSubjects() {
        System.out.println("Opening Manage Subjects...");
        JOptionPane.showMessageDialog(this, "Manage Subjects coming soon!");
    }
    
    /**
     * Open Manage Fees window
     */
    private void openManageFees() {
        System.out.println("Opening Manage Fees...");
        JOptionPane.showMessageDialog(this, "Manage Fees coming soon!");
    }
    
    /**
     * Open System Reports
     */
    private void openSystemReports() {
        System.out.println("Opening System Reports...");
        JOptionPane.showMessageDialog(this, "System Reports coming soon!");
    }
    
    /**
     * Open View Attendance
     */
    private void openViewAttendance() {
        System.out.println("Opening Attendance View...");
        JOptionPane.showMessageDialog(this, "View Attendance coming soon!");
    }
    
    /**
     * Show admin profile
     */
    private void showProfile() {
        String profile = "Administrator Profile\n\n" +
                        "ID: " + loggedInAdmin.getAdminId() + "\n" +
                        "Name: " + loggedInAdmin.getName() + "\n" +
                        "Email: " + loggedInAdmin.getEmail() + "\n" +
                        "Username: " + loggedInAdmin.getUsername();
        
        JOptionPane.showMessageDialog(this, profile, "My Profile", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Logout
     */
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.out.println("Admin logged out: " + loggedInAdmin.getName());
            dispose();
            new LoginWindow();
        }
    }
}