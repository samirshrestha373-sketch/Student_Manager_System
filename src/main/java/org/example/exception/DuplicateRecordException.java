package org.example.exception;

/**
 * Thrown when an operation would create a duplicate record, e.g. enrolling
 * a student in a course they are already enrolled in, or reusing a student ID.
 */
public class DuplicateRecordException extends Exception {

    public DuplicateRecordException(String message) {
        super(message);
    }
}
