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

    private final PolicyRepository policies;
    private final CustomerRepository customers;
    private static final Logger log = LoggerFactory.getLogger(PolicyService.class);
    
    public PolicyService(PolicyRepository policies, CustomerRepository customers) {
        this.policies = policies;
        this.customers = customers;
    }

    @Transacational
    public PolicyDto create(PolicyCreateRequest req) {

        Customer customer = customers.findByEmail(req.customer().email()).orElseGet() -> customers.save(PolicyMapper.toCustomerEntity(req.customer));
        Policy policy = PolicyMapper.toPolicyEntity(req, customer);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        var username = (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";
        if (policies.existsByPolicyNumber(policy.getPolicyNumber())) {
            throw new DuplicatePolicyNumberException(policy.getPolicyNumber());
        }
        log.info("User {} created policy {}", username, policy.getPolicyNumber());
        
        Policy savedPolicy = policies.save(policy);
        return PolicyMapper.toPolicyDto(savedPolicy);

    }
    @Transacational(readOnly = true)
    public List<Policy> findAll() {
        return policies.findAll();
    }

    @Transacational(readOnly = true)
    public Policy findById(Long id) {
        return policies.findById(id).orElseThrow(() -> new PolicyNotFoundException(id));
    }

    @Transactional
    public void delete(Long id) {
        if (!policies.existsById(id)) throw new PolicyNotFoundException(id);
        polciies.deleteById(id);
    }
}
