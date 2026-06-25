package com.campusflow.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

import com.campusflow.model.*;
import com.campusflow.database.*;

/**
 * ManageFeesWindow - Update student fee status
 */
public class ManageFeesWindow extends JFrame {
    
    private Administrator loggedInAdmin;
    private JTable feesTable;
    private DefaultTableModel tableModel;
    
    /**
     * Constructor
     */
    public ManageFeesWindow(Administrator admin) {
        this.loggedInAdmin = admin;
        
        setTitle("Manage Fees - " + admin.getName());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        loadFees();
        
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
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by Status:"));
        
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"All", "Pending", "Paid"});
        filterPanel.add(statusCombo);
        
        JButton filterBtn = new JButton("Filter");
        filterBtn.addActionListener(e -> {
            String status = (String) statusCombo.getSelectedItem();
            if (status.equals("All")) {
                loadFees();
            } else {
                loadFeesByStatus(status);
            }
        });
        filterPanel.add(filterBtn);
        
        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.addActionListener(e -> loadFees());
        filterPanel.add(refreshBtn);
        
        panel.add(filterPanel, BorderLayout.WEST);
        
        return panel;
    }
    
    /**
     * Create table panel
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"Roll No", "Student Name", "Course", "Semester", "Faculty", "Current Status"};
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create bottom panel
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton markPaidBtn = new JButton("✅ Mark as Paid");
        markPaidBtn.setBackground(new Color(46, 204, 113));
        markPaidBtn.setForeground(Color.WHITE);
        markPaidBtn.addActionListener(e -> markStudentFeePaid());
        panel.add(markPaidBtn);
        
        JButton markPendingBtn = new JButton("⏳ Mark as Pending");
        markPendingBtn.setBackground(new Color(230, 126, 34));
        markPendingBtn.setForeground(Color.WHITE);
        markPendingBtn.addActionListener(e -> markStudentFeePending());
        panel.add(markPendingBtn);
        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        panel.add(closeBtn);
        
        return panel;
    }
    
    /**
     * Load all student fees
     */
    private void loadFees() {
        tableModel.setRowCount(0);
        
        StudentDAO dao = new StudentDAO();
        List<Student> students = dao.getAllStudents();
        
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                s.getRollNumber(),
                s.getName(),
                s.getCourse(),
                s.getSemester(),
                s.getFaculty(),
                s.getFeeStatus()
            });
        }
        
        System.out.println("✅ Loaded fees for " + students.size() + " students");
    }
    
    /**
     * Load fees by status
     */
    private void loadFeesByStatus(String status) {
        tableModel.setRowCount(0);
        
        StudentDAO dao = new StudentDAO();
        List<Student> students = dao.getAllStudents();
        
        int count = 0;
        for (Student s : students) {
            if (s.getFeeStatus().equals(status)) {
                tableModel.addRow(new Object[]{
                    s.getRollNumber(),
                    s.getName(),
                    s.getCourse(),
                    s.getSemester(),
                    s.getFaculty(),
                    s.getFeeStatus()
                });
                count++;
            }
        }
        
        System.out.println("✅ Found " + count + " students with status: " + status);
    }
    
    /**
     * Mark selected student fee as paid
     */
    private void markStudentFeePaid() {
        int selectedRow = ((JTable) getFocusOwner()).getSelectedRow();
        
        // Try to get selected row from table
        JPanel panel = (JPanel) getContentPane();
        JTable table = findTable(panel);
        
        if (table == null || table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student!");
            return;
        }
        
        String rollNumber = (String) tableModel.getValueAt(table.getSelectedRow(), 0);
        String name = (String) tableModel.getValueAt(table.getSelectedRow(), 1);
        
        StudentDAO dao = new StudentDAO();
        boolean success = dao.updateStudent(
            rollNumber,
            name,
            (String) tableModel.getValueAt(table.getSelectedRow(), 2),
            (Integer) tableModel.getValueAt(table.getSelectedRow(), 3),
            (String) tableModel.getValueAt(table.getSelectedRow(), 4),
            "",
            "",
            "Paid"
        );
        
        if (success) {
            JOptionPane.showMessageDialog(this, "✅ Fee marked as PAID for " + rollNumber);
            loadFees();
        }
    }
    
    /**
     * Mark selected student fee as pending
     */
    private void markStudentFeePending() {
        JPanel panel = (JPanel) getContentPane();
        JTable table = findTable(panel);
        
        if (table == null || table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student!");
            return;
        }
        
        String rollNumber = (String) tableModel.getValueAt(table.getSelectedRow(), 0);
        String name = (String) tableModel.getValueAt(table.getSelectedRow(), 1);
        
        StudentDAO dao = new StudentDAO();
        boolean success = dao.updateStudent(
            rollNumber,
            name,
            (String) tableModel.getValueAt(table.getSelectedRow(), 2),
            (Integer) tableModel.getValueAt(table.getSelectedRow(), 3),
            (String) tableModel.getValueAt(table.getSelectedRow(), 4),
            "",
            "",
            "Pending"
        );
        
        if (success) {
            JOptionPane.showMessageDialog(this, "✅ Fee marked as PENDING for " + rollNumber);
            loadFees();
        }
    }
    
    /**
     * Helper method to find JTable
     */
    private JTable findTable(JComponent comp) {
        if (comp instanceof JTable) return (JTable) comp;
        
        if (comp instanceof JScrollPane) {
            JScrollPane scroll = (JScrollPane) comp;
            if (scroll.getViewport().getView() instanceof JTable) {
                return (JTable) scroll.getViewport().getView();
            }
        }
        
        if (comp instanceof JPanel) {
            for (java.awt.Component c : ((JPanel) comp).getComponents()) {
                JTable result = findTable((JComponent) c);
                if (result != null) return result;
            }
        }
        
        return null;
    }
}