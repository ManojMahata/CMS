package com.campusflow.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.campusflow.model.Administrator;
import com.campusflow.database.AdminDAO;

import com.campusflow.database.TeacherDAO;
import com.campusflow.model.Teacher;

/**
 * LoginWindow - First window users see
 * Allows teachers and admins to log in
 */
public class LoginWindow extends JFrame {

    // GUI Components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JComboBox<String> userTypeCombo;

    /**
     * Constructor - Sets up the login window
     */
    public LoginWindow() {
        // Window settings
        setTitle("CampusFlow - Login");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center on screen
        setResizable(false);

        // Create and add components
        initComponents();

        // Make window visible
        setVisible(true);
    }

    /**
     * Initialize and layout all GUI components
     */
    private void initComponents() {
        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Title label
        JLabel titleLabel = new JLabel("GNC", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(41, 128, 185));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel (center)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 15));
        formPanel.setBackground(new Color(240, 240, 240));

        // User Type selection
        JLabel userTypeLabel = new JLabel("Login as:");
        userTypeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(userTypeLabel);

        String[] userTypes = {"Teacher", "Administrator"};
        userTypeCombo = new JComboBox<>(userTypes);
        userTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(userTypeCombo);

        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(usernameField);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));

        formPanel.add(passwordField);

        // empty celss for spacing
        formPanel.add(new JLabel(""));
        formPanel.add(new JLabel(""));

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // button panel (botton_
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 240, 240));

        loginButton = new JButton("Login");

        loginButton.setFont(new Font("Arial", Font.BOLD, 16));

        loginButton.setPreferredSize(new Dimension(120, 40));

        loginButton.setBackground(new Color(52, 152, 219));

        loginButton.setForeground(Color.WHITE);

        // add click listner to login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        buttonPanel.add(loginButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);


        // add main panel to window
        add(mainPanel);
    }//initComponents method closed


        /*
        // mahdle login click
        private void handleLogin() {
            String username = usernameField.getText().trim();

            String password = new String(passwordField.getPassword());

            String userType = (String) userTypeCombo.getSelectedItem();

            // validation
            if ( username.isEmpty() || password.isEmpty() ){
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter both username and password",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                        );
                return;
            }// if closed

            // current output in termial
            System.out.println("Login attempt:");
            System.out.println("User Type: " + userType);
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);

            // tenporary success message
            JOptionPane.showMessageDialog(
                    this,
                    "Login functionality will be connected to database soon",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
                    );
            // clear password field for security
            passwordField.setText("");


        }// handleLogin method ends here


        */

        private void handleLogin() {

            String username = usernameField.getText().trim();
            String passowrd = new String(passwordField.getPassword());
            String userType = (String) userTypeCombo.getSelectedItem();

            // validation
            if (username.isEmpty() || passowrd.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please enter both username and password",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // authenticationn based on user type

            if (userType.equals("Teacher")) {
                authenticateTeacher(username, passowrd);
            } else {
                authenticateAdmin(username, passowrd);
            }

            // password field for security
            passwordField.setText("");

        }// handleLogin method ends here

        /*
        Authenticate teacher login
        */

        private void authenticateTeacher(String username, String password) {

            System.out.println("Attemptin teacher login for: " + username);

            TeacherDAO dao = new TeacherDAO();
            Teacher teacher = dao.validateLogin(username, password);

            if(teacher != null) {
                // open teacher dashboard
            System.out.println("Teacher logged in: " + teacher.getName());

                // close login window
                dispose();

                // open teacher dashboard
                new TeacherDashboard(teacher);

            }  else {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
                );
            }


        }// authenticationTeacher class ends here

        /*
        Authenticate admin login (placeholder for now)
        */

        // authenticationAdmin method ends here

        /**
 * Authenticate admin login
 */
private void authenticateAdmin(String username, String password) {
    System.out.println("Attempting admin login for: " + username);
    
    AdminDAO dao = new AdminDAO();
    Administrator admin = dao.validateLogin(username, password);
    
    if (admin != null) {
        System.out.println("Admin Logged in: " + admin.getName());
        
        // dispose
        dispose();

        // new admin dashboard
        new AdminDashboard(admin);
    } else {
        JOptionPane.showMessageDialog(this,
            "Invalid admin username or password",
            "Login Failed",
            JOptionPane.ERROR_MESSAGE);
    }
}

        // main method entry point of the application
        public static void main(String[] args){
            // running gui on event dispatch thread
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new LoginWindow();
                }
            });
        }// main method ends here

}// main class ens here
