package com.insurance.policy.web.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record PolicyCreateRequest(
        @NotBlank(message = "{policy.number.required}")
        @Pattern(regexp = "[A-Z]{3}-\\d{5}", message = "{policy.number.pattern}")
        String policyNumber,

        @NotNull(message = "{coverage.amount.required}")
        @DecimalMin(value = "1.0", inclusive = true, message = "{coverage.amount.min}")
        Double coverageAmount,

        @NotNull(message = "{effective.date.required}")
        @Future(message = "{effective.date.future}")
        LocalDate effectiveDate

        @NotNull CustomerRequest customer;
){
    public record CustomerRequest(
            @NotBlank String firstName,
            @NotBlank String lastName,
            @Email @NotBlank String email,
            String phone
    ){}
}
