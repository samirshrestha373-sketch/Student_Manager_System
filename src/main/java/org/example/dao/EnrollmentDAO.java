package org.example.dao;

import org.example.exception.DuplicateRecordException;
import org.example.exception.RecordNotFoundException;
import org.example.model.Enrollment;
import org.example.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Enrollment records (the many-to-many link between
 * students and courses, carrying the marks for that student in that course).
 */
public class EnrollmentDAO {

    public Enrollment enroll(int studentId, int courseId) throws DuplicateRecordException, SQLException {
        String sql = "INSERT INTO enrollments (student_id, course_id, marks) VALUES (?, ?, NULL)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return new Enrollment(keys.getInt(1), studentId, courseId, null);
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DuplicateRecordException("Student " + studentId + " is already enrolled in course " + courseId);
        }
    }

    public void unenroll(int studentId, int courseId) throws RecordNotFoundException, SQLException {
        String sql = "DELETE FROM enrollments WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RecordNotFoundException("Student " + studentId + " is not enrolled in course " + courseId);
            }
        }
    }

    public void updateMarks(int studentId, int courseId, double marks) throws RecordNotFoundException, SQLException {
        String sql = "UPDATE enrollments SET marks = ? WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, marks);
            ps.setInt(2, studentId);
            ps.setInt(3, courseId);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RecordNotFoundException("Student " + studentId + " is not enrolled in course " + courseId);
            }
        }
    }

    public List<Enrollment> findByStudent(int studentId) throws SQLException {
        String sql = "SELECT * FROM enrollments WHERE student_id = ?";
        List<Enrollment> results = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    public List<Enrollment> findAll() throws SQLException {
        String sql = "SELECT * FROM enrollments";
        List<Enrollment> results = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.add(mapRow(rs));
            }
        }
        return results;
    }

    private Enrollment mapRow(ResultSet rs) throws SQLException {
        double marks = rs.getDouble("marks");
        Double marksObj = rs.wasNull() ? null : marks;
        return new Enrollment(
                rs.getInt("id"),
                rs.getInt("student_id"),
                rs.getInt("course_id"),
                marksObj
        );
    }
}
