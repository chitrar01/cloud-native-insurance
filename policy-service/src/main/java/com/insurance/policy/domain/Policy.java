package com.insurance.policy.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "policies", schema = "PUBLIC")   
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "policy_number", nullable = false, unique = true, length = 16)
    private String policyNumber;

    @Column(name = "customer_id", nullable = false, length = 64)
    private String customerId;

    @Column(name = "coverage_amount", nullable = false)
    private Double coverageAmount;

    @Column(name = "effective_date", nullable = false)
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
