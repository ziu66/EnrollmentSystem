/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package irregularenrollmentsystem;


import javax.swing.*;

public class IrregularEnrollmentSystem {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Initialize database connection
            DatabaseConnection.getConnection();
            
            // Start application with login form
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new LoginForm().setVisible(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                                        "Error starting application: " + e.getMessage(), 
                                        "Application Error", 
                                        JOptionPane.ERROR_MESSAGE);
        }
    }
}