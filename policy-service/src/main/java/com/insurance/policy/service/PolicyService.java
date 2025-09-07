package com.insurance.policy.service;

import com.insurance.policy.domain.Policy;
import com.insurance.policy.repo.PolicyRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PolicyService {

    private final PolicyRepository repo;
    private static final Logger log = LoggerFactory.getLogger(PolicyService.class);
    
    public PolicyService(PolicyRepository repo) {
        this.repo = repo;
    }

    public Policy create(Policy policy) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var username = (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";
        if (repo.existsByPolicyNumber(policy.getPolicyNumber())) {
            throw new DuplicatePolicyNumberException(policy.getPolicyNumber());
        }
        log.info("User {} created policy {}", username, policy.getPolicyNumber());
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
