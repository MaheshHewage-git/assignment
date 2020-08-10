package com.assignment.handler;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<List<ErrorMessage>> handleException(MethodArgumentNotValidException ex) {
        final BindingResult result = ex.getBindingResult();

        List<FieldError> fieldErrors = result.getFieldErrors();
        return ResponseEntity.badRequest().body(processFieldErrors(fieldErrors));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorMessage> handleException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new ErrorMessage("error", ex.getMessage()));
    }

    private List<ErrorMessage> processFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
        final List<ErrorMessage> error = new ArrayList<>();
        for (org.springframework.validation.FieldError fieldError : fieldErrors) {
            error.add(new ErrorMessage(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return error;
    }
}
