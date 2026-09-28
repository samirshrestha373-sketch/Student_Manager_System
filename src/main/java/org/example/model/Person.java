package org.example.model;

/**
 * Abstract base class representing any person in the system.
 * Demonstrates abstraction + encapsulation. Extended by Student and Instructor.
 */
public abstract class Person {

    private int id;
    private String name;
    private String email;
    private String phone;

    public Person(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Every concrete subtype must describe how it displays itself.
     * This is overridden differently by Student and Instructor (polymorphism).
     */
    public abstract String displayInfo();

    /**
     * Every concrete subtype must define its role in the system.
     */
    public abstract String getRole();
}
