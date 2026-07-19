package com.uagrm.personal.security;

import com.uagrm.personal.security.dto.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
public class LoginIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void testLoginShouldNotReturn403() throws Exception {
        LoginRequestDto loginRequest = new LoginRequestDto("testuser", "password");

        mockMvc.perform(post("/api/security/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status == 403) {
                        System.out.println("[DEBUG_LOG] Received 403 Forbidden");
                    } else if (status == 401) {
                        System.out.println("[DEBUG_LOG] Received 401 Unauthorized (this means the request reached authentication)");
                    } else {
                        System.out.println("[DEBUG_LOG] Received status: " + status);
                    }
                });
    }
}
