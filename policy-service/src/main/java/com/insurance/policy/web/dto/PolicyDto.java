package com.insurance.policy.web.dto;

import java.time.LocalDate;

public record PolicyDto(
        Long id,
        String policyNumber,
        CustomerDto customer;
        String customerId,
        Double coverageAmount,
        LocalDate effectiveDate;
        Instant createdAt;
        Instant updatedAt;
) {

}
