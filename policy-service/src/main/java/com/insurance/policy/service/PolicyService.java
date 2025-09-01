package com.insurance.policy.service;

import com.insurance.policy.domain.Policy;
import com.insurance.policy.repo.PolicyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyService {

    private final PolicyRepository repo;

    public PolicyService(PolicyRepository repo) {
        this.repo = repo;
    }

    public Policy create(Policy policy) {
        if (repo.existsByPolicyNumber(policy.getPolicyNumber())) {
            throw new DuplicatePolicyNumberException(policy.getPolicyNumber());
        }
        return repo.save(policy);
    }

    public List<Policy> findAll() {
        return repo.findAll();
    }

    public Policy findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new PolicyNotFoundException(id));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new PolicyNotFoundException(id);
        repo.deleteById(id);
    }
}
