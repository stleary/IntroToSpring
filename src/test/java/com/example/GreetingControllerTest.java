package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.context.annotation.Import;

@WebMvcTest(value = GreetingController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = SecurityConfig.class))
@Import(GreetingControllerTest.TestSecurityConfig.class)
class GreetingControllerTest {
    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic();
            return http.build();
        }
    }

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private GreetingService greetingService;
    // -------------------------------------------------------
    // GET endpoints; no authentication required
    // -------------------------------------------------------
    @Test
    @WithMockUser
    void getAll_returnsListOfGreetings() throws Exception {
        GreetingModel alice = new GreetingModel("Alice", "Hello, Alice!");
        GreetingModel bob = new GreetingModel("Bob", "Hello, Bob!");
        when(greetingService.findAll()).thenReturn(Arrays.asList(alice, bob));
        mockMvc.perform(get("/greetings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].name").value("Bob"));
    }


    @Test
    @WithMockUser
    void getByName_existingName_returnsGreeting() throws Exception {
        GreetingModel alice = new GreetingModel("Alice", "Hello, Alice!");
        when(greetingService.findByName("Alice")).thenReturn(alice);
        mockMvc.perform(get("/greeting").param("name", "Alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.message").value("Hello, Alice!"));
    }


    @Test
    @WithMockUser
    void getByName_nonExistingName_returns200() throws Exception {
        when(greetingService.findByName("Nobody")).thenReturn(null);
        mockMvc.perform(get("/greeting").param("name", "Nobody"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    void getByName_missingParam_returns400() throws Exception {
        mockMvc.perform(get("/greeting"))
                .andExpect(status().isBadRequest());
    }


    // -------------------------------------------------------
    // POST endpoint; authentication required
    // -------------------------------------------------------


    @Test
    void post_withoutAuth_returns401() throws Exception {
        GreetingModel newGreeting = new GreetingModel("Charlie", "Hello, Charlie!");
        mockMvc.perform(post("/greeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGreeting)))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser
    void post_withAuth_validInput_returns201() throws Exception {
        GreetingModel newGreeting = new GreetingModel("Charlie", "Hello, Charlie!");
        GreetingModel saved = new GreetingModel("Charlie", "Hello, Charlie!");
        when(greetingService.create(any(GreetingModel.class))).thenReturn(saved);
        mockMvc.perform(post("/greeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGreeting)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Charlie"));
    }


    @Test
    @WithMockUser
    void post_withAuth_blankName_returns400() throws Exception {
        GreetingModel invalid = new GreetingModel("", "Hello!");
        mockMvc.perform(post("/greeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser
    void post_withAuth_nameTooLong_returns400() throws Exception {
        String longName = "A".repeat(101);
        GreetingModel invalid = new GreetingModel(longName, "Hello!");
        mockMvc.perform(post("/greeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }


// -------------------------------------------------------
// PUT endpoint — authentication required
// -------------------------------------------------------

    @Test
    void put_withoutAuth_returns401() throws Exception {
        GreetingModel updated = new GreetingModel("Alice", "Updated message");

        mockMvc.perform(put("/greeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void put_withAuth_validInput_returns200() throws Exception {
        GreetingModel updated = new GreetingModel("Alice", "Updated message");
        when(greetingService.update(any(GreetingModel.class))).thenReturn(updated);

        mockMvc.perform(put("/greeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated message"));
    }

    @Test
    @WithMockUser
    void put_withAuth_notFound_returns400() throws Exception {
        GreetingModel updated = new GreetingModel("Nobody", "Updated message");
        when(greetingService.update(any(GreetingModel.class)))
                .thenThrow(new GreetingException("Greeting not found"));

        mockMvc.perform(put("/greeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isBadRequest());
    }

// -------------------------------------------------------
// DELETE endpoint — authentication required
// -------------------------------------------------------

    @Test
    void delete_withoutAuth_returns401() throws Exception {
        mockMvc.perform(delete("/greeting").param("name", "Alice"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void delete_withAuth_returns204() throws Exception {
        when(greetingService.delete("Alice")).thenReturn(1);

        mockMvc.perform(delete("/greeting").param("name", "Alice"))
                .andExpect(status().isNoContent());
    }
}