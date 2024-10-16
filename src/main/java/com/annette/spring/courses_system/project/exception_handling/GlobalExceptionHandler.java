package com.annette.spring.courses_system.project.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleException(
        NoPlacesOnCourseException exception) {
           
            IncorrectData data = new IncorrectData();

            data.setInfo(exception.getMessage());

            return new ResponseEntity<>(data, HttpStatus.OK);

    }

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleException(
        WrongTimeException exception) {

        IncorrectData data = new IncorrectData();

        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.OK);

    }

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleException(
        StudentAlreadyOnCourseException exception) {

        IncorrectData data = new IncorrectData();

        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.OK);

    }

}
