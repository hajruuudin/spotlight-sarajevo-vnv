package com.spotlightsarajevo.spotlight_sarajevo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotlightsarajevo.modules.auth.domain.UserDAO;
import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public abstract class BaseIntegrationClass {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected UserDAO userDAO;

    protected String userToken;
    protected String adminToken;

    protected static final String TEST_USER_FIRSTNAME = "hajrudin";
    protected static final String TEST_USER_LASTNAME = "imamovic";
    protected static final String TEST_USER_USERNAME = "hajruuudin";
    protected static final String TEST_USER_EMAIL = "testuser@test.com";

    protected static final String TEST_ADMIN_FIRSTNAME = "admin";
    protected static final String TEST_ADMIN_LASTNAME = "adminovic";
    protected static final String TEST_ADMIN_USERNAME = "adminuser";
    protected static final String TEST_ADMIN_EMAIL = "testadmin@test.com";

    @BeforeEach
    void setupUsers() {
        // Create regular user
        UserEntity user = new UserEntity();
        user.setFirstName(TEST_USER_FIRSTNAME);
        user.setLastName(TEST_USER_LASTNAME);
        user.setUsername(TEST_USER_USERNAME);
        user.setSysEmail(TEST_USER_EMAIL);
        user.setSysPassword(passwordEncoder.encode("password"));
        user.setIsAdmin(false);
        user.setRegistrationType("SYSTEM");
        user.setQuestionOne(true);
        user.setQuestionTwo(true);
        user.setQuestionThree(true);
        user.setQuestionFour(true);

        userDAO.save(user);

        // Create admin user
        UserEntity admin = new UserEntity();
        admin.setFirstName(TEST_ADMIN_FIRSTNAME);
        admin.setLastName(TEST_ADMIN_LASTNAME);
        admin.setUsername(TEST_ADMIN_USERNAME);
        admin.setSysEmail(TEST_ADMIN_EMAIL);
        admin.setSysPassword(passwordEncoder.encode("password"));
        admin.setIsAdmin(true);
        admin.setRegistrationType("SYSTEM");
        admin.setQuestionOne(true);
        admin.setQuestionTwo(true);
        admin.setQuestionThree(true);
        admin.setQuestionFour(true);

        userDAO.save(admin);

        // Generate tokens
        userToken = TestJwtUtil.generateToken(TEST_USER_EMAIL);
        adminToken = TestJwtUtil.generateToken(TEST_ADMIN_EMAIL);
    }
}
