package org.example.service;

import org.example.dao.CourseDAO;
import org.example.dao.EnrollmentDAO;
import org.example.exception.DuplicateRecordException;
import org.example.exception.RecordNotFoundException;
import org.example.model.Course;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Business-logic layer for courses and enrollment actions.
 */
public class CourseService {

    private final CourseDAO courseDAO = new CourseDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    public Course addCourse(String code, String name, int credits) throws DuplicateRecordException, SQLException {
        return courseDAO.create(new Course(0, code, name, credits));
    }

    public Course getCourse(int id) throws RecordNotFoundException, SQLException {
        return courseDAO.findById(id);
    }

    public List<Course> getAllCourses() throws SQLException {
        return courseDAO.findAll();
    }

    /** TreeMap keeps courses naturally ordered by course code, e.g. for display. */
    public Map<String, Course> getCoursesByCode() throws SQLException {
        Map<String, Course> byCode = new TreeMap<>();
        for (Course c : courseDAO.findAll()) {
            byCode.put(c.getCourseCode(), c);
        }
        return byCode;
    }

    public void updateCourse(Course c) throws RecordNotFoundException, SQLException {
        courseDAO.update(c);
    }

    public void deleteCourse(int id) throws RecordNotFoundException, SQLException {
        courseDAO.delete(id);
    }

    public void enrollStudent(int studentId, int courseId) throws DuplicateRecordException, SQLException {
        enrollmentDAO.enroll(studentId, courseId);
    }

    public void unenrollStudent(int studentId, int courseId) throws RecordNotFoundException, SQLException {
        enrollmentDAO.unenroll(studentId, courseId);
    }

    public void recordMarks(int studentId, int courseId, double marks) throws RecordNotFoundException, SQLException {
        enrollmentDAO.updateMarks(studentId, courseId, marks);
    }

    /** Course codes as a formatted list, purely to demonstrate stream-based filtering/formatting. */
    public List<String> getCourseSummaries() throws SQLException {
        return courseDAO.findAll().stream()
                .map(c -> c.getCourseCode() + " - " + c.getCourseName())
                .collect(Collectors.toList());
    }
}
