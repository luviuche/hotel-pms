package io.github.luviuche.hotel.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.github.luviuche.hotel.AbstractIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Checks that entities exposed straight through the API serialise safely: the
 * password never shows up, and foreign keys appear as a plain id (roleId)
 * without tripping lazy loading when the record is read back.
 */
class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void createUserHidesPasswordAndExposesRoleId() throws Exception {
        // A role to attach the user to.
        String role = mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"CORPORATE\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int roleId = JsonPath.parse(role).read("$.id", Integer.class);

        String body = "{"
                + "\"roleId\":" + roleId + ","
                + "\"name\":\"Ana\","
                + "\"lastName\":\"Perez\","
                + "\"email\":\"ana@mail.com\","
                + "\"password\":\"secret123\","
                + "\"phone\":\"3001234567\","
                + "\"documentNumber\":\"CC-100\""
                + "}";

        // Create: the password is accepted on input but never returned.
        String created = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.roleId").value(roleId))
                .andExpect(jsonPath("$.email").value("ana@mail.com"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andReturn().getResponse().getContentAsString();
        int userId = JsonPath.parse(created).read("$.id", Integer.class);

        // Read: "role" comes back as a lazy proxy; getRoleId() does not initialise it.
        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleId").value(roleId))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void duplicateEmailReturns400() throws Exception {
        String role = mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"FRONT_DESK\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int roleId = JsonPath.parse(role).read("$.id", Integer.class);

        String body = "{"
                + "\"roleId\":" + roleId + ","
                + "\"name\":\"Luis\",\"lastName\":\"Gomez\","
                + "\"email\":\"luis@mail.com\",\"password\":\"x\","
                + "\"phone\":\"300\",\"documentNumber\":\"CC-200\""
                + "}";

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());

        // Same email, different document -> business rule (400).
        String duplicate = body.replace("CC-200", "CC-201");
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON).content(duplicate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void unknownRoleReturns404() throws Exception {
        String body = "{"
                + "\"roleId\":888888,"
                + "\"name\":\"No\",\"lastName\":\"Role\","
                + "\"email\":\"norole@mail.com\",\"password\":\"x\","
                + "\"phone\":\"300\",\"documentNumber\":\"CC-300\""
                + "}";

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
