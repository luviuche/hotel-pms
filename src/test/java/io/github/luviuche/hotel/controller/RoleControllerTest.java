package io.github.luviuche.hotel.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Web layer integration tests covering the Role CRUD and the 404 error handling.
 * They run under the "test" profile (in-memory H2).
 */
@SpringBootTest
@ActiveProfiles("test")
class RoleControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void fullCrudCycle() throws Exception {
        // Create
        String created = mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"MANAGER\",\"description\":\"Runs a property\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("MANAGER"))
                .andReturn().getResponse().getContentAsString();

        int id = JsonPath.parse(created).read("$.id", Integer.class);

        // Read
        mockMvc.perform(get("/api/roles/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("MANAGER"));

        // Update
        mockMvc.perform(put("/api/roles/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"MANAGER\",\"description\":\"Updated text\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated text"));

        // Delete
        mockMvc.perform(delete("/api/roles/" + id))
                .andExpect(status().isNoContent());

        // Gone for good
        mockMvc.perform(get("/api/roles/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void unknownIdReturns404WithErrorBody() throws Exception {
        mockMvc.perform(get("/api/roles/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists());
    }
}
