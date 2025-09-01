package com.insurance.policy.service;

public class PolicyNotFoundException extends RuntimeException {
    private final Long policyId;
    public PolicyNotFoundException(Long policyId) {
        super("Policy not found: " + policyId);
        this.policyId = policyId;
    }
    public Long getPolicyId() { return policyId; }
}
