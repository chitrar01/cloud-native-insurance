package com.insurance.policy.repo;

import com.insurance.policy.domain.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

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

    @Override
    @EntityGraph(attributePaths = "customer")
    List <Policy> findAll();

    @Override
    @EntityGraph(attributePaths = "customer")
    Optional<Policy> findById(Long id);
}
