/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irregularenrollmentsystem;

/**
 *
 * @author sophi
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {
    private Connection connection;
    private SectionDAO sectionDAO;
    private CourseDAO courseDAO;
    
    public EnrollmentDAO() {
        this.connection = DatabaseConnection.getConnection();
        this.sectionDAO = new SectionDAO();
        this.courseDAO = new CourseDAO();
    }
    
    public boolean enrollStudent(String studentId, int sectionId) {
        // Check if already enrolled
        if (isStudentEnrolled(studentId, sectionId)) {
            return false;
        }
        
        // Get section details
        Section section = sectionDAO.getSectionById(sectionId);
        if (section == null || section.getCurrentEnrolled() >= section.getMaxStudents()) {
            return false;
        }
        
        // Check prerequisites
        if (!courseDAO.hasPrerequisites(section.getCourseId(), studentId)) {
            return false;
        }
        
        // Check for schedule conflicts
        if (hasScheduleConflict(studentId, section)) {
            return false;
        }
        
        // Enroll student
        String query = "INSERT INTO enrollments (student_id, section_id, status) VALUES (?, ?, 'Pending')";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentId);
            stmt.setInt(2, sectionId);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Update section enrollment count
                sectionDAO.updateSectionEnrollment(sectionId, true);
                
                // Log enrollment history
                logEnrollmentHistory(studentId, "Enrolled in course", 
                                    "Section ID: " + sectionId + ", Course: " + section.getCourseId());
                
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean dropEnrollment(int enrollmentId, String studentId) {
        // Get section ID before dropping
        Enrollment enrollment = getEnrollmentById(enrollmentId);
        if (enrollment == null || !enrollment.getStudentId().equals(studentId)) {
            return false;
        }
        
        String query = "DELETE FROM enrollments WHERE enrollment_id = ? AND student_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, enrollmentId);
            stmt.setString(2, studentId);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Update section enrollment count
                sectionDAO.updateSectionEnrollment(enrollment.getSectionId(), false);
                
                // Log enrollment history
                logEnrollmentHistory(studentId, "Dropped course", 
                                    "Enrollment ID: " + enrollmentId + ", Section ID: " + enrollment.getSectionId());
                
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public List<Enrollment> getStudentEnrollments(String studentId, String status) {
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "SELECT e.*, s.course_id, s.section_number, s.schedule, s.instructor " +
                      "FROM enrollments e " +
                      "JOIN sections s ON e.section_id = s.section_id " +
                      "WHERE e.student_id = ?" + (status != null ? " AND e.status = ?" : "");
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentId);
            if (status != null) {
                stmt.setString(2, status);
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setEnrollmentId(rs.getInt("enrollment_id"));
                enrollment.setStudentId(rs.getString("student_id"));
                enrollment.setSectionId(rs.getInt("section_id"));
                enrollment.setEnrollmentDate(rs.getTimestamp("enrollment_date"));
                enrollment.setStatus(rs.getString("status"));
                enrollment.setRemarks(rs.getString("remarks"));
                
                // Get section details
                Section section = new Section();
                section.setSectionId(rs.getInt("section_id"));
                section.setCourseId(rs.getString("course_id"));
                section.setSectionNumber(rs.getString("section_number"));
                section.setSchedule(rs.getString("schedule"));
                section.setInstructor(rs.getString("instructor"));
                
                // Get course details
                Course course = courseDAO.getCourseById(rs.getString("course_id"));
                
                enrollment.setSection(section);
                enrollment.setCourse(course);
                
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return enrollments;
    }
    private Enrollment getEnrollmentById(int enrollmentId) {
        Enrollment enrollment = null;
        String query = "SELECT * FROM enrollments WHERE enrollment_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, enrollmentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                enrollment = new Enrollment();
                enrollment.setEnrollmentId(rs.getInt("enrollment_id"));
                enrollment.setStudentId(rs.getString("student_id"));
                enrollment.setSectionId(rs.getInt("section_id"));
                enrollment.setEnrollmentDate(rs.getTimestamp("enrollment_date"));
                enrollment.setStatus(rs.getString("status"));
                enrollment.setRemarks(rs.getString("remarks"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return enrollment;
    }
    
    private boolean isStudentEnrolled(String studentId, int sectionId) {
        String query = "SELECT COUNT(*) FROM enrollments WHERE student_id = ? AND section_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentId);
            stmt.setInt(2, sectionId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private boolean hasScheduleConflict(String studentId, Section newSection) {
        String query = "SELECT s.schedule FROM enrollments e " +
                      "JOIN sections s ON e.section_id = s.section_id " +
                      "WHERE e.student_id = ? AND e.status != 'Rejected'";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String existingSchedule = rs.getString("schedule");
                if (scheduleConflicts(existingSchedule, newSection.getSchedule())) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private boolean scheduleConflicts(String schedule1, String schedule2) {
        // Simple implementation - would need more complex logic for real-world use
        // Assumes schedule format is "MWF 9:00-10:30" or "TTh 11:00-12:30"
        
        // Extract days and times
        String[] parts1 = schedule1.split(" ");
        String[] parts2 = schedule2.split(" ");
        
        String days1 = parts1[0];
        String days2 = parts2[0];
        
        // Check if any day overlaps
        for (char day : days1.toCharArray()) {
            if (days2.indexOf(day) >= 0) {
                // Same day, now check time overlap
                String[] time1 = parts1[1].split("-");
                String[] time2 = parts2[1].split("-");
                
                // Simple string comparison - assumes correct time format HH:MM
                String start1 = time1[0];
                String end1 = time1[1];
                String start2 = time2[0];
                String end2 = time2[1];
                
                // Check if one time range is completely before or after the other
                if (!((end1.compareTo(start2) <= 0) || (end2.compareTo(start1) <= 0))) {
                    return true; // Time overlap
                }
            }
        }
        
        return false;
    }
    
    private void logEnrollmentHistory(String studentId, String action, String details) {
        String query = "INSERT INTO enrollment_history (student_id, action, details) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentId);
            stmt.setString(2, action);
            stmt.setString(3, details);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}