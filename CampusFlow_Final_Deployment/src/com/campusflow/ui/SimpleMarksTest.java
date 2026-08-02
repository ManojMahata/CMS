package com.campusflow.ui;

import javax.swing.*;
import java.awt.*;

public class SimpleMarksTest extends JFrame {
    
    public SimpleMarksTest() {
        setTitle("TEST - Enter Marks");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JLabel label = new JLabel("TEST WINDOW - If you see this, the problem is in EnterMarksWindow code", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        add(label);
        
        setLocationRelativeTo(null);
        setVisible(true);
        
        System.out.println("Test window created and visible");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimpleMarksTest());
    }
}