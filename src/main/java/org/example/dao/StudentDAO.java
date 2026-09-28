package org.example.dao;

import org.example.exception.DuplicateRecordException;
import org.example.exception.RecordNotFoundException;
import org.example.model.Student;
import org.example.util.DBConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student records. Every method opens its own
 * connection/statement/result set inside try-with-resources so nothing
 * is ever leaked, and every query that touches user input goes through
 * a PreparedStatement.
 */
public class StudentDAO {

    public Student create(Student s) throws DuplicateRecordException, SQLException {
        String sql = "INSERT INTO students (id, name, email, phone, enrollment_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, s.getId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getPhone());
            ps.setDate(5, Date.valueOf(s.getEnrollmentDate()));
            ps.executeUpdate();
            return s;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DuplicateRecordException("A student with ID " + s.getId() + " (or that email) already exists.");
        }
    }

    public Student findById(int id) throws RecordNotFoundException, SQLException {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        throw new RecordNotFoundException("No student found with ID " + id);
    }

    public List<Student> findByName(String namePart) throws SQLException {
        String sql = "SELECT * FROM students WHERE name LIKE ? ORDER BY name";
        List<Student> results = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + namePart + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    public List<Student> findAll() throws SQLException {
        String sql = "SELECT * FROM students ORDER BY id";
        List<Student> results = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.add(mapRow(rs));
            }
        }
        return results;
    }

    public void update(Student s) throws RecordNotFoundException, SQLException {
        String sql = "UPDATE students SET name = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setString(3, s.getPhone());
            ps.setInt(4, s.getId());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RecordNotFoundException("No student found with ID " + s.getId());
            }
        }
    }

    public void delete(int id) throws RecordNotFoundException, SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RecordNotFoundException("No student found with ID " + id);
            }
        }
    }

    /** Students enrolled in a given course, joined through the enrollments table. */
    public List<Student> findByCourse(int courseId) throws SQLException {
        String sql = "SELECT s.* FROM students s " +
                "JOIN enrollments e ON s.id = e.student_id " +
                "WHERE e.course_id = ? ORDER BY s.name";
        List<Student> results = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getDate("enrollment_date").toLocalDate()
        );
    }
}
