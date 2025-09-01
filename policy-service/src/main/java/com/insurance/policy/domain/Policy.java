package com.insurance.policy.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "policies", indexes = {
        @Index(name = "uk_policy_number", columnList = "policyNumber", unique = true)
})
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 16)
    private String policyNumber;

    @Column(nullable = false, length = 64)
    private String customerId;

    @Column(nullable = false)
    private Double coverageAmount;

    @Column(nullable = false)
    private LocalDate effectiveDate;

    protected Policy() {} // JPA

    public Policy(String policyNumber, String customerId, Double coverageAmount, LocalDate effectiveDate) {
        this.policyNumber = policyNumber;
        this.customerId = customerId;
        this.coverageAmount = coverageAmount;
        this.effectiveDate = effectiveDate;
    }

    public Long getId() { return id; }
    public String getPolicyNumber() { return policyNumber; }
    public String getCustomerId() { return customerId; }
    public Double getCoverageAmount() { return coverageAmount; }
    public LocalDate getEffectiveDate() { return effectiveDate; }

    // Keep setters package-private to discourage direct mutation outside JPA
    void setPolicyNumber(String v) { this.policyNumber = v; }
    void setCustomerId(String v) { this.customerId = v; }
    void setCoverageAmount(Double v) { this.coverageAmount = v; }
    void setEffectiveDate(LocalDate v) { this.effectiveDate = v; }
}
