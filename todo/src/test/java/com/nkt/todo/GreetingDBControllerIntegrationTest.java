package com.nkt.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Roll back database changes after each test
class GreetingDBControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GreetingRepository greetingRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addGreeting_shouldCreateGreetingAndReturnIt() throws Exception {
        GreetingDTO newGreeting = new GreetingDTO("Hello Integration Test");

        mockMvc.perform(post("/greetings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGreeting)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.message", is("Hello Integration Test")))
                .andExpect(jsonPath("$.createdAt", notNullValue()));
    }

    @Test
    void getAllGreetings_shouldReturnListOfGreetings() throws Exception {
        greetingRepository.save(new GreetingEntity("First"));
        greetingRepository.save(new GreetingEntity("Second"));

        mockMvc.perform(get("/greetings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].message", is("First")))
                .andExpect(jsonPath("$[1].message", is("Second")));
    }

    @Test
    void getGreetingById_whenExists_shouldReturnGreeting() throws Exception {
        GreetingEntity saved = greetingRepository.save(new GreetingEntity("Find Me"));

        mockMvc.perform(get("/greetings/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId().intValue())))
                .andExpect(jsonPath("$.message", is("Find Me")));
    }

    @Test
    void getGreetingById_whenNotExists_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/greetings/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateGreeting_whenExists_shouldUpdateAndReturnGreeting() throws Exception {
        GreetingEntity saved = greetingRepository.save(new GreetingEntity("Original Message"));
        GreetingDTO updatedDto = new GreetingDTO("Updated Message");

        mockMvc.perform(put("/greetings/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId().intValue())))
                .andExpect(jsonPath("$.message", is("Updated Message")));
    }

    @Test
    void updateGreeting_whenNotExists_shouldReturnNotFound() throws Exception {
        GreetingDTO updatedDto = new GreetingDTO("Updated Message");

        mockMvc.perform(put("/greetings/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteGreetingById_whenExists_shouldDeleteAndReturnNoContent() throws Exception {
        GreetingEntity saved = greetingRepository.save(new GreetingEntity("To Be Deleted"));

        mockMvc.perform(delete("/greetings/" + saved.getId()))
                .andExpect(status().isNoContent());

        assertFalse(greetingRepository.existsById(saved.getId()));
    }

    @Test
    void deleteGreetingById_whenNotExists_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/greetings/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addGreeting_withInvalidBody_shouldReturnBadRequest() throws Exception {
        GreetingDTO invalidGreeting = new GreetingDTO(""); // Blank message

        mockMvc.perform(post("/greetings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidGreeting)))
                .andExpect(status().isBadRequest());
    }
}