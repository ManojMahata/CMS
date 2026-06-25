package com.campusflow.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

import com.campusflow.model.*;
import com.campusflow.database.*;

/**
 * ManageSubjectsWindow - Add, Edit, Delete, Reassign subjects to teachers
 */
public class ManageSubjectsWindow extends JFrame {
    
    private Administrator loggedInAdmin;
    private JTable subjectsTable;
    private DefaultTableModel tableModel;
    
    /**
     * Constructor
     */
    public ManageSubjectsWindow(Administrator admin) {
        this.loggedInAdmin = admin;
        
        setTitle("Manage Subjects - " + admin.getName());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        loadSubjects();
        
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
        
        // Bottom - buttons
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Create top panel
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadSubjects());
        panel.add(refreshBtn);
        
        JButton addBtn = new JButton("Add Subject");
        addBtn.setBackground(new Color(46, 204, 113));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addBtn.addActionListener(e -> showAddSubjectDialog());
        panel.add(addBtn);
        
        return panel;
    }
    
    /**
     * Create table panel
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"Subject Code", "Subject Name", "Course", "Semester", "Assigned Teacher"};
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        subjectsTable = new JTable(tableModel);
        subjectsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        subjectsTable.setRowHeight(25);
        subjectsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(subjectsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create bottom panel
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton reassignBtn = new JButton("Reassign Teacher");
        reassignBtn.setBackground(new Color(52, 152, 219));
        reassignBtn.setForeground(Color.WHITE);
        reassignBtn.addActionListener(e -> reassignTeacher());
        panel.add(reassignBtn);
        
        JButton deleteBtn = new JButton("Delete Subject");
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> deleteSubject());
        panel.add(deleteBtn);
        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        panel.add(closeBtn);
        
        return panel;
    }
    
    /**
     * Load all subjects
     */
    private void loadSubjects() {
        tableModel.setRowCount(0);
        
        SubjectDAO dao = new SubjectDAO();
        List<Subject> subjects = dao.getAllSubjects();
        
        for (Subject s : subjects) {
            tableModel.addRow(new Object[]{
                s.getSubjectCode(),
                s.getSubjectName(),
                s.getCourse(),
                s.getSemester(),
                s.getTeacherId()
            });
        }
        
        System.out.println("Loaded " + subjects.size() + " subjects");
    }
    
    /**
     * Show add subject dialog
     */
    private void showAddSubjectDialog() {
        JDialog dialog = new JDialog(this, "Add New Subject", true);
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTextField codeField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField courseField = new JTextField("BCA");
        JTextField semesterField = new JTextField("5");
        
        TeacherDAO teacherDAO = new TeacherDAO();
        List<Teacher> teachers = teacherDAO.getAllTeachers();
        JComboBox<Teacher> teacherCombo = new JComboBox<>();
        for (Teacher t : teachers) {
            teacherCombo.addItem(t);
        }
        
        panel.add(new JLabel("Subject Code:"));
        panel.add(codeField);
        panel.add(new JLabel("Subject Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Course:"));
        panel.add(courseField);
        panel.add(new JLabel("Semester:"));
        panel.add(semesterField);
        panel.add(new JLabel("Assign to Teacher:"));
        panel.add(teacherCombo);
        
        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);
        
        JButton cancelBtn = new JButton("Cancel");
        
        panel.add(saveBtn);
        panel.add(cancelBtn);
        
        saveBtn.addActionListener(e -> {
            String code = codeField.getText().trim().toUpperCase();
            String name = nameField.getText().trim();
            String course = courseField.getText().trim();
            int semester = Integer.parseInt(semesterField.getText().trim());
            Teacher selectedTeacher = (Teacher) teacherCombo.getSelectedItem();
            
            if (code.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Code and name are required!");
                return;
            }
            
            SubjectDAO dao = new SubjectDAO();
            boolean success = dao.addSubject(code, name, course, semester, selectedTeacher.getTeacherId());
            
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Subject added successfully!");
                dialog.dispose();
                loadSubjects();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add subject");
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Reassign teacher to subject
     */
    private void reassignTeacher() {
        int selectedRow = subjectsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a subject!");
            return;
        }
        
        String subjectCode = (String) tableModel.getValueAt(selectedRow, 0);
        
        TeacherDAO teacherDAO = new TeacherDAO();
        List<Teacher> teachers = teacherDAO.getAllTeachers();
        
        JComboBox<Teacher> teacherCombo = new JComboBox<>();
        for (Teacher t : teachers) {
            teacherCombo.addItem(t);
        }
        
        int option = JOptionPane.showConfirmDialog(this, teacherCombo, 
            "Select new teacher for " + subjectCode,
            JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            Teacher selectedTeacher = (Teacher) teacherCombo.getSelectedItem();
            
            SubjectDAO dao = new SubjectDAO();
            boolean success = dao.reassignTeacher(subjectCode, selectedTeacher.getTeacherId());
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Teacher reassigned!");
                loadSubjects();
            }
        }
    }
    
    /**
     * Delete subject
     */
    private void deleteSubject() {
        int selectedRow = subjectsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a subject!");
            return;
        }
        
        String subjectCode = (String) tableModel.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Delete subject: " + subjectCode + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            SubjectDAO dao = new SubjectDAO();
            boolean success = dao.deleteSubject(subjectCode);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Subject deleted!");
                loadSubjects();
            }
        }
    }
}