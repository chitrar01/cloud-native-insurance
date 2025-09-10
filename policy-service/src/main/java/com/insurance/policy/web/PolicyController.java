package com.insurance.policy.web;

import com.insurance.policy.domain.Policy;
import com.insurance.policy.service.PolicyService;
import com.insurance.policy.web.dto.PolicyCreateRequest;
import com.insurance.policy.web.dto.PolicyDto;
import com.insurance.policy.service.PolicyMapper;

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
    
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<PolicyDto> create(@Valid @RequestBody PolicyCreateRequest req) {
        Policy policyCreated = service.create(req);
        return ResponseEntity.created(URI.create("/api/policies/" + policyCreated.getId())).body(policyCreated);
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping(produces = "application/json")
    public List<PolicyDto> all() {
        return service.findAll().stream()
                .map(PolicyMapper::toPolicyDto)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping(value="/{id}", produces = "application/json")
    public PolicyDto one(@PathVariable Long id) {
        var p = service.findById(id);
        return PolicyMapper.toPolicyDto(p);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
