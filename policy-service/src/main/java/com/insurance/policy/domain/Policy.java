package com.insurance.policy.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.insurance.policy.domain.Customer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Entity
@Table(name = "POLICIES")   
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Policy extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "policy_number", nullable = false, unique = true, length = 16)
    private String policyNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    @Column(name = "coverage_amount", nullable = false)
    private Double coverageAmount;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    

    public Policy(String policyNumber, Customer customer, Double coverageAmount, LocalDate effectiveDate) {
        this.policyNumber = policyNumber;
        this.customer = customer;
        this.coverageAmount = coverageAmount;
        this.effectiveDate = effectiveDate;
    }

}
