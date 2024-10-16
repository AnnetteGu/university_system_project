package com.annette.spring.courses_system.project.exception_handling;

public class WrongTimeException extends RuntimeException {

    public WrongTimeException(String message) {
        super(message);
    }

}
