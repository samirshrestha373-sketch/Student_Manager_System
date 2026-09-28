-- Student Management System - Database Schema
-- Run this once against your MySQL server to set everything up:
--   mysql -u root -p < schema.sql

CREATE DATABASE IF NOT EXISTS student_management_system;
USE student_management_system;

-- ---------------------------------------------------------------
-- students
-- ---------------------------------------------------------------
CREATE TABLE IF NOT EXISTS students (
    id               INT PRIMARY KEY,
    name             VARCHAR(100) NOT NULL,
    email            VARCHAR(100) NOT NULL UNIQUE,
    phone            VARCHAR(20),
    enrollment_date  DATE NOT NULL
);

-- ---------------------------------------------------------------
-- courses
-- ---------------------------------------------------------------
CREATE TABLE IF NOT EXISTS courses (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    course_code  VARCHAR(20) NOT NULL UNIQUE,
    course_name  VARCHAR(100) NOT NULL,
    credits      INT NOT NULL DEFAULT 3
);

-- ---------------------------------------------------------------
-- enrollments (many-to-many link between students and courses)
-- ---------------------------------------------------------------
CREATE TABLE IF NOT EXISTS enrollments (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    student_id  INT NOT NULL,
    course_id   INT NOT NULL,
    marks       DOUBLE NULL,
    CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_enrollment_course  FOREIGN KEY (course_id)  REFERENCES courses(id)  ON DELETE CASCADE,
    CONSTRAINT uq_student_course UNIQUE (student_id, course_id)
);

-- ---------------------------------------------------------------
-- Optional sample data - uncomment to try the app out quickly
-- ---------------------------------------------------------------
-- INSERT INTO students (id, name, email, phone, enrollment_date) VALUES
--   (1, 'Aarav Sharma', 'aarav@example.com', '9800000001', '2024-01-10'),
--   (2, 'Priya Gurung', 'priya@example.com', '9800000002', '2024-01-11');
--
-- INSERT INTO courses (course_code, course_name, credits) VALUES
--   ('CS101', 'Introduction to Programming', 3),
--   ('CS102', 'Data Structures', 4);
--
-- INSERT INTO enrollments (student_id, course_id, marks) VALUES
--   (1, 1, 85.5),
--   (1, 2, 78.0),
--   (2, 1, 91.0);
