package com.example;

import com.example.model.GreetingModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GreetingIntegrationTest {
    @MockBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser
    void getAll_returnsSeededData() throws Exception {
        mockMvc.perform(get("/greetings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNumber());
    }


    @Test
    @WithMockUser
    void createAndRetrieve_fullRoundTrip() throws Exception {
        // Create a new greeting
        GreetingModel newGreeting = new GreetingModel("TestUser", "Hello from integration test!");
        mockMvc.perform(post("/greeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGreeting)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("TestUser"));
        // Retrieve it by name
        mockMvc.perform(get("/greeting").param("name", "TestUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestUser"))
                .andExpect(jsonPath("$.message").value("Hello from integration test!"));
    }


    @Test
    @WithMockUser
    void createUpdateDelete_fullLifecycle() throws Exception {
        // Create
        GreetingModel greeting = new GreetingModel("Lifecycle", "Version 1");
        mockMvc.perform(post("/greeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(greeting)))
                .andExpect(status().isCreated());
        // Update
        GreetingModel updated = new GreetingModel("Lifecycle", "Version 2");
        mockMvc.perform(put("/greeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Version 2"));
        // Delete
        mockMvc.perform(delete("/greeting").param("name", "Lifecycle"))
                .andExpect(status().isNoContent());
        // Verify it's gone
        mockMvc.perform(get("/greeting").param("name", "Lifecycle"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }


    @Test
    @WithMockUser
    void post_invalidInput_returns400WithErrorDetails() throws Exception {
        GreetingModel invalid = new GreetingModel("", "");
        mockMvc.perform(post("/greeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
}