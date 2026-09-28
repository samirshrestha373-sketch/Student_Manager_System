package org.example.model;

/**
 * Represents a student's enrollment in a course, including their marks
 * for that course (nullable until graded).
 */
public class Enrollment {

    private int id;
    private int studentId;
    private int courseId;
    private Double marks; // null until recorded

    public Enrollment(int id, int studentId, int courseId, Double marks) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.marks = marks;
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public Double getMarks() {
        return marks;
    }

    public void setMarks(Double marks) {
        this.marks = marks;
    }
}
