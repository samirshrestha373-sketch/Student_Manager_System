package org.example;

import org.example.exception.DuplicateRecordException;
import org.example.exception.RecordNotFoundException;
import org.example.model.Course;
import org.example.model.Student;
import org.example.service.CourseService;
import org.example.service.StudentService;
import org.example.util.InputValidator;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Menu-driven console entry point for the Student Management System.
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final StudentService studentService = new StudentService();
    private static final CourseService courseService = new CourseService();

    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println(" STUDENT MANAGEMENT SYSTEM");
        System.out.println("=====================================");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = InputValidator.readInt(sc, "Enter choice: ");
            try {
                switch (choice) {
                    case 1 -> addStudent();
                    case 2 -> viewAllStudents();
                    case 3 -> updateStudent();
                    case 4 -> deleteStudent();
                    case 5 -> searchStudent();
                    case 6 -> addCourse();
                    case 7 -> viewAllCourses();
                    case 8 -> enrollStudent();
                    case 9 -> unenrollStudent();
                    case 10 -> listStudentsByCourse();
                    case 11 -> recordMarks();
                    case 12 -> classRankingReport();
                    case 0 -> {
                        System.out.println("Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please pick an option from the menu.");
                }
            } catch (RecordNotFoundException | DuplicateRecordException e) {
                // domain-level errors: expected, so we just message the user
                System.out.println("Error: " + e.getMessage());
            } catch (SQLException e) {
                // database-level errors: log meaningfully instead of a bare stack trace
                System.out.println("A database error occurred: " + e.getMessage());
            }
        }
        sc.close();
    }

    private static void printMenu() {
        System.out.println("\n--- MENU ---");
        System.out.println(" 1. Add Student");
        System.out.println(" 2. View All Students");
        System.out.println(" 3. Update Student");
        System.out.println(" 4. Delete Student");
        System.out.println(" 5. Search Student (by ID or name)");
        System.out.println(" 6. Add Course");
        System.out.println(" 7. View All Courses");
        System.out.println(" 8. Enroll Student in Course");
        System.out.println(" 9. Unenroll Student from Course");
        System.out.println("10. List Students in a Course");
        System.out.println("11. Record/Update Marks");
        System.out.println("12. Class Ranking Report");
        System.out.println(" 0. Exit");
    }

    private static void addStudent() throws SQLException, DuplicateRecordException {
        int id = InputValidator.readPositiveInt(sc, "Student ID: ");
        String name = InputValidator.readNonEmptyString(sc, "Name: ");
        String email = InputValidator.readNonEmptyString(sc, "Email: ");
        String phone = InputValidator.readNonEmptyString(sc, "Phone: ");
        studentService.addStudent(id, name, email, phone);
        System.out.println("Student added successfully.");
    }

    private static void viewAllStudents() throws SQLException {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        students.forEach(s -> System.out.println(s.displayInfo()));
    }

    private static void updateStudent() throws RecordNotFoundException, SQLException {
        int id = InputValidator.readInt(sc, "Student ID to update: ");
        Student existing = studentService.getStudent(id);
        System.out.println("Current: " + existing.displayInfo());

        String name = InputValidator.readOptionalString(sc, "New name (blank to keep '" + existing.getName() + "'): ");
        String email = InputValidator.readOptionalString(sc, "New email (blank to keep '" + existing.getEmail() + "'): ");
        String phone = InputValidator.readOptionalString(sc, "New phone (blank to keep '" + existing.getPhone() + "'): ");

        if (!name.isEmpty()) existing.setName(name);
        if (!email.isEmpty()) existing.setEmail(email);
        if (!phone.isEmpty()) existing.setPhone(phone);

        studentService.updateStudent(existing);
        System.out.println("Student updated successfully.");
    }

    private static void deleteStudent() throws RecordNotFoundException, SQLException {
        int id = InputValidator.readInt(sc, "Student ID to delete: ");
        studentService.deleteStudent(id);
        System.out.println("Student deleted successfully.");
    }

    private static void searchStudent() throws SQLException {
        System.out.println("Search by: 1) ID  2) Name");
        int mode = InputValidator.readInt(sc, "Choice: ");
        if (mode == 1) {
            int id = InputValidator.readInt(sc, "Student ID: ");
            try {
                System.out.println(studentService.getStudent(id).displayInfo());
            } catch (RecordNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            String name = InputValidator.readNonEmptyString(sc, "Name (or part of it): ");
            List<Student> results = studentService.searchByName(name);
            if (results.isEmpty()) {
                System.out.println("No matching students.");
            } else {
                results.forEach(s -> System.out.println(s.displayInfo()));
            }
        }
    }

    private static void addCourse() throws DuplicateRecordException, SQLException {
        String code = InputValidator.readNonEmptyString(sc, "Course code: ");
        String name = InputValidator.readNonEmptyString(sc, "Course name: ");
        int credits = InputValidator.readPositiveInt(sc, "Credits: ");
        courseService.addCourse(code, name, credits);
        System.out.println("Course added successfully.");
    }

    private static void viewAllCourses() throws SQLException {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        courses.forEach(System.out::println);
    }

    private static void enrollStudent() throws DuplicateRecordException, SQLException {
        int studentId = InputValidator.readInt(sc, "Student ID: ");
        int courseId = InputValidator.readInt(sc, "Course ID: ");
        courseService.enrollStudent(studentId, courseId);
        System.out.println("Enrollment successful.");
    }

    private static void unenrollStudent() throws RecordNotFoundException, SQLException {
        int studentId = InputValidator.readInt(sc, "Student ID: ");
        int courseId = InputValidator.readInt(sc, "Course ID: ");
        courseService.unenrollStudent(studentId, courseId);
        System.out.println("Unenrolled successfully.");
    }

    private static void listStudentsByCourse() throws SQLException {
        int courseId = InputValidator.readInt(sc, "Course ID: ");
        List<Student> students = studentService.getStudentsByCourse(courseId);
        if (students.isEmpty()) {
            System.out.println("No students enrolled in this course.");
            return;
        }
        students.forEach(s -> System.out.println(s.displayInfo()));
    }

    private static void recordMarks() throws RecordNotFoundException, SQLException {
        int studentId = InputValidator.readInt(sc, "Student ID: ");
        int courseId = InputValidator.readInt(sc, "Course ID: ");
        double marks = InputValidator.readDoubleInRange(sc, "Marks (0-100): ", 0, 100);
        courseService.recordMarks(studentId, courseId, marks);
        System.out.println("Marks recorded successfully.");
    }

    private static void classRankingReport() throws SQLException {
        List<Student> ranked = studentService.getRankedStudents();
        if (ranked.isEmpty()) {
            System.out.println("No students to rank.");
            return;
        }
        System.out.println("\n--- CLASS RANKING (by average marks) ---");
        int rank = 1;
        for (Student s : ranked) {
            System.out.printf("%2d. %-20s (ID %d) - Avg: %.2f%n", rank++, s.getName(), s.getId(), s.getAverageMarks());
        }
    }
}
