package com.insurance.policy.repo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.insurance.policy.domain.Policy;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class PolicyRepositoryTest {
    @Autowired
    private PolicyRepository policyRepository;
    
    @Test
    @DisplayName( "existsByPolicyNumber: returns true if policy number exists")
    void testExistsByPolicyNumber() {
        // Given
        String policyNumber = "POL-12345";
        Customer customer = new Customer("Alice", "Johnson", "alice.johnosn@abc.com", "0400000000");
        Policy policy = new Policy(policyNumber, customer, 100000.0, LocalDate.now().plusDays(1));
        policyRepository.save(policy);
        
        // When
        boolean exists = policyRepository.existsByPolicyNumber(policyNumber);
        
        // Then
        assertThat(exists).isTrue();
    }
}
