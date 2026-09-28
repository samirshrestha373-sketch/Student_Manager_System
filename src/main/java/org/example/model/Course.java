package org.example.model;

/**
 * Represents a course that students can enroll in.
 */
public class Course {

    private int id;
    private String courseCode;
    private String courseName;
    private int credits;

    public Course(int id, String courseCode, String courseName, int credits) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return String.format("ID: %-4d Code: %-10s Name: %-25s Credits: %d", id, courseCode, courseName, credits);
    }
}
