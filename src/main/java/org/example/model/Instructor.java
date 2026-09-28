package org.example.model;

/**
 * Represents a course instructor. A second concrete subtype of Person,
 * demonstrating that the abstract class supports multiple, differently
 * behaving implementations (polymorphism).
 */
public class Instructor extends Person {

    private String department;

    public Instructor(int id, String name, String email, String phone, String department) {
        super(id, name, email, phone);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String displayInfo() {
        return String.format("[Instructor] ID: %-4d Name: %-20s Dept: %-15s Email: %s",
                getId(), getName(), department, getEmail());
    }

    @Override
    public String getRole() {
        return "INSTRUCTOR";
    }

    @Override
    public String toString() {
        return displayInfo();
    }
}
