package com.annette.spring.courses_system.project.exception_handling;

public class StudentAlreadyOnCourseException extends RuntimeException {

    public StudentAlreadyOnCourseException(String message) {
        super(message);
    }

}
