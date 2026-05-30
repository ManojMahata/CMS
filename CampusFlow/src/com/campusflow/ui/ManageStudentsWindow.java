package com.campusflow.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

import com.campusflow.model.*;
import com.campusflow.database.*;

/**
 * ManageStudentsWindow - Add, Edit, Delete, Search students
 */
public class ManageStudentsWindow extends JFrame {

    private Administrator loggedInAdmin;
    private JTable studentsTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    /**
     * Constructor
     */
    public ManageStudentsWindow(Administrator admin) {
        this.loggedInAdmin = admin;

        setTitle("Manage Students - " + admin.getName());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadStudents();

        setVisible(true);
    }

    /**
     * Initialize components
     */
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top panel - search and add
        mainPanel.add(createTopPanel(), BorderLayout.NORTH);

        // Center - table
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);

        // Bottom - buttons
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Create top panel with search and add button
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));

        searchField = new JTextField(20);
        searchPanel.add(searchField);

        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchStudents());
        searchPanel.add(searchBtn);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadStudents());
        searchPanel.add(refreshBtn);

        panel.add(searchPanel, BorderLayout.WEST);

        // Add button
        JButton addBtn = new JButton("Add New Student");
        addBtn.setBackground(new Color(46, 204, 113));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addBtn.addActionListener(e -> showAddStudentDialog());

        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addPanel.add(addBtn);
        panel.add(addPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Create table panel
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Column names
        String[] columns = {
                "Roll No", "Name", "Course", "Semester",
                "Faculty", "Email", "Phone", "Fee Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only
            }
        };

        studentsTable = new JTable(tableModel);
        studentsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        studentsTable.setRowHeight(25);
        studentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Column widths
        studentsTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        studentsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        studentsTable.getColumnModel().getColumn(2).setPreferredWidth(60);
        studentsTable.getColumnModel().getColumn(3).setPreferredWidth(60);
        studentsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        studentsTable.getColumnModel().getColumn(5).setPreferredWidth(180);
        studentsTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        studentsTable.getColumnModel().getColumn(7).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(studentsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Create bottom panel with action buttons
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton editBtn = new JButton("Edit");
        editBtn.setBackground(new Color(52, 152, 219));
        editBtn.setForeground(Color.WHITE);
        editBtn.addActionListener(e -> editSelectedStudent());
        panel.add(editBtn);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> deleteSelectedStudent());
        panel.add(deleteBtn);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        panel.add(closeBtn);

        return panel;
    }

    /**
     * Load all students into table
     */
    private void loadStudents() {
        tableModel.setRowCount(0);

        StudentDAO dao = new StudentDAO();
        List<Student> students = dao.getAllStudents();

        for (Student s : students) {
            tableModel.addRow(new Object[] {
                    s.getRollNumber(),
                    s.getName(),
                    s.getCourse(),
                    s.getSemester(),
                    s.getFaculty(),
                    s.getEmail(),
                    s.getPhone(),
                    s.getFeeStatus()
            });
        }

        System.out.println("Loaded " + students.size() + " students");
    }

    /**
     * Search students
     */
    private void searchStudents() {
        String query = searchField.getText().trim();

        if (query.isEmpty()) {
            loadStudents();
            return;
        }

        tableModel.setRowCount(0);

        StudentDAO dao = new StudentDAO();
        List<Student> students = dao.searchStudents(query);

        for (Student s : students) {
            tableModel.addRow(new Object[] {
                    s.getRollNumber(),
                    s.getName(),
                    s.getCourse(),
                    s.getSemester(),
                    s.getFaculty(),
                    s.getEmail(),
                    s.getPhone(),
                    s.getFeeStatus()
            });
        }

        System.out.println("Found " + students.size() + " students matching: " + query);
    }

    /**
     * Show add student dialog
     */
    private void showAddStudentDialog() {
        JDialog dialog = new JDialog(this, "Add New Student", true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Fields
        JTextField rollField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField courseField = new JTextField("BCA");
        JTextField semesterField = new JTextField("5");
        JTextField facultyField = new JTextField("Science");
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JComboBox<String> feeCombo = new JComboBox<>(new String[] { "Pending", "Paid" });

        panel.add(new JLabel("Roll Number:"));
        panel.add(rollField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Course:"));
        panel.add(courseField);
        panel.add(new JLabel("Semester:"));
        panel.add(semesterField);
        panel.add(new JLabel("Faculty:"));
        panel.add(facultyField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Fee Status:"));
        panel.add(feeCombo);

        // Buttons
        JButton saveBtn = new JButton("💾 Save");
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);

        JButton cancelBtn = new JButton("Cancel");

        panel.add(saveBtn);
        panel.add(cancelBtn);

        saveBtn.addActionListener(e -> {
            String roll = rollField.getText().trim().toUpperCase();
            String name = nameField.getText().trim();
            String course = courseField.getText().trim();
            int semester = Integer.parseInt(semesterField.getText().trim());
            String faculty = facultyField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String feeStatus = (String) feeCombo.getSelectedItem();

            if (roll.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Roll number and name are required!");
                return;
            }

            StudentDAO dao = new StudentDAO();
            boolean success = dao.addStudent(roll, name, course, semester, faculty, email, phone, feeStatus);

            if (success) {
                JOptionPane.showMessageDialog(dialog, "Student added successfully!");
                dialog.dispose();
                loadStudents();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add student (duplicate roll number?)");
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.add(panel);
        dialog.setVisible(true);
    }

    /**
     * Edit selected student
     */
    private void editSelectedStudent() {
        int selectedRow = studentsTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to edit!");
            return;
        }

        String rollNumber = (String) tableModel.getValueAt(selectedRow, 0);

        StudentDAO dao = new StudentDAO();
        Student student = dao.getStudentByRollNumber(rollNumber);

        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found!");
            return;
        }

        // Create edit dialog (similar to add but pre-filled)
        JDialog dialog = new JDialog(this, "Edit Student: " + rollNumber, true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField rollField = new JTextField(student.getRollNumber());
        rollField.setEditable(false); // Can't change roll number
        JTextField nameField = new JTextField(student.getName());
        JTextField courseField = new JTextField(student.getCourse());
        JTextField semesterField = new JTextField(String.valueOf(student.getSemester()));
        JTextField facultyField = new JTextField(student.getFaculty());
        JTextField emailField = new JTextField(student.getEmail());
        JTextField phoneField = new JTextField(student.getPhone());
        JComboBox<String> feeCombo = new JComboBox<>(new String[] { "Pending", "Paid" });
        feeCombo.setSelectedItem(student.getFeeStatus());

        panel.add(new JLabel("Roll Number:"));
        panel.add(rollField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Course:"));
        panel.add(courseField);
        panel.add(new JLabel("Semester:"));
        panel.add(semesterField);
        panel.add(new JLabel("Faculty:"));
        panel.add(facultyField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Fee Status:"));
        panel.add(feeCombo);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBackground(new Color(52, 152, 219));
        updateBtn.setForeground(Color.WHITE);

        JButton cancelBtn = new JButton("Cancel");

        panel.add(updateBtn);
        panel.add(cancelBtn);

        updateBtn.addActionListener(e -> {
            boolean success = dao.updateStudent(
                    rollNumber,
                    nameField.getText().trim(),
                    courseField.getText().trim(),
                    Integer.parseInt(semesterField.getText().trim()),
                    facultyField.getText().trim(),
                    emailField.getText().trim(),
                    phoneField.getText().trim(),
                    (String) feeCombo.getSelectedItem());

            if (success) {
                JOptionPane.showMessageDialog(dialog, "Student updated successfully!");
                dialog.dispose();
                loadStudents();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to update student");
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.add(panel);
        dialog.setVisible(true);
    }

    /**
     * Delete selected student
     */
    private void deleteSelectedStudent() {
        int selectedRow = studentsTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete!");
            return;
        }

        String rollNumber = (String) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete student:\n" + rollNumber + " - " + name + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            StudentDAO dao = new StudentDAO();
            boolean success = dao.deleteStudent(rollNumber);

            if (success) {
                JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                loadStudents();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete student");
            }
        }
    }
}