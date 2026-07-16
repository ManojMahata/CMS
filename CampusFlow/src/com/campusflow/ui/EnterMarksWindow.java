package com.campusflow.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import com.campusflow.model.*;
import com.campusflow.database.*;

/**
 * EnterMarksWindow - Window for entering student marks
 */
public class EnterMarksWindow extends JFrame {
    
    private Teacher loggedInTeacher;
    private List<Subject> teacherSubjects;
    private List<Student> students;
    
    // UI Components
    private JComboBox<Subject> subjectCombo;
    private JComboBox<String> examTypeCombo;
    private JTextField totalMarksField;
    private JTable marksTable;
    private DefaultTableModel tableModel;
    
    
    /**
    * Constructor
    */
        public EnterMarksWindow(Teacher teacher) {
            this.loggedInTeacher = teacher;
            
            // Load teacher's subjects
            SubjectDAO dao = new SubjectDAO();
            teacherSubjects = dao.getSubjectsByTeacher(teacher.getTeacherId());
            
            if (teacherSubjects.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                    "No subjects assigned to you!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }
            
            // Setup window
            setTitle("Enter Marks - " + teacher.getName());
            setSize(700, 500);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            
            initComponents();
            
            setVisible(true);
        }

    /**
    * Initialize components - CORRECT ORDER
    */
        private void initComponents() {
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            // STEP 1: Create selection panel (initializes subjectCombo)
            JPanel selectionPanel = createSelectionPanel();
            
            // STEP 2: Create table panel (uses subjectCombo)
            JPanel tablePanel = createTablePanel();
            
            // STEP 3: Create button panel
            JPanel buttonPanel = createButtonPanel();
            
            // Add all to main
            mainPanel.add(selectionPanel, BorderLayout.NORTH);
            mainPanel.add(tablePanel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            add(mainPanel);
        }


    /**
     * Create selection panel
     */
    private JPanel createSelectionPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Exam Details"));
        
        // Semester selection
        panel.add(new JLabel("Semester:"));
        JComboBox<Integer> semesterCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
        semesterCombo.setSelectedItem(5);
        panel.add(semesterCombo);
        
        // Subject selection - INITIALIZE FIRST!
        panel.add(new JLabel("Subject:"));
        subjectCombo = new JComboBox<>();  // Create it HERE
        for (Subject s : teacherSubjects) {
            subjectCombo.addItem(s);
        }
        subjectCombo.addActionListener(e -> loadStudents());
        panel.add(subjectCombo);
        
        // Exam type
        panel.add(new JLabel("Exam Type:"));
        examTypeCombo = new JComboBox<>(new String[]{
            "Internal", "Final", "Assignment"
        });
        panel.add(examTypeCombo);
        
        // Total marks
        panel.add(new JLabel("Total Marks:"));
        totalMarksField = new JTextField("100");
        panel.add(totalMarksField);
        
        // Semester listener
        semesterCombo.addActionListener(e -> {
            int semester = (Integer) semesterCombo.getSelectedItem();
            loadSubjectsBySemester(semester);
        });
        
        return panel;
    }

    /**
     * Load subjects by semester
     */
    private void loadSubjectsBySemester(int semester) {
        subjectCombo.removeAllItems();
        teacherSubjects.clear();
        
        SubjectDAO dao = new SubjectDAO();
        List<Subject> allSubjects = dao.getSubjectsByTeacher(loggedInTeacher.getTeacherId());
        
        for (Subject s : allSubjects) {
            if (s.getSemester() == semester) {
                teacherSubjects.add(s);
                subjectCombo.addItem(s);
            }
        }
        
        if (!teacherSubjects.isEmpty()) {
            loadStudents();  // Only load students AFTER tableModel is created
        }
        
        System.out.println("Loaded " + teacherSubjects.size() + " subjects for semester " + semester);
    }
    
    /**
     * Create table panel
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Student Marks"));
        
        // Create table
        String[] columns = {"Roll Number", "Student Name", "Marks Obtained"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;  // Only marks column editable
            }
        };
        
        marksTable = new JTable(tableModel);
        marksTable.setFont(new Font("Arial", Font.PLAIN, 14));
        marksTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(marksTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Load students for first subject
        loadStudents();
        
        return panel;
    }
    
    /**
     * Create button panel
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton submitBtn = new JButton("Submit Marks");
        submitBtn.setBackground(new Color(46, 204, 113));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 14));
        submitBtn.addActionListener(e -> submitMarks());
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        panel.add(submitBtn);
        panel.add(cancelBtn);
        
        return panel;
    }
    
    /**
     * Load students for selected subject
     */
    private void loadStudents() {
        Subject selectedSubject = (Subject) subjectCombo.getSelectedItem();
        if (selectedSubject == null) return;
        
        StudentDAO dao = new StudentDAO();
        students = dao.getStudentsByCourseAndSemester(
            selectedSubject.getCourse(),
            selectedSubject.getSemester()
        );
        
        // Clear table
        tableModel.setRowCount(0);
        
        // Add students to table
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                s.getRollNumber(),
                s.getName(),
                ""  // Empty marks field
            });
        }
    }
    
    /**
     * Submit marks to database
     */
    private void submitMarks() {
        Subject selectedSubject = (Subject) subjectCombo.getSelectedItem();
        String examType = (String) examTypeCombo.getSelectedItem();
        
        // Validate total marks
        double totalMarks;
        try {
            totalMarks = Double.parseDouble(totalMarksField.getText());
            if (totalMarks <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Please enter valid total marks!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate and collect marks
        List<Double> marksList = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String marksStr = (String) tableModel.getValueAt(i, 2);
            
            if (marksStr == null || marksStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please enter marks for all students!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                double marks = Double.parseDouble(marksStr.trim());
                if (marks < 0 || marks > totalMarks) {
                    JOptionPane.showMessageDialog(this,
                        "Marks must be between 0 and " + totalMarks,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                marksList.add(marks);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Invalid marks format for " + tableModel.getValueAt(i, 1),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Save to database
        MarksDAO dao = new MarksDAO();
        int successCount = 0;
        
        for (int i = 0; i < students.size(); i++) {
            boolean success = dao.addMarks(
                students.get(i).getRollNumber(),
                selectedSubject.getSubjectCode(),
                examType,
                marksList.get(i),
                totalMarks,
                loggedInTeacher.getTeacherId()
            );
            
            if (success) successCount++;
        }
        
        // Show result
        if (successCount == students.size()) {
            JOptionPane.showMessageDialog(this,
                "✅ Marks entered successfully!\n" +
                "Subject: " + selectedSubject.getSubjectName() + "\n" +
                "Exam: " + examType + "\n" +
                "Students: " + successCount,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "⚠️ Partial success: " + successCount + "/" + students.size(),
                "Warning",
                JOptionPane.WARNING_MESSAGE);
        }
    }
}