package org.example.dao;

import org.example.exception.DuplicateRecordException;
import org.example.exception.RecordNotFoundException;
import org.example.model.Course;
import org.example.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Course records.
 */
public class CourseDAO {

    public Course create(Course c) throws DuplicateRecordException, SQLException {
        String sql = "INSERT INTO courses (course_code, course_name, credits) VALUES (?, ?, ?)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getCourseCode());
            ps.setString(2, c.getCourseName());
            ps.setInt(3, c.getCredits());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setId(keys.getInt(1));
                }
            }
            return c;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DuplicateRecordException("A course with code " + c.getCourseCode() + " already exists.");
        }
    }

    public Course findById(int id) throws RecordNotFoundException, SQLException {
        String sql = "SELECT * FROM courses WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        throw new RecordNotFoundException("No course found with ID " + id);
    }

    public List<Course> findAll() throws SQLException {
        String sql = "SELECT * FROM courses ORDER BY id";
        List<Course> results = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.add(mapRow(rs));
            }
        }
        return results;
    }

    public void update(Course c) throws RecordNotFoundException, SQLException {
        String sql = "UPDATE courses SET course_code = ?, course_name = ?, credits = ? WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getCourseCode());
            ps.setString(2, c.getCourseName());
            ps.setInt(3, c.getCredits());
            ps.setInt(4, c.getId());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RecordNotFoundException("No course found with ID " + c.getId());
            }
        }
    }

    public void delete(int id) throws RecordNotFoundException, SQLException {
        String sql = "DELETE FROM courses WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RecordNotFoundException("No course found with ID " + id);
            }
        }
    }

    private Course mapRow(ResultSet rs) throws SQLException {
        return new Course(
                rs.getInt("id"),
                rs.getString("course_code"),
                rs.getString("course_name"),
                rs.getInt("credits")
        );
    }
}
