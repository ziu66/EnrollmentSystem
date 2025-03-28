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

public class StudentDAO {
    private Connection connection;
    
    public StudentDAO() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    public Student getStudentById(String studentId) {
        Student student = null;
        String query = "SELECT * FROM students WHERE student_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                student = new Student();
                student.setStudentId(rs.getString("student_id"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                student.setEmail(rs.getString("email"));
                student.setPhone(rs.getString("phone"));
                student.setAddress(rs.getString("address"));
                student.setDateOfBirth(rs.getDate("date_of_birth"));
                student.setAcademicStatus(rs.getString("academic_status"));
                student.setProgram(rs.getString("program"));
                student.setYearLevel(rs.getInt("year_level"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return student;
    }
    
    public Student login(String studentId, String password) {
        Student student = null;
        String query = "SELECT * FROM students WHERE student_id = ? AND password = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentId);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                student = new Student();
                student.setStudentId(rs.getString("student_id"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                student.setEmail(rs.getString("email"));
                student.setPhone(rs.getString("phone"));
                student.setProgram(rs.getString("program"));
                student.setYearLevel(rs.getInt("year_level"));
                student.setAcademicStatus(rs.getString("academic_status"));
                
                // Update last login
                updateLastLogin(studentId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return student;
    }
    
    private void updateLastLogin(String studentId) {
        String query = "UPDATE students SET last_login = CURRENT_TIMESTAMP WHERE student_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}