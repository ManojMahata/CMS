package com.campusflow.ui;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import com.campusflow.model.Teacher;

/*
Teacher Deshboard - Main menu for teacher
Shows after successful login
*/

public class TeacherDashboard extends JFrame {

    private Teacher loggedInTeacher;

    /*
    Constructor
    @param teacher The logged-in teacher object
     */

    public TeacherDashboard(Teacher teacher) {

        this.loggedInTeacher = teacher;

        // whindow settings
        setTitle("Campusflow - Teacher Dashboard");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        // create UI
        initComponents();

        setLocationRelativeTo(null);


        setVisible(true);

    }// TeacherDashboard method class ends here

    /*
    Initialize dashboard components
    */

    private void initComponents() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInTeacher.getName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        welcomeLabel.setForeground(Color.WHITE);

        JLabel infoLabel = new JLabel(loggedInTeacher.getDepartment());
        infoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        infoLabel.setForeground(Color.WHITE);

        JPanel headerTextPanel = new JPanel();
        headerTextPanel.setLayout(new BoxLayout(headerTextPanel, BoxLayout.Y_AXIS));
        headerTextPanel.setBackground(new Color(41, 128, 185));
        headerTextPanel.add(welcomeLabel);
        headerTextPanel.add(Box.createVerticalStrut(5));
        headerTextPanel.add(infoLabel);

        headerPanel.add(headerTextPanel, BorderLayout.WEST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(3,2,20,20));
        buttonsPanel.setBackground(new Color(240, 240, 240));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Mark Attendance Button
        JButton attendanceBtn = createDashboardButton("Mark Attendance", new Color(46, 204, 113));
        attendanceBtn.addActionListener(e -> openAttendanceWindow());
        buttonsPanel.add(attendanceBtn);

        // Enter Marks button
        JButton marksBtn = createDashboardButton ("Enter Marks", new Color(52, 152, 219));
        marksBtn.addActionListener(e -> openMarksWindow());
        buttonsPanel.add(marksBtn);

        // view reports button
        JButton reportBtn = createDashboardButton("View Reports", new Color(155, 89, 182));
        reportBtn.addActionListener(e -> openReportsWindow());
        buttonsPanel.add(reportBtn);

        // upload question paper button
        JButton uploadBtn = createDashboardButton("Uplaod Question Paper", new Color(230, 126, 34));
        uploadBtn.addActionListener(e -> openUploadWindow());
        buttonsPanel.add(uploadBtn);

        // My profile button
        JButton profileBtn = createDashboardButton("My Profile", new Color(149, 165, 166));
        profileBtn.addActionListener(e -> showProfile());
        buttonsPanel.add(profileBtn);

        // Logotu Button
        JButton logoutBtn = createDashboardButton("Logout", new Color(231, 76, 60));
        logoutBtn.addActionListener(e -> logout());
        buttonsPanel.add(logoutBtn);

        mainPanel.add(buttonsPanel, BorderLayout.CENTER);

        // FOOTER
        JLabel footerLabel = new JLabel("Campusflow v1.0 | GNC College Management System", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        footerLabel.setForeground(Color.GRAY);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        add(mainPanel);

    }// initComponent method ends here


    /*
     * Helper method to create styled buttons
     */
    private JButton createDashboardButton(String text, Color color) {

        JButton button = new JButton(text);
        button.setFont(new Font("Arial",Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 80));


        // hover effect
        button.addMouseListener (new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;

        }// createDashboardButton method ends here




        // Oopen a attendance marking window
        private void openAttendanceWindow() {

           System.out.println("Opening Attendence window...");

        //    JOptionPane.showMessageDialog(this, "Attendence window comoing soon");
        new MarkAttendanceWindow(loggedInTeacher);

        }// openattendancewindow method ends here

        /*
        open marks entry window
        */

        private void openMarksWindow() {
            System.out.println("Opening Marks Window...");
            
            new EnterMarksWindow(loggedInTeacher);
        }

        // reprot window
        private void openReportsWindow(){
            System.out.println("Opening Report winsow...");
            new ViewReportsWindow(loggedInTeacher);
        }// report window method ends here


        // oopen question paper upload window
        private void openUploadWindow() {
                System.out.println("Opening Upload Window...");
                JOptionPane.showMessageDialog(this, "Upload coming soon");
        }// uploadwinod method ends here


        //show teacher profile

        private void showProfile() {
            String profile = "Teacher Profile\n\n" +
                                "ID: " + loggedInTeacher.getTeacherId() + "\n" +
                                "Name: "  + loggedInTeacher.getName() + "\n" +
                                "Department: " + loggedInTeacher.getDepartment() + "\n" +
                                "Email: " + loggedInTeacher.getEmail() + "\n" +
                                "Phone: " + loggedInTeacher.getPhone();

            JOptionPane.showMessageDialog(this, profile, "My profile", JOptionPane.INFORMATION_MESSAGE);
        }// showProfile method ends here
        //


        // lOGOUT and return to login window
        private void logout() {

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION
                    );

            if ( confirm == JOptionPane.YES_OPTION) {
                System.out.println("Teacher logged out: " + loggedInTeacher.getName());
                dispose();// close sahsboard
                new LoginWindow(); // again opens login window
            }

        }// logout method ends


}// main class ended
