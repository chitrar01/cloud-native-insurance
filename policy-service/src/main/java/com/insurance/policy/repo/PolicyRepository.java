package com.insurance.policy.repo;

import com.insurance.policy.domain.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    Optional<Policy> findByPolicyNumber(String policyNumber);
    boolean existsByPolicyNumber(String policyNumber);
}
