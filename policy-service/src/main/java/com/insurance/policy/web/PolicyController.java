package com.insurance.policy.web;

import com.insurance.policy.domain.Policy;
import com.insurance.policy.service.PolicyService;
import com.insurance.policy.web.dto.PolicyDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;
    private final PolicyMapper mapper;

    @PostMapping
    public ResponseEntity<PolicyResponse> create(@Valid @RequestBody PolicyDto dto) {
        Policy savedPolicy = policyService.save(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(savedPolicy));
    }

    @GetMapping
    public List<PolicyResponse> all() {
        return policyService.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public PolicyResponse one(@PathVariable Long id) {
        return mapper.toResponse(policyService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        policyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
