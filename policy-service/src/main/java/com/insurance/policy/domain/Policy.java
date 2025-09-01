package com.insurance.policy.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String policyNumber;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private Double coverageAmount;

    @Column(nullable = false)
    private LocalDate effectiveDate;
}
