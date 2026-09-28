package org.example.exception;

/**
 * Thrown when a requested student, course, or enrollment record cannot be found.
 * A checked exception so callers are forced to handle the "not found" case
 * explicitly rather than letting a null slip through.
 */
public class RecordNotFoundException extends Exception {

    public RecordNotFoundException(String message) {
        super(message);
    }
}
