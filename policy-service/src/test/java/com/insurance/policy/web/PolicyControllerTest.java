package com.insurance.policy.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.insurance.policy.service.PolicyService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDate;
import com.insurance.policy.web.PolicyController;
import com.insurance.policy.web.PolicyCreateRequest;
import com.insurance.policy.web.PolicyDto;
import com.insurance.policy.web.CustomerDto;

@WebMvcTest(controllers=PolicyController.class)
@ActiveProfiles("test")
public class PolicyControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean PolicyService policyService;

    @Test
    @DisplayName("POST /api/policies: returns 201 Created when policy is created successfully")
    void testCreatePolicy() throws Exception {
        // Given
        var now = Instant.now();
        var response = new PolicyDto(
            1L,
            "POL-12345",
            new CustomerDto(10L, "Alice", "Johnson", "alice.johnson@example.com", "0400000000"),
            100000.0,
            LocalDate.now().plusDays(5),
            now, now
        );

        //When
        when(policyService.create(any(PolicyCreateRequest.class))).thenReturn(response);

        var req = new PolicyCreateRequest(
            "POL-12345",
            100000.0,
            LocalDate.now().plusDays(7),
            new PolicyCreateRequest.CustomerRequest(
                "Alice", "Johnson", "alice.johnson@example.com", "0400000000"
            )
        );

        // Use mockMvc to perform POST request and verify response        
        mockMvc.perform(post("/api/policies")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.policyNumber").value("POL-12345"))
            .andExpect(jsonPath("$.customer.email").value("alice.johnson@example.com"))
            .andExpect(jsonPath("$.coverageAmount").value(100000.0))
            .andExpect(jsonPath("$.createdAt").exists())
            .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    @DisplayName("POST validates request and returns ProblemDetail (400)")
    void testCreatePolicyValidationError() throws Exception {
        var badRequest = new PolicyCreateRequest(
            "", -5.0, LocalDate.now().minusDays(1),
            new PolicyCreateRequest.CustomerRequest("", "J", "not-an-email", "0400000000")
        );

        // Use mockMvc to perform POST request with invalid data
        mockMvc.perform(post("/api/policies")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(badRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.title").value("Bad Request"))
            //.andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.detail").exists())
            .andExpect(jsonPath("$.errors.policyNumber").value("Policy number is required"))
            .andExpect(jsonPath("$.errors.coverageAmount").value("Coverage amount must be greater than or equal to 1.0"));
    }

    @Test
    @DisplayName("Post Duplicate Policy Number returns 409 Conflict")
    void testCreatePolicyDuplicatePolicyNumber() throws Exception {
        // Given
        var request = new PolicyCreateRequest(
            "POL-12345",
            100000.0,
            LocalDate.now().plusDays(7),
            new PolicyCreateRequest.CustomerRequest(
                "Alice", "Johnson", "alice.johnson@example.com", "0400000000"
            )
        );

        // When
        when(policyService.create(any(PolicyCreateRequest.class)))
            .thenThrow(new com.insurance.policy.service.DuplicatePolicyNumberException("Duplicate policy number: POL-12345"));

        // Use mockMvc to perform POST request with duplicate policy number
        mockMvc.perform(post("/api/policies")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isConflict())
            //.andExpect(jsonPath("$.type").value("about:blank"))
            .andExpect(jsonPath("$.title").value("Duplicate policy number"))
            //.andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.detail").exists());
            //.andExpect(jsonPath("$.instance").value("/api/policies"));
    }

    @Test
    @DisplayName("GET /api/policies/{id}: returns 404 Not Found when policy does not exist")
    void testGetPolicyNotFound() throws Exception {
        // When
        when(policyService.findById(1L))
            .thenThrow(new com.insurance.policy.service.PolicyNotFoundException(1L));

        // Use mockMvc to perform GET request for non-existent policy
        mockMvc.perform(get("/api/policies/1"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.title").value("Policy not found"))
            .andExpect(jsonPath("$.detail").exists());
            //.andExpect(jsonPath("$.instance").value("/api/policies/1"));
    }


}