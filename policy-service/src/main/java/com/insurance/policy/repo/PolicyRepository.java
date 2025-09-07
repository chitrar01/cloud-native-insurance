package com.insurance.policy.repo;

import com.insurance.policy.domain.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {
    /**
     * Retrieves a policy by its policy number.
     *
     * @param policyNumber the unique policy number to search for
     * @return an Optional containing the found Policy, or empty if not found
     */
    Optional<Policy> findByPolicyNumber(String policyNumber);
    boolean existsByPolicyNumber(String policyNumber);
}
