/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irregularenrollmentsystem;

/**
 *
 * @author sophi
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private JTextField txtStudentId;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblStatus;
    
    public LoginForm() {
        setTitle("Student Enrollment System - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel lblHeader = new JLabel("Irregular Student Enrollment System", JLabel.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(lblHeader, BorderLayout.NORTH);
        
        // Login Panel
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        JLabel lblStudentId = new JLabel("Student ID:");
        txtStudentId = new JTextField(15);
        JLabel lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(15);
        
        loginPanel.add(lblStudentId);
        loginPanel.add(txtStudentId);
        loginPanel.add(lblPassword);
        loginPanel.add(txtPassword);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(btnLogin);
        
        // Status Panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lblStatus = new JLabel(" ");
        lblStatus.setForeground(Color.RED);
        statusPanel.add(lblStatus);
        
        // Add panels to main panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(loginPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = txtStudentId.getText();
                String password = new String(txtPassword.getPassword());
                
                if (studentId.isEmpty() || password.isEmpty()) {
                    lblStatus.setText("Please enter both student ID and password.");
                    return;
                }
                
                StudentDAO studentDAO = new StudentDAO();
                Student student = studentDAO.login(studentId, password);
                
                if (student != null) {
                    // Login successful
                    lblStatus.setText("Login successful!");
                    
                    // Open main dashboard
                    Dashboard dashboard = new Dashboard (student);
                    dashboard.setVisible(true);
                    dispose(); // Close login form
                } else {
                    lblStatus.setText("Invalid student ID or password.");
                }
            }
        });
        
        add(mainPanel);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }
}
