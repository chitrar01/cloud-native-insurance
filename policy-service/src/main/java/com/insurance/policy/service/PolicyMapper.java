package com.insurance.policy.service;

import com.insurance.policy.domain.Customer;
import com.insurance.policy.domain.Policy;
import com.insurance.policy.web.dto.CustomerDto;
import com.insurance.policy.web.dto.PolicyDto;
import com.insurnane.policy.web.dto.PolicyCreateRequest;


public final class PolicyMapper {
    private PolicyMapper() {}

    public static Customer toCustomerEntity(PolicyCreateRequest.CustomerRequest r){
        return new Customer(r.firstName(),r.lastName(),r.email(),r.phone());
    }

    public static Policy toPolicyEntity(PolicyCreateRequest req, Customer customer){
        return new Policy(req.policyNumber(), customer, req.coverageAmount(), req.effectiveDate());
    }

    public static CustomerDto toCustomerDto (Customer c){
        return new CustomerDto(c.getId(),c.getFirstName(), c.getLastName(), c.getEmail(), c.getPhone());
    }

    public static PolicyDto toPolicyDto (Policy p) {
        return new PolicyDto (p.getId(), p.getPolicyNumber(), toDto(p.getCustomer()),
                    p.getCoverageAmount(),p.getEffectiveDate(),p.getCreatedAt(),p.getUpdatedAt());
    }
}