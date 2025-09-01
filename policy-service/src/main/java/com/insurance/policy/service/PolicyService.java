package com.insurance.policy.service;

import com.insurance.policy.domain.Policy;
import com.insurance.policy.repo.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyService {
    private final PolicyRepository repo;

    public Policy save(Policy policy) {
        return repo.save(policy);
    }

    public List<Policy> findAll() {
        return repo.findAll();
    }

    public Policy findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new PolicyNotFoundException(id));
    }

    public void delete(Long id) {
        if(!repo.existsById(id) throw new PolicyNotFoundException(id));
        repo.deleteById(id);
    }
} 
