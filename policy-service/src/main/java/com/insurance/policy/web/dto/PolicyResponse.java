package com.insurance.policy.web.dto;

import java.time.LocalDate;

public record PolicyResponse(
        Long id,
        String policyNumber,
        String customerId,
        Double coverageAmount,
        LocalDate effectiveDate
) {}
