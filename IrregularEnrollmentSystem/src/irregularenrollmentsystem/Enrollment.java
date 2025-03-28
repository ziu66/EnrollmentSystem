/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irregularenrollmentsystem;

/**
 *
 * @author sophi
 */
import java.sql.Timestamp;

public class Enrollment {
    private int enrollmentId;
    private String studentId;
    private int sectionId;
    private Timestamp enrollmentDate;
    private String status;
    private String remarks;
    private Section section;
    private Course course;
    
    // Constructors
    public Enrollment() {}
    
    public Enrollment(String studentId, int sectionId) {
        this.studentId = studentId;
        this.sectionId = sectionId;
    }
    
    // Getters and setters
    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public int getSectionId() { return sectionId; }
    public void setSectionId(int sectionId) { this.sectionId = sectionId; }
    
    public Timestamp getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(Timestamp enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    
    public Section getSection() { return section; }
    public void setSection(Section section) { this.section = section; }
    
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
}
