package com.annette.spring.courses_system.project.exception_handling;

public class NoPlacesOnCourseException extends RuntimeException {

    public NoPlacesOnCourseException(String message) {
        super(message);
    }

}
