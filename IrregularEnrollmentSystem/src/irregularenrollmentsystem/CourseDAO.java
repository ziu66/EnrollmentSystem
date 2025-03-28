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

public class CourseDAO {
    private Connection connection;
    
    public CourseDAO() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    public Course getCourseById(String courseId) {
        Course course = null;
        String query = "SELECT * FROM courses WHERE course_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, courseId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                course = new Course();
                course.setCourseId(rs.getString("course_id"));
                course.setCourseName(rs.getString("course_name"));
                course.setDescription(rs.getString("description"));
                course.setCredits(rs.getInt("credits"));
                course.setDepartment(rs.getString("department"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return course;
    }
    
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM courses";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getString("course_id"));
                course.setCourseName(rs.getString("course_name"));
                course.setDescription(rs.getString("description"));
                course.setCredits(rs.getInt("credits"));
                course.setDepartment(rs.getString("department"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return courses;
    }
    
    public boolean hasPrerequisites(String courseId, String studentId) {
        String query = "SELECT p.prereq_course_id FROM prerequisites p " +
                      "WHERE p.course_id = ? AND NOT EXISTS " +
                      "(SELECT 1 FROM student_history sh " +
                      "WHERE sh.student_id = ? AND sh.course_id = p.prereq_course_id " +
                      "AND sh.status = 'Passed')";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, courseId);
            stmt.setString(2, studentId);
            ResultSet rs = stmt.executeQuery();
            
            return !rs.next(); // Return true if all prerequisites are met
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}