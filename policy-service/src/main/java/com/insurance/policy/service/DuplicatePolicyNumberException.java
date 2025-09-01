package com.insurance.policy.service;

public class DuplicatePolicyNumberException extends RuntimeException {
    private final String policyNumber;
    public DuplicatePolicyNumberException(String policyNumber) {
        super("Duplicate policy number: " + policyNumber);
        this.policyNumber = policyNumber;
    }
    public String getPolicyNumber() { return policyNumber; }
}
