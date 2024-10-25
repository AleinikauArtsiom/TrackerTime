package com.example.timetracker.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerGlobal {

    @ExceptionHandler(value = {CustomValidException.class, jakarta.validation.ValidationException.class})
    public ResponseEntity<HttpStatusCode> customValidExceptionHandler(Exception exception) {
        log.error("Valid exception: " + exception);
        return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<HttpStatusCode> usernameNotFoundException(Exception exception) {
        log.error("Username not found: " + exception);
        return new ResponseEntity<>(HttpStatus.valueOf(401));
    }

    @ExceptionHandler(value = {SameUserInDataBase.class})
    public ResponseEntity<HttpStatusCode> SameUserInDataBase(Exception exception) {
        log.error(String.valueOf(exception));
        return new ResponseEntity<>(HttpStatus.valueOf(409));
    }

    @ExceptionHandler(value = {UserReqEmailException.class})
    public ResponseEntity<HttpStatusCode> UserReqEmailException(Exception exception) {
        log.error(String.valueOf(exception));
        return new ResponseEntity<>(HttpStatus.valueOf(409));
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<HttpStatusCode> NotFoundException(Exception exception) {
        log.error(String.valueOf(exception));
        return new ResponseEntity<>(HttpStatus.valueOf(404));
    }

    @ExceptionHandler(value = {ClassCastException.class})
    public ResponseEntity<String> classCastException(ClassCastException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserHasProjectsException.class)
    public ResponseEntity<String> handleUserHasProjectsException(UserHasProjectsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
