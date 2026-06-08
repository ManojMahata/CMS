package com.campusflow.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.Flow;

import com.campusflow.model.*;
import com.campusflow.database.*;
import com.campusflow.utils.PasswordHasher;

/**
 * manageteacher window (adding, editing, delete, reset password for teachers)
 */

public class ManageTeachersWindow extends JFrame{

    // attributes and encapsulation
    private Administrator loggedInAdmin;
    private JTable teachersTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    // constructor 
    public ManageTeachersWindow(Administrator admin) {
        this.loggedInAdmin = admin;

        setTitle("Manage Teachers - " + admin.getName());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        intiComponents();
        loadTeachers();

        setVisible(true);

    }
    
    /**
     * initialize component */

    private void intiComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // top panel search and add button
        mainPanel.add(createTopPanel(), BorderLayout.NORTH);

        // center table
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);

        // button - buttons
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        add(mainPanel);

    }// initComponents endse here

    // top panel with search and add button
    private JPanel createTopPanel(){

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));

        searchField = new JTextField(20);
        searchPanel.add(searchField);

        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchTeachers());
        searchPanel.add(searchBtn);

        JButton refreshBtn = new JButton("Refresh:");
        refreshBtn.addActionListener(e -> loadTeachers());
        searchPanel.add(refreshBtn);

        panel.add(searchPanel, BorderLayout.WEST);

        // add button
        JButton addBtn =  new JButton("Add New Teacher");
        addBtn.setBackground(new Color(46, 204, 113));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addBtn.addActionListener(e -> showAddTeacherDialog());

        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addPanel.add(addBtn);
        panel.add(addPanel, BorderLayout.EAST);

        return panel;

    }// top panel ends here
    

    /**createTablePanel */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // column names
        String[] columns = {"Teacher ID", "Name", "Email", "Username", "Subjects"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        teachersTable = new JTable(tableModel);
        teachersTable.setFont(new Font("Arial", Font.PLAIN, 13));
        teachersTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(teachersTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;

    }// createTablePanel endsh here

    /**bottom panel with action buttons */
    private JPanel createBottomPanel() {
        JPanel panel =  new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton editBtn = new JButton("Edit");
        editBtn.setBackground(new Color(52, 152, 219));
        editBtn.setForeground(Color.WHITE);
        editBtn.addActionListener(e -> editSelectedTeacher());
        panel.add(editBtn);

        JButton resetPwdBtn = new JButton("Reset Password");
        resetPwdBtn.setBackground(new Color(230, 126, 34));
        resetPwdBtn.setForeground(Color.WHITE);
        resetPwdBtn.addActionListener(e -> resetSelectedTeacherPassword());
        panel.add(resetPwdBtn);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> deleteSelectedTeacher());
        panel.add(deleteBtn);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        panel.add(closeBtn);

        return panel;

    }// createBottomPanel endds here

    /**load teacher into table */

    private void loadTeachers() {
        tableModel.setRowCount(0);

        TeacherDAO dao = new TeacherDAO();
        List<Teacher> teachers = dao.getAllTeachers();

        SubjectDAO subjectDAO = new SubjectDAO();

        for (Teacher t : teachers) {
            List<Subject> subjects = subjectDAO.getSubjectsByTeacher(t.getTeacherId());

            StringBuilder subjectStr = new StringBuilder();
            for (Subject s : subjects) {
                if (subjectStr.length() > 0) subjectStr.append(",");
                subjectStr.append(s.getSubjectCode());
            }// nested for loop endshere

            tableModel.addRow(new Object[]{
                t.getTeacherId(),
                t.getName(),
                t.getEmail(),
                t.getUsername(),
                subjectStr.toString()
            }); 

        }// for loop ends here

        System.out.println("Loaded " + teachers.size() + " teacher");

    }


    /**search teacher */
    private void searchTeachers() {
        String query = searchField.getText().trim();

        if (query.isEmpty()) {
            loadTeachers();
            return;
        }

        tableModel.setRowCount(0);

        TeacherDAO dao = new TeacherDAO();
        List<Teacher> teachers = dao.searchTeachers(query);

        SubjectDAO subjectDAO = new SubjectDAO();

        for (Teacher t : teachers) {
            List<Subject> subjects = subjectDAO.getSubjectsByTeacher(t.getTeacherId());
            StringBuilder subjectStr = new StringBuilder();

            for (Subject s : subjects) {
                if (subjectStr.length() > 0) subjectStr.append(", ");
                subjectStr.append(s.getSubjectCode());
            }

            tableModel.addRow(new Object[] {
                t.getTeacherId(),
                t.getName(),
                t.getEmail(),
                t.getUsername(),
                subjectStr.toString()
            });
        }
        System.out.println("Found " + teachers.size() + " teachers");
    }



    /**add teacher dialog */
    private void showAddTeacherDialog() {

        JDialog dialog = new JDialog(this, "Add new Teacher", true);
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel =  new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("Teacher ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password: "));
        panel.add(passwordField);

        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);

        JButton cancelBtn = new JButton("Cancel");

        panel.add(saveBtn);
        panel.add(cancelBtn);

        saveBtn.addActionListener(e ->{
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (id.isEmpty() || name.isEmpty() || username.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(dialog, "All fields are required!"); 
            }

            String passwrodHash = PasswordHasher.hashPassword(password);

            TeacherDAO dao = new TeacherDAO();
            boolean success = dao.addTeacher(id, name, email, username, passwrodHash);

            if (success) {
                JOptionPane.showMessageDialog(dialog, "Teacher added successfully");
                dialog.dispose();
                loadTeachers();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add teacher (duplicate ID?");
            }

        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.add(panel);
        dialog.setVisible(true);
    }

    /**edit teacher */

    private void editSelectedTeacher() {
        int selectRow = teachersTable.getSelectedRow();

        if (selectRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a teacher to edit");
            return;
        }

        String teacherId = (String) tableModel.getValueAt(selectRow, 0);

        TeacherDAO dao = new TeacherDAO();
        Teacher teacher = dao.getTeacherById(teacherId);

        if (teacher == null) {
            JOptionPane.showMessageDialog(this, "Teacher not found!");
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Teacher: " + teacherId, true);
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField idField = new JTextField( teacher.getTeacherId());
        idField.setEditable(false);
        JTextField nameField = new JTextField(teacher.getName());
        JTextField emailField = new JTextField(teacher.getEmail());
        JTextField usernameField = new JTextField(teacher.getUsername());
        usernameField.setEditable(false);

        panel.add(new JLabel("Teacher ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email: "));
        panel.add(emailField);
        panel.add(new JLabel("Username: "));
        panel.add(usernameField);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBackground(new Color(52, 152, 219));
        updateBtn.setForeground(Color.WHITE);

        JButton cancelBtn = new JButton("Cancel");

        panel.add(updateBtn);
        panel.add(cancelBtn);

        updateBtn.addActionListener(e -> {
            boolean success = dao.updateTeacher     (teacherId, 
                nameField.getText().trim(),
                emailField.getText().trim());

            if (success) {
                JOptionPane.showMessageDialog(dialog, "Teacher update successfully!");
                dialog.dispose();
                loadTeachers();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to update teacher");
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.add(panel);
        dialog.setVisible(true);
    }


    /**reset teacher's password */
    private void resetSelectedTeacherPassword() {
        int selectedRow = teachersTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a teacher!");
            return;
        }

        String teacherId = (String) tableModel.getValueAt(selectedRow, 0);
        String newPassword = JOptionPane.showInputDialog(this, "Enter new password:", "");

        if (newPassword == null || newPassword.trim().isEmpty()) {
            return;
        }

        new PasswordHasher();
        String passwordHash = PasswordHasher.hashPassword(newPassword);

        TeacherDAO dao = new TeacherDAO();
        boolean success = dao.resetPassword(teacherId, passwordHash);

        if (success) {
            JOptionPane.showMessageDialog(this, "Password reset successfully!\nNew passsword: " + newPassword);
        } else {
            JOptionPane.showMessageDialog(this,"Failed to reset password");
        }

    }

    /** delete selected teacher */
    private void deleteSelectedTeacher() {
        int selectedRow = teachersTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a teacher to delete!");
            return;
        }

        String teacherId = (String) tableModel.getValueAt(selectedRow, 0);

        String name = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure want to delete:\n" + teacherId + " - " + name + "?",
            "Confirm delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            TeacherDAO dao = new TeacherDAO();
            boolean success = dao.deleteTeacher(teacherId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Teacher deleted successfully!");
                loadTeachers();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete teacher");
            }
        }
    }

}// main class ends here