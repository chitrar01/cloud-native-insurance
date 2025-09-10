package com.insurance.policy.web.dto;

import java.time.LocalDate;
import java.time.Instant;
import com.insurance.policy.web.dto.CustomerDto;

public record PolicyDto(
        Long id,
        String policyNumber,
        CustomerDto customer,
        String customerId,
        Double coverageAmount,
        LocalDate effectiveDate,
        Instant createdAt,
        Instant updatedAt
) {

}
