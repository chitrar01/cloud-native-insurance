package com.insurance.policy.web;

import com.insurance.policy.service.PolicyNotFoundException;
import org.springframework.http.*;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation failed");
        pd.setType(URI.create("https://api.example.com/errors/invalid-request"));
        // include first field error plus a map of all errors
        String detail = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(err -> (err instanceof FieldError fe)
                        ? fe.getField() + ": " + fe.getDefaultMessage()
                        : err.getDefaultMessage())
                .orElse("Validation error");
        pd.setDetail(detail);
        pd.setProperty("errors", ex.getBindingResult().getFieldErrors().stream()
                .collect(java.util.stream.Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a,b)->a)));
        return pd;
    }

    @ExceptionHandler(PolicyNotFoundException.class)
    public ProblemDetail handleNotFound(PolicyNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Policy not found");
        pd.setType(URI.create("https://api.example.com/errors/not-found"));
        pd.setDetail(ex.getMessage());
        pd.setProperty("policyId", ex.getPolicyId());
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Unexpected error");
        pd.setType(URI.create("https://api.example.com/errors/internal"));
        pd.setDetail("Please contact support with the correlation ID.");
        return pd;
    }
}
