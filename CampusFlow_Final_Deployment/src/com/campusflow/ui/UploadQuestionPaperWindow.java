package com.campusflow.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.Flow;

import com.campusflow.model.*;
import com.campusflow.database.*;

/**
 * program to upload qeustion papers for subjects
 */

public class UploadQuestionPaperWindow extends JFrame {

    private Teacher loggedInTeacher;
    private JTable papersTable;
    private DefaultTableModel tableModel;
    private String selectedFilePath = "";

    public UploadQuestionPaperWindow(Teacher teacher) {
        this.loggedInTeacher = teacher;

        setTitle("Upload Question Paper - " + teacher.getName());
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponent();
        loadPapers();

        setVisible(true);
    }

    /**
     * initialize components
     */
    private void initComponent() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // top panel
        mainPanel.add(createTopPanel(), BorderLayout.NORTH);

        //center panel
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);

        // bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        bottomPanel.add(closeBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * create top panel
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton uploadBtn = new JButton("Upload Paper");
        uploadBtn.setBackground(new Color(46, 204, 113));
        uploadBtn.setForeground(Color.WHITE);
        uploadBtn.setFont(new Font("Arial", Font.BOLD, 14));
        uploadBtn.addActionListener(e -> uploadPaper());
        panel.add(uploadBtn);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadPapers());
        panel.add(refreshBtn);

        return panel;
    }
    
    // table panel
    private JPanel createTablePanel() {
        JPanel panelTable = new JPanel(new BorderLayout());

        String[] cloumns = {"Subject Code", "Subject Name", "File Path", "Upload Date"};
        
        tableModel = new DefaultTableModel(cloumns, 0){
        @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        papersTable = new JTable(tableModel);
        papersTable.setFont(new Font("Arial", Font.PLAIN, 13));
        papersTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(papersTable);
        panelTable.add(scrollPane, BorderLayout.CENTER);

        return panelTable;
    }

    // load uploaded papers
    private void loadPapers() {
        tableModel.setRowCount(0);

        SubjectDAO subjectDAO = new SubjectDAO();
        List<Subject> subjects = subjectDAO.getSubjectsByTeacher(loggedInTeacher.getTeacherId());

        for (Subject s : subjects) {
            tableModel.addRow(new Object[] {
                s.getSubjectCode(),
                s.getSubjectName(),
                "Not uploaded yet",
                "-"
            });
        }

        System.out.println("Loaded " + subjects.size() + " subjects");
    }


/**
 * Upload paper dialog
 */
private void uploadPaper() {
    int selectedRow = papersTable.getSelectedRow();
    
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a subject!");
        return;
    }
    
    // File chooser dialog
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select Question Paper PDF");
    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));
    
    int result = fileChooser.showOpenDialog(this);
    
    if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        String filePath = selectedFile.getAbsolutePath();
        
        String subjectCode = (String) tableModel.getValueAt(selectedRow, 0);
        
        // Get semester from subject
        SubjectDAO subjectDAO = new SubjectDAO();
        List<Subject> allSubjects = subjectDAO.getSubjectsByTeacher(loggedInTeacher.getTeacherId());
        int semester = 5;  // Default
        
        for (Subject s : allSubjects) {
            if (s.getSubjectCode().equals(subjectCode)) {
                semester = s.getSemester();
                break;
            }
        }
        
        // Save to database
        QuestionPaperDAO dao = new QuestionPaperDAO();
        boolean success = dao.uploadPaper(subjectCode, filePath, loggedInTeacher.getTeacherId(), semester);
        
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Question paper uploaded successfully!\n" +
                "Subject: " + subjectCode + "\n" +
                "File: " + selectedFile.getName(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Update table
            tableModel.setValueAt(filePath, selectedRow, 2);
            tableModel.setValueAt(new java.util.Date().toString(), selectedRow, 3);
            
            System.out.println("Paper uploaded for " + subjectCode + ": " + filePath);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to upload paper to database");
        }
    }
}

}// main class ends here