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
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Dashboard extends JFrame {
    private Student student;
    private JTabbedPane tabbedPane;
    
    // Enrollment tab components
    private JTable tblCourses;
    private DefaultTableModel courseTableModel;
    private JButton btnEnroll;
    private JComboBox<String> cmbSemester;
    private JComboBox<String> cmbAcademicYear;
    
    // My Schedule tab components
    private JTable tblSchedule;
    private DefaultTableModel scheduleTableModel;
    private JButton btnDrop;
    
    public Dashboard(Student student) {
        this.student = student;
        
        setTitle("Student Enrollment System - Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Initialize components
        initComponents();
        
        // Load initial data
        loadEnrollmentData();
        loadScheduleData();
    }
    
    private void initComponents() {
        // Main tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create tabs
        JPanel enrollmentPanel = createEnrollmentPanel();
        JPanel schedulePanel = createSchedulePanel();
        JPanel profilePanel = createProfilePanel();
        
        // Add tabs
        tabbedPane.addTab("Enroll in Courses", enrollmentPanel);
        tabbedPane.addTab("My Schedule", schedulePanel);
        tabbedPane.addTab("My Profile", profilePanel);
        
        // Add tabbed pane to frame
        add(tabbedPane, BorderLayout.CENTER);
        
        // Header panel with student info
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Student info
        JPanel studentInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblName = new JLabel("<html><b>Student:</b> " + student.getFirstName() + " " + student.getLastName() + "</html>");
        JLabel lblId = new JLabel("<html><b>ID:</b> " + student.getStudentId() + "</html>");
        JLabel lblProgram = new JLabel("<html><b>Program:</b> " + student.getProgram() + "</html>");
        JLabel lblStatus = new JLabel("<html><b>Status:</b> " + student.getAcademicStatus() + "</html>");
        
        studentInfoPanel.add(lblName);
        studentInfoPanel.add(new JLabel(" | "));
        studentInfoPanel.add(lblId);
        studentInfoPanel.add(new JLabel(" | "));
        studentInfoPanel.add(lblProgram);
        studentInfoPanel.add(new JLabel(" | "));
        studentInfoPanel.add(lblStatus);
        
        // Logout button
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
                dispose();
            }
        });
        
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(btnLogout);
        
        panel.add(studentInfoPanel, BorderLayout.WEST);
        panel.add(logoutPanel, BorderLayout.EAST);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        
        return panel;
    }
    
    private JPanel createEnrollmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel lblSemester = new JLabel("Semester:");
        cmbSemester = new JComboBox<>(new String[]{"1st", "2nd"});
        
        JLabel lblAcademicYear = new JLabel("Academic Year:");
        cmbAcademicYear = new JComboBox<>(new String[]{"2023-2024", "2024-2025"});
        
        JButton btnFilter = new JButton("Filter");
        btnFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadEnrollmentData();
            }
        });
        
        filterPanel.add(lblSemester);
        filterPanel.add(cmbSemester);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(lblAcademicYear);
        filterPanel.add(cmbAcademicYear);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(btnFilter);
        
        // Course table
        courseTableModel = new DefaultTableModel(
                new Object[]{"Section ID", "Course ID", "Course Name", "Section", "Schedule", "Room", "Instructor", "Available Slots"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblCourses = new JTable(courseTableModel);
        JScrollPane scrollPane = new JScrollPane(tblCourses);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), 
                            "Available Courses", TitledBorder.LEFT, TitledBorder.TOP));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnEnroll = new JButton("Enroll in Selected Course");
        buttonPanel.add(btnEnroll);
        
        btnEnroll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enrollInSelectedCourse();
            }
        });
        
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Schedule table
        scheduleTableModel = new DefaultTableModel(
                new Object[]{"Enrollment ID", "Course ID", "Course Name", "Section", "Schedule", "Instructor", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblSchedule = new JTable(scheduleTableModel);
        JScrollPane scrollPane = new JScrollPane(tblSchedule);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), 
                            "My Enrolled Courses", TitledBorder.LEFT, TitledBorder.TOP));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnDrop = new JButton("Drop Selected Course");
        buttonPanel.add(btnDrop);
        
        btnDrop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dropSelectedCourse();
            }
        });
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Student information
        JPanel infoPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), 
                           "Student Information", TitledBorder.LEFT, TitledBorder.TOP));
        
        JLabel lblStudentId = new JLabel("Student ID:");
        JTextField txtStudentId = new JTextField(student.getStudentId());
        txtStudentId.setEditable(false);
        
        JLabel lblName = new JLabel("Name:");
        JTextField txtName = new JTextField(student.getFirstName() + " " + student.getLastName());
        txtName.setEditable(false);
        
        JLabel lblEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField(student.getEmail());
        
        JLabel lblPhone = new JLabel("Phone:");
        JTextField txtPhone = new JTextField(student.getPhone() != null ? student.getPhone() : "");
        
        JLabel lblProgram = new JLabel("Program:");
        JTextField txtProgram = new JTextField(student.getProgram());
        txtProgram.setEditable(false);
        
        JLabel lblYearLevel = new JLabel("Year Level:");
        JTextField txtYearLevel = new JTextField(String.valueOf(student.getYearLevel()));
        txtYearLevel.setEditable(false);
        
        infoPanel.add(lblStudentId);
        infoPanel.add(txtStudentId);
        infoPanel.add(lblName);
        infoPanel.add(txtName);
        infoPanel.add(lblEmail);
        infoPanel.add(txtEmail);
        infoPanel.add(lblPhone);
        infoPanel.add(txtPhone);
        infoPanel.add(lblProgram);
        infoPanel.add(txtProgram);
        infoPanel.add(lblYearLevel);
        infoPanel.add(txtYearLevel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnUpdate = new JButton("Update Profile");
        buttonPanel.add(btnUpdate);
        
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Dashboard.this, 
                                             "Profile updated successfully!", 
                                             "Update Profile", 
                                             JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadEnrollmentData() {
        // Clear existing data
        courseTableModel.setRowCount(0);
        
        // Get selected semester and academic year
        String semester = (String) cmbSemester.getSelectedItem();
        String academicYear = (String) cmbAcademicYear.getSelectedItem();
        
        // Get available sections
        SectionDAO sectionDAO = new SectionDAO();
        List<Section> sections = sectionDAO.getAvailableSections(semester, academicYear);
        
        // Populate table
        for (Section section : sections) {
            int availableSlots = section.getMaxStudents() - section.getCurrentEnrolled();
            courseTableModel.addRow(new Object[]{
                section.getSectionId(),
                section.getCourseId(),
                section.getCourse().getCourseName(),
                section.getSectionNumber(),
                section.getSchedule(),
                section.getRoom(),
                section.getInstructor(),
                availableSlots
            });
        }
    }
    
    private void loadScheduleData() {
        // Clear existing data
        scheduleTableModel.setRowCount(0);
        
        // Get student enrollments
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
        List<Enrollment> enrollments = enrollmentDAO.getStudentEnrollments(student.getStudentId(), null);
        
        // Populate table
        for (Enrollment enrollment : enrollments) {
            scheduleTableModel.addRow(new Object[]{
                enrollment.getEnrollmentId(),
                enrollment.getCourse().getCourseId(),
                enrollment.getCourse().getCourseName(),
                enrollment.getSection().getSectionNumber(),
                enrollment.getSection().getSchedule(),
                enrollment.getSection().getInstructor(),
                enrollment.getStatus()
            });
        }
    }
    
    private void enrollInSelectedCourse() {
        int selectedRow = tblCourses.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                                         "Please select a course to enroll in.", 
                                         "No Selection", 
                                         JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int sectionId = (int) courseTableModel.getValueAt(selectedRow, 0);
        String courseId = (String) courseTableModel.getValueAt(selectedRow, 1);
        String courseName = (String) courseTableModel.getValueAt(selectedRow, 2);
        
        // Confirm enrollment
        int confirm = JOptionPane.showConfirmDialog(this, 
                                                 "Are you sure you want to enroll in " + courseId + " - " + courseName + "?", 
                                                 "Confirm Enrollment", 
                                                 JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
            boolean success = enrollmentDAO.enrollStudent(student.getStudentId(), sectionId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                                             "Successfully enrolled in " + courseId + " - " + courseName, 
                                             "Enrollment Success", 
                                             JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh data
                loadEnrollmentData();
                loadScheduleData();
            } else {
                JOptionPane.showMessageDialog(this, 
                                             "Failed to enroll in course. Please check prerequisites or schedule conflicts.", 
                                             "Enrollment Failed", 
                                             JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void dropSelectedCourse() {
        int selectedRow = tblSchedule.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                                         "Please select a course to drop.", 
                                         "No Selection", 
                                         JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int enrollmentId = (int) scheduleTableModel.getValueAt(selectedRow, 0);
        String courseId = (String) scheduleTableModel.getValueAt(selectedRow, 1);
        String courseName = (String) scheduleTableModel.getValueAt(selectedRow, 2);
        String status = (String) scheduleTableModel.getValueAt(selectedRow, 6);
        
        // Check if course can be dropped
        if (!"Pending".equals(status) && !"Approved".equals(status)) {
            JOptionPane.showMessageDialog(this, 
                                         "Cannot drop course with status: " + status, 
                                         "Drop Failed", 
                                         JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Confirm drop
        int confirm = JOptionPane.showConfirmDialog(this, 
                                                 "Are you sure you want to drop " + courseId + " - " + courseName + "?", 
                                                 "Confirm Drop", 
                                                 JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
            boolean success = enrollmentDAO.dropEnrollment(enrollmentId, student.getStudentId());
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                                             "Successfully dropped " + courseId + " - " + courseName, 
                                             "Drop Success", 
                                             JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh data
                loadEnrollmentData();
                loadScheduleData();
            } else {
                JOptionPane.showMessageDialog(this, 
                                             "Failed to drop course.", 
                                             "Drop Failed", 
                                             JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}