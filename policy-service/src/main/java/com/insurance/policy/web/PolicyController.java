package com.insurance.policy.web;

import com.insurance.policy.domain.Policy;
import com.insurance.policy.service.PolicyService;
import com.insurance.policy.web.dto.PolicyCreateRequest;
import com.insurance.policy.web.dto.PolicyResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyService service;

    public PolicyController(PolicyService service) {
        this.service = service;
    }
    
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PolicyResponse> create(@Valid @RequestBody PolicyCreateRequest req) {
        Policy toSave = new Policy(
                req.policyNumber(),
                req.customerId(),
                req.coverageAmount(),
                req.effectiveDate()
        );
        Policy saved = service.create(toSave);

        PolicyResponse resp = new PolicyResponse(
                saved.getId(),
                saved.getPolicyNumber(),
                saved.getCustomerId(),
                saved.getCoverageAmount(),
                saved.getEffectiveDate()
        );

        return ResponseEntity.created(URI.create("/policies/" + saved.getId())).body(resp);
    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public List<PolicyResponse> all() {
        return service.findAll().stream()
                .map(p -> new PolicyResponse(
                        p.getId(), p.getPolicyNumber(), p.getCustomerId(),
                        p.getCoverageAmount(), p.getEffectiveDate()))
                .toList();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public PolicyResponse one(@PathVariable Long id) {
        var p = service.findById(id);
        return new PolicyResponse(p.getId(), p.getPolicyNumber(), p.getCustomerId(),
                p.getCoverageAmount(), p.getEffectiveDate());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
