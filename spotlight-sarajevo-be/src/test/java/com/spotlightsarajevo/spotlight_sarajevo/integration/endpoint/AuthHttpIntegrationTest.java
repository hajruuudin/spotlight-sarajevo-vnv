package com.spotlightsarajevo.spotlight_sarajevo.integration.endpoint;

import com.spotlightsarajevo.modules.auth.api.dto.SystemLoginModel;
import com.spotlightsarajevo.spotlight_sarajevo.integration.BaseIntegrationClass;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthHttpIntegrationTest extends BaseIntegrationClass {
    @Test
    void loginSystem_validCredentials_returns200() throws Exception {
        SystemLoginModel request = new SystemLoginModel();
        request.setEmail(TEST_USER_EMAIL);
        request.setPassword("password");

        mockMvc.perform(post("/auth/login/system")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void loginSystem_invalidCredentials_returns401() throws Exception {
        SystemLoginModel request = new SystemLoginModel();
        request.setEmail(TEST_USER_EMAIL);
        request.setPassword("wrongpassword");

        mockMvc.perform(post("/auth/login/system")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginSystem_nonExistentUser_returns404() throws Exception {
        SystemLoginModel request = new SystemLoginModel();
        request.setEmail("ghost@test.com");
        request.setPassword("password");

        mockMvc.perform(post("/auth/login/system")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void me_validToken_returns200() throws Exception {
        mockMvc.perform(post("/auth/me")
                        .cookie(new Cookie("spo-sar", userToken)))
                .andExpect(status().isOk());
    }

    @Test
    void me_noToken_returns401() throws Exception {
        mockMvc.perform(post("/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logout_validToken_returns200() throws Exception {
        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }
}
