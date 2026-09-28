package org.example.service;

import org.example.dao.EnrollmentDAO;
import org.example.dao.StudentDAO;
import org.example.exception.DuplicateRecordException;
import org.example.exception.RecordNotFoundException;
import org.example.model.Enrollment;
import org.example.model.Student;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

/**
 * Business-logic layer for students. Keeps a HashMap cache (ID -> Student)
 * for fast in-memory lookups on top of the database-backed DAO, and uses
 * a List for ordered iteration/sorting. The cache is refreshed from the
 * database whenever it's used, so the DB always remains the source of truth.
 */
public class StudentService {

    private final StudentDAO studentDAO = new StudentDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    /** ID -> Student, for O(1) lookups once loaded. */
    private final Map<Integer, Student> studentCache = new HashMap<>();

    public Student addStudent(int id, String name, String email, String phone) throws DuplicateRecordException, SQLException {
        Student s = new Student(id, name, email, phone, LocalDate.now());
        studentDAO.create(s);
        studentCache.put(s.getId(), s);
        return s;
    }

    public Student getStudent(int id) throws RecordNotFoundException, SQLException {
        Student s = studentDAO.findById(id);
        studentCache.put(id, s);
        return s;
    }

    public List<Student> searchByName(String namePart) throws SQLException {
        return studentDAO.findByName(namePart);
    }

    /** Ordered List of all students, refreshing the ID -> Student cache as it goes. */
    public List<Student> getAllStudents() throws SQLException {
        List<Student> all = studentDAO.findAll();
        studentCache.clear();
        for (Student s : all) {
            studentCache.put(s.getId(), s);
        }
        return all;
    }

    public void updateStudent(Student s) throws RecordNotFoundException, SQLException {
        studentDAO.update(s);
        studentCache.put(s.getId(), s);
    }

    public void deleteStudent(int id) throws RecordNotFoundException, SQLException {
        studentDAO.delete(id);
        studentCache.remove(id);
    }

    public List<Student> getStudentsByCourse(int courseId) throws SQLException {
        return studentDAO.findByCourse(courseId);
    }

    /**
     * Computes each student's average marks across all their (graded)
     * enrollments and returns the list sorted by average descending
     * (uses Student's Comparable + a Comparator, and Java's sort/streams).
     */
    public List<Student> getRankedStudents() throws SQLException {
        List<Student> all = studentDAO.findAll();
        List<Enrollment> allEnrollments = enrollmentDAO.findAll();

        // group marks per student id
        Map<Integer, List<Double>> marksByStudent = new HashMap<>();
        for (Enrollment e : allEnrollments) {
            if (e.getMarks() != null) {
                marksByStudent.computeIfAbsent(e.getStudentId(), k -> new ArrayList<>()).add(e.getMarks());
            }
        }

        for (Student s : all) {
            List<Double> marks = marksByStudent.getOrDefault(s.getId(), Collections.emptyList());
            double avg = marks.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            s.setAverageMarks(avg);
        }

        all.sort(Comparator.comparingDouble(Student::getAverageMarks).reversed());
        return all;
    }
}
