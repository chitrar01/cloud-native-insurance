package com.insurance.policy.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
                        "startDate": "%s"
                    }
                    """.formatted(java.time.LocalDate.now())
                ))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/policies/POL-12345"));
    }
}
