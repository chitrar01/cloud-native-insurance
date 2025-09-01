package com.insurance.policy.web.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PolicyDto {

    private Long id;

    @NotBlank(message = "{policy.number.required}")
    @Pattern(regexp = "[A-Z]{3}-\\d{5}", message = "{policy.number.pattern}")
    private String policyNumber;

    @NotBlank(message = "{policy.customer.required}")
    private String customerId;

    @NotNull(message = "{policy.coverage.required}")
    @Min(value = 1, message = "{policy.coverage.min}")
    private Double coverageAmount;

    @NotNull(message = "{policy.effective.required}")
    @Future(message = "{policy.effective.future}")
    private LocalDate effectiveDate;

}
