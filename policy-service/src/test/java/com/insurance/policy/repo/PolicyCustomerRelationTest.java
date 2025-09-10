package com.insurance.policy.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.insurance.policy.domain.Customer;
import com.insurance.policy.domain.Policy;

import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class PolicyCustomerRelationTest {
    @Autowired CustomerRepository customerRepository;
    @Autowired PolicyRepository policyRepository;

    @Test
    @DisplayName("Test policy - customer relation")
    void testSavePolicyWithCustomer(){
        var customer = customerRepository.save(new Customer("Alice", "Johnson", "alice.johnosn@abc.com", "0400000000"));
        var policy = new Policy("ABC-12345",customer, 1500.00, LocalDate.now().plusDays(1));

        var savedPolicy = policyRepository.save(policy);
        assertThat(savedPolicy.getId()).isNotNull();
        assertThat(savedPolicy.getCustomer().getEmail()).isEqualTo(customer.getEmail());
        assertThat(savedPolicy.getCreatedAt()).isNotNull();
        assertThat(savedPolicy.getUpdatedAt()).isNotNull();
    }
}