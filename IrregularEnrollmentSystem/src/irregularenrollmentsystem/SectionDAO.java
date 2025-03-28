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

public class SectionDAO {
    private Connection connection;
    private CourseDAO courseDAO;
    
    public SectionDAO() {
        this.connection = DatabaseConnection.getConnection();
        this.courseDAO = new CourseDAO();
    }
    
    public List<Section> getAvailableSections(String semester, String academicYear) {
        List<Section> sections = new ArrayList<>();
        String query = "SELECT s.*, c.course_name FROM sections s " +
                      "JOIN courses c ON s.course_id = c.course_id " +
                      "WHERE s.semester = ? AND s.academic_year = ? " +
                      "AND s.current_enrolled < s.max_students";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, semester);
            stmt.setString(2, academicYear);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Section section = new Section();
                section.setSectionId(rs.getInt("section_id"));
                section.setCourseId(rs.getString("course_id"));
                section.setSectionNumber(rs.getString("section_number"));
                section.setSemester(rs.getString("semester"));
                section.setAcademicYear(rs.getString("academic_year"));
                section.setRoom(rs.getString("room"));
                section.setSchedule(rs.getString("schedule"));
                section.setInstructor(rs.getString("instructor"));
                section.setMaxStudents(rs.getInt("max_students"));
                section.setCurrentEnrolled(rs.getInt("current_enrolled"));
                
                Course course = new Course();
                course.setCourseId(rs.getString("course_id"));
                course.setCourseName(rs.getString("course_name"));
                section.setCourse(course);
                
                sections.add(section);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return sections;
    }
    
    public Section getSectionById(int sectionId) {
        Section section = null;
        String query = "SELECT s.*, c.course_name FROM sections s " +
                      "JOIN courses c ON s.course_id = c.course_id " +
                      "WHERE s.section_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, sectionId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                section = new Section();
                section.setSectionId(rs.getInt("section_id"));
                section.setCourseId(rs.getString("course_id"));
                section.setSectionNumber(rs.getString("section_number"));
                section.setSemester(rs.getString("semester"));
                section.setAcademicYear(rs.getString("academic_year"));
                section.setRoom(rs.getString("room"));
                section.setSchedule(rs.getString("schedule"));
                section.setInstructor(rs.getString("instructor"));
                section.setMaxStudents(rs.getInt("max_students"));
                section.setCurrentEnrolled(rs.getInt("current_enrolled"));
                
                Course course = new Course();
                course.setCourseId(rs.getString("course_id"));
                course.setCourseName(rs.getString("course_name"));
                section.setCourse(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return section;
    }
    
    public boolean updateSectionEnrollment(int sectionId, boolean increase) {
        String query = "UPDATE sections SET current_enrolled = current_enrolled " + (increase ? "+ 1" : "- 1") +
                      " WHERE section_id = ? AND " + (increase ? "current_enrolled < max_students" : "current_enrolled > 0");
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, sectionId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}