/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irregularenrollmentsystem;

/**
 *
 * @author sophi
 */
public class Section {
    private int sectionId;
    private String courseId;
    private String sectionNumber;
    private String semester;
    private String academicYear;
    private String room;
    private String schedule;
    private String instructor;
    private int maxStudents;
    private int currentEnrolled;
    private Course course;
    
    // Constructors
    public Section() {}
    
    public Section(int sectionId, String courseId, String sectionNumber, String semester, 
                  String academicYear, String schedule, String instructor, int maxStudents) {
        this.sectionId = sectionId;
        this.courseId = courseId;
        this.sectionNumber = sectionNumber;
        this.semester = semester;
        this.academicYear = academicYear;
        this.schedule = schedule;
        this.instructor = instructor;
        this.maxStudents = maxStudents;
    }
    
    // Getters and setters
    public int getSectionId() { return sectionId; }
    public void setSectionId(int sectionId) { this.sectionId = sectionId; }
    
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    
    public String getSectionNumber() { return sectionNumber; }
    public void setSectionNumber(String sectionNumber) { this.sectionNumber = sectionNumber; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    
    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
    
    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    
    public int getMaxStudents() { return maxStudents; }
    public void setMaxStudents(int maxStudents) { this.maxStudents = maxStudents; }
    
    public int getCurrentEnrolled() { return currentEnrolled; }
    public void setCurrentEnrolled(int currentEnrolled) { this.currentEnrolled = currentEnrolled; }
    
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    
    @Override
    public String toString() {
        return courseId + " " + sectionNumber + " - " + schedule + " (" + instructor + ")";
    }
}