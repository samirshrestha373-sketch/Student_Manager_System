package org.example.model;

import java.time.LocalDate;

/**
 * Represents a student. Extends the abstract Person class.
 */
public class Student extends Person implements Comparable<Student> {

    private LocalDate enrollmentDate;
    private double averageMarks; // computed field, not persisted directly

    public Student(int id, String name, String email, String phone, LocalDate enrollmentDate) {
        super(id, name, email, phone);
        this.enrollmentDate = enrollmentDate;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public double getAverageMarks() {
        return averageMarks;
    }

    public void setAverageMarks(double averageMarks) {
        this.averageMarks = averageMarks;
    }

    @Override
    public String displayInfo() {
        return String.format("[Student] ID: %-4d Name: %-20s Email: %-25s Phone: %-15s Enrolled: %s",
                getId(), getName(), getEmail(), getPhone(), enrollmentDate);
    }

    @Override
    public String getRole() {
        return "STUDENT";
    }

    /**
     * Natural ordering by average marks descending, used for ranking reports.
     * Demonstrates use of Comparable alongside Comparator in the service layer.
     */
    @Override
    public int compareTo(Student other) {
        return Double.compare(other.averageMarks, this.averageMarks);
    }

    @Override
    public String toString() {
        return displayInfo();
    }
}
