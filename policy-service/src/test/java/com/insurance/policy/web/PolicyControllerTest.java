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

@WebMvcTest(controllers=PolicyController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class PolicyControllerTest {
    @Autowired MockMvc mockMvc;

    @MockBean PolicyService policyService;

    @Test
    @DisplayName("POST /api/policies: returns 201 Created when policy is created successfully")
    void testCreatePolicy() throws Exception {

        //When
        when(policyService.create(any())).thenAnswer(i -> i.getArgument(0));

        // Use mockMvc to perform POST request and verify response
        mockMvc.perform(post("/api/policies")
                .contentType("application/json")
                .content("""
                    {
                        "policyNumber": "POL-12345",
                        "customerId": "CUST001",
                        "coverageAmount": 100000.0,
                        "effectiveDate": "%s"
                    }
                    """.formatted(java.time.LocalDate.now().plusDays(1))
                ))
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST validates request and returns ProblemDetail (400)")
    void testCreatePolicyValidationError() throws Exception {
        // Use mockMvc to perform POST request with invalid data
        mockMvc.perform(post("/api/policies")
                .contentType("application/json")
                .content("""
                    {
                        "customerId": "CUST001",
                        "coverageAmount": -100000.0,
                        "effectiveDate": "%s"
                    }
                    """.formatted(java.time.LocalDate.now().plusDays(1))
                ))
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
        // When
        when(policyService.create(any()))
            .thenThrow(new com.insurance.policy.service.DuplicatePolicyNumberException("Duplicate policy number: POL-12345"));

        // Use mockMvc to perform POST request with duplicate policy number
        mockMvc.perform(post("/api/policies")
                .contentType("application/json")
                .content("""
                    {
                        "policyNumber": "POL-12345",
                        "customerId": "CUST001",
                        "coverageAmount": 100000.0,
                        "effectiveDate": "%s"
                    }
                    """.formatted(java.time.LocalDate.now().plusDays(1))
                ))
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
            //.andExpect(jsonPath("$.type").value("about:blank"))
            .andExpect(jsonPath("$.title").value("Policy not found"))
            //.andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.detail").exists());
            //.andExpect(jsonPath("$.instance").value("/api/policies/1"));
    }


}
