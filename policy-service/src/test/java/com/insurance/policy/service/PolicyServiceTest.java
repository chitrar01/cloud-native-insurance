package com.insurance.policy.service;

import com.insurance.policy.domain.Policy;
import com.insurance.policy.domain.Customer;
import com.insurance.policy.repo.PolicyRepository;
import com.insurance.policy.repo.CustomerRepository;
import com.insurance.policy.web.dto.PolicyDto;
import com.insurance.policy.web.dto.PolicyCreateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class PolicyServiceTest {
    private final PolicyRepository repo = mock(PolicyRepository.class);
    private final CustomerRepository custRepo = mock(CustomerRepository.class);
    private final PolicyService policyService = new PolicyService(repo,custRepo);

    @Test
    @DisplayName("create: saves policy when policyNumber is unique")
    void testCreatePolicySuccess() {
        var req = new PolicyCreateRequest(
                    "POL-12345",
                    100000.0,
                    LocalDate.now().plusDays(1),
                    new PolicyCreateRequest.CustomerRequest(
                        "Alice", "Johnson", "alice.johnosn@abc.com", "0400000000"
                        )
                    );
        when(repo.existsByPolicyNumber("POL-12345")).thenReturn(false);

        when(custRepo.findByEmail("alice.johnosn@abc.com")).thenReturn(Optional.empty());
        when(custRepo.save(any(Customer.class))).thenAnswer(i -> i.getArgument(0));

        when(repo.save(any(Policy.class))).thenAnswer(i -> i.getArgument(0));
        
        PolicyDto createdPolicy = policyService.create(req);
        assertThat(createdPolicy).isNotNull();
        assertThat(createdPolicy.policyNumber()).isEqualTo("POL-12345");
        assertThat(createdPolicy.customer().email()).isEqualTo("alice.johnosn@abc.com");

        ArgumentCaptor<Policy> policyCaptor = ArgumentCaptor.forClass(Policy.class);
        verify(repo).save(policyCaptor.capture());
        assertThat(policyCaptor.getValue().getPolicyNumber()).isEqualTo("POL-12345");

        verify(repo).existsByPolicyNumber("POL-12345");
        verify(custRepo).findByEmail("alice.johnosn@abc.com");
    }

    @Test
    @DisplayName("create: throws exception when policyNumber already exists")
    void testCreatePolicyDuplicate() {
        var req = new PolicyCreateRequest(
                "POL-12345",
                150000.0,
                LocalDate.now().plusDays(2),
                new PolicyCreateRequest.CustomerRequest(
                        "Alice", "John", "alice.john@abc.com", "0400000000"
                )
        );

        when(repo.existsByPolicyNumber("POL-12345")).thenReturn(true);

        assertThatThrownBy(() -> policyService.create(req))
                .isInstanceOf(DuplicatePolicyNumberException.class)
                .hasMessageContaining("Duplicate policy number");

        verify(repo, never()).save(any(Policy.class));
        verify(custRepo, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("get: finds by id or throws PolicyNotFoundException")
    void testGetPolicyByIdNotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> policyService.findById(1L))
                .isInstanceOf(PolicyNotFoundException.class)
                .hasMessageContaining("Policy not found: 1");

        verify(repo).findById(1L);
    }
    
}
