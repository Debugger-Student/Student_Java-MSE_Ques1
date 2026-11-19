package com.student.management.exception;

public class DuplicateStudentException extends RuntimeException {

    public DuplicateStudentException(String message) {
        super(message);
    }
}
