package com.albaag.todo.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@RestControllerAdvice
public class GlobalErrorController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleTaskProductNotFound(TaskNotFoundException taskNotFoundException) {
        ProblemDetail result = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, taskNotFoundException.getMessage());
        result.setTitle("La tarea no ha sido encontrada :(");
        result.setType(URI.create("http://www.openwebinars.net/errors/task-not-found"));
        return result;
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ProblemDetail handleAuthException(AuthenticationException ex) {
        ProblemDetail result = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        result.setTitle("Error de autenticación -_-");
        result.setType(URI.create("http://www.openwebinars.net/errors/authentication"));
        return result;
    }


}