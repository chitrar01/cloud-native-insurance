package com.insurance.policy.service;

import com.insurance.policy.domain.Policy;
import com.insurance.policy.repo.PolicyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class PolicyServiceTest {
    private final PolicyRepository repo = mock(PolicyRepository.class);
    private final PolicyService policyService = new PolicyService(repo);

    @Test
    @DisplayName("create: saves policy when policyNumber is unique")
    void testCreatePolicySuccess() {
        Policy newPolicy = new Policy("POL-12345", "CUST001", 100000.0, java.time.LocalDate.now());
        when(repo.findByPolicyNumber("POL-12345")).thenReturn(Optional.empty());
        when(repo.save(any(Policy.class))).thenAnswer(i -> i.getArgument(0));
        
        Policy createdPolicy = policyService.create(newPolicy);
        assertThat(createdPolicy).isNotNull();
        assertThat(createdPolicy.getPolicyNumber()).isEqualTo("POL-12345");
        verify(repo).save(newPolicy);

        ArgumentCaptor<Policy> policyCaptor = ArgumentCaptor.forClass(Policy.class);
        verify(repo).save(policyCaptor.capture());
        assertThat(policyCaptor.getValue().getPolicyNumber()).isEqualTo("POL-12345");
    }

    @Test
    @DisplayName("create: throws exception when policyNumber already exists")
    void testCreatePolicyDuplicate() {
        Policy existingPolicy = new Policy("POL-12345", "CUST001", 100000.0, java.time.LocalDate.now());
        when(repo.findByPolicyNumber("POL-12345")).thenReturn(Optional.of(existingPolicy));

        Policy newPolicy = new Policy("POL-12345", "CUST002", 150000.0, java.time.LocalDate.now().plusDays(1));
        
        assertThatThrownBy(() -> policyService.create(newPolicy))
                .isInstanceOf(DuplicatePolicyNumberException.class)
                .hasMessageContaining("Duplicate policy number");
        
        verify(repo, never()).save(any(Policy.class));
    }

    @Test
    @DisplayName("get: finds by id or throws PolicyNotFoundException")
    void testGetPolicyByIdNotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> policyService.findById(1L))
                .isInstanceOf(PolicyNotFoundException.class)
                .hasMessageContaining("Policy not found with id: 1");
        
        verify(repo).findById(1L);
    }
    
}
