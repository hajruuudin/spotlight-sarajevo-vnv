package com.spotlightsarajevo.spotlight_sarajevo.integration;

import com.spotlightsarajevo.modules.auth.api.dto.SystemLoginModel;
import com.spotlightsarajevo.modules.auth.domain.UserDAO;
import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthIntegrationClass extends BaseIntegrationClass {

    @Autowired
    UserDAO userDAO;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_PASSWORD = "TestPassword123!";
    private static final String TEST_FIRST_NAME = "Test";
    private static final String TEST_LAST_NAME = "User";
    private static final String REGISTRATION_TYPE = "SYSTEM";

    @BeforeEach
    void setup() {
        userDAO.deleteAll();

        UserEntity user = new UserEntity();
        user.setUsername(TEST_USERNAME);
        user.setSysPassword(passwordEncoder.encode(TEST_PASSWORD));
        user.setSysEmail(TEST_EMAIL);
        user.setFirstName(TEST_FIRST_NAME);
        user.setLastName(TEST_LAST_NAME);
        user.setRegistrationType(REGISTRATION_TYPE);
        user.setIsAdmin(false);
        user.setIsPremium(false);
        userDAO.save(user);
    }

    @Test
    void login_ShouldReturn200_WhenCredentialsAreValid() throws Exception {
        // Arrange
        SystemLoginModel request = new SystemLoginModel();
        request.setEmail(TEST_EMAIL);
        request.setPassword(TEST_PASSWORD);

        // Act & Assert
        MvcResult result = mockMvc.perform(post("/auth/login/system")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(TEST_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(TEST_LAST_NAME))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.username").value(TEST_USERNAME))
                .andExpect(jsonPath("$.isAdmin").value(false))
                .andExpect(jsonPath("$.isPremium").value(false))
                .andExpect(cookie().exists("spo-sar"))
                .andReturn();

        assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    void login_ShouldReturn401_WhenPasswordIsInvalid() throws Exception {
        SystemLoginModel request = new SystemLoginModel();
        request.setEmail(TEST_EMAIL);
        request.setPassword("12345678");

        MvcResult result = mockMvc.perform(post("/auth/login/system")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    void login_ShouldReturn401_WhenUserDoesNotExist() throws Exception {
        // Arrange
        SystemLoginModel request = new SystemLoginModel();
        request.setEmail("nonexistent@test.com");
        request.setPassword(TEST_PASSWORD);

        // Act & Assert
        mockMvc.perform(post("/auth/login/system")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void login_ShouldReturn400_WhenEmailIsNull() throws Exception {
        // Arrange
        SystemLoginModel request = new SystemLoginModel();
        request.setEmail(null);
        request.setPassword(TEST_PASSWORD);

        // Act & Assert
        mockMvc.perform(post("/auth/login/system")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_ShouldReturn400_WhenPasswordIsNull() throws Exception {
        // Arrange
        SystemLoginModel request = new SystemLoginModel();
        request.setEmail(TEST_EMAIL);
        request.setPassword(null);

        // Act & Assert
        mockMvc.perform(post("/auth/login/system")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_ShouldReturn400_WhenRequestBodyIsEmpty() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/auth/login/system")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}