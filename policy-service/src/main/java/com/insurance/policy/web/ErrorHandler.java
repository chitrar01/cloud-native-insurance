package com.insurance.policy.web;

import com.insurance.policy.service.DuplicatePolicyNumberException;
import com.insurance.policy.service.PolicyNotFoundException;
import org.springframework.http.*;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a,b)->a));
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Bad Request");
        pd.setType(URI.create("https://api.example.com/errors/invalid-request"));
        pd.setDetail(errors.entrySet().stream().findFirst().map(Map.Entry::toString).orElse("Invalid input"));
        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(PolicyNotFoundException.class)
    public ProblemDetail handleNotFound(PolicyNotFoundException ex) {
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Policy not found");
        pd.setType(URI.create("https://api.example.com/errors/not-found"));
        pd.setDetail(ex.getMessage());
        pd.setProperty("policyId", ex.getPolicyId());
        return pd;
    }

    @ExceptionHandler(DuplicatePolicyNumberException.class)
    public ProblemDetail handleDuplicate(DuplicatePolicyNumberException ex) {
        var pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("Duplicate policy number");
        pd.setType(URI.create("https://api.example.com/errors/conflict"));
        pd.setDetail(ex.getMessage());
        pd.setProperty("policyNumber", ex.getPolicyNumber());
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        var pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Unexpected error");
        pd.setType(URI.create("https://api.example.com/errors/internal"));
        pd.setDetail("Please contact support with the correlation ID.");
        return pd;
    }
}
