package com.insurance.policy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PolicyIntegrationTest {
    //Create end to end integration tests

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper om;

    @Test
    @DisplayName("Create and retrieve policy end to end")
    void testCreateAndRetrievePolicy() throws Exception{
        // Implement end to end test using MockMvc to create and then retrieve a policy
        //Create a policy via POST /api/policies
        var requestBody = """
            {
                "policyNumber": "POL-12345",
                "customerId": "CUST001",
                "coverageAmount": 100000.0,
                "effectiveDate": "%s"
            }
            """.formatted(java.time.LocalDate.now().plusDays(1));
        // Use mockMvc to perform POST request and verify response
        var createdPolicy = mockMvc.perform(post("/api/policies")
                .with(jwt().jwt(j -> j.subject("user").claim("roles", List.of("USER"))))
                .contentType("application/json")
                .content(requestBody)
            )
            //.andDo(result -> System.out.println("Create response: " + result.getResponse().getContentAsString()))
            .andExpect(status().isCreated());
            //.andExpect(header().string("Location", "/api/policies/POL-12345"));

        var jsonResponse = createdPolicy.andReturn().getResponse().getContentAsString();
        var id = om.readTree(jsonResponse).get("id").asText();

        //Retrieve the created policy via GET /api/policies/{policyNumber}

        mockMvc.perform(get("/api/policies/{id}",id)
            .with(jwt().jwt(j -> j.subject("user").claim("roles", List.of("USER")))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.policyNumber").value("POL-12345"))
            .andExpect(jsonPath("$.customerId").value("CUST001"))
            .andExpect(jsonPath("$.coverageAmount").value(100000.0));
    }

    
}
