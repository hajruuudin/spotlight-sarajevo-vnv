package com.spotlightsarajevo.modules.auth.service;

import com.spotlightsarajevo.modules.auth.api.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import com.spotlightsarajevo.common.exceptions.AuthExceptions.SystemRegisterException;
import com.spotlightsarajevo.common.exceptions.AuthExceptions.GoogleRegisterException;
import com.spotlightsarajevo.common.exceptions.AuthExceptions.SystemLoginException;
import com.spotlightsarajevo.common.exceptions.AuthExceptions.GoogleLoginException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

/**
 * Service Definition Class representing available methods regarding authentication, authorisation and two-factor authentication.
 * @version 1.0.0
 * @author hajrudin.imamovic
 */
public interface AuthService {
    /**
     * Service method to store google credentials within a session. This method stores the users main credentials
     * (being email, first and last name, created username and google id, all fields required by the {@link GoogleUserModel}).
     * The data is stored in a temporary session on the backend, when it is later pulled in the register methods. The method first
     * verifies if the token is valid, after which is attempts to retrieve the Google Data from Googles servers. If any of the data
     * is not present, the mentioned exception will be thrown. Upon success, the data should be stored within a newly made session for the user.
     *
     * @param payload The payload only consists of a {@link String} labeled <b>idToken</b> which is used to identify the users Google account.
     *                This is received from the frontend in a String format.
     * @param session The HTTP session of the user used to add the data temporarily to the backend.
     *
     * @throws GoogleRegisterException in case the idToken is null, is not present, is invalid or any other potential mishap with Googles GSI api.
     * @see com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
     */
    ResponseEntity<Map<String, Object>> storeGoogleCredentials(Map<String, String> payload, HttpSession session) throws GeneralSecurityException, IOException;

    /**
     * Service method to store system credentials within a session. This method stores the users main credentials
     * (being email, first and last name, generated username and hashed password, all field required by the {@link SystemUserModel}).
     * The data is stored in a temporary session when it is later pulled in the register methods for system users. The method
     * checks if the data is present (if not, throws the exception). Afterward, it hashes the password and stores the user credentials into the session.
     *
     * @param payload The data sent from the form on the frontend, essentially a {@link SystemUserModel}
     * @param session The HTTP session of the user to add the data temporarily to the backend.
     *
     * @throws SystemRegisterException in case anything is missing, data does not get added or unexpected errors occur within the method.
     * @see org.springframework.security.crypto.bcrypt.BCrypt
     */
    ResponseEntity<Map<String, Object>> storeSystemCredentials(SystemUserModel payload, HttpSession session);

    /**
     * Service method to register the user to the database via Google authentication. Upon completion, the users account
     * should be stored in the database, along with the user preferences data. It firstly gathers the data from the
     * Google model stored in the session from the initial storeSystemCredentials() method. Afterward, it registers
     * the user to the main database table. Upon success, the preferences are stored one by one into their respective
     * tables, including creating default collection lists for every user.
     *
     * @param googleData The Google data taken from Googles servers and used to register the account.
     * @param preferencesModel The model containing information about the users favourite categories and survey question answers
     *
     * @throws GoogleRegisterException in case anything is missing, data does not get added or unexpected errors occur within the method.
     *
     * @see com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
     */
    ResponseEntity<LoggedUserModel> registerByGoogle(GoogleUserModel googleData, PreferencesModel preferencesModel, HttpServletResponse response);

    /**
     * Service method to register the user to the database via System authentication. Upon completion, the users account
     * should be stored in the database, along with the user preferences data. It firstly gathers the data from the
     * System model stored in the session from the initial storeSystemCredentials() method. Afterward, it registers
     * the user to the main database table. Upon success, the preferences are stored one by one into their respective
     * tables, including creating default collection lists for every user.
     *
     * @param systemData The System data taken entered on the frontend application, including the users credentials.
     * @param preferencesModel The model containing information about the users favourite categories and survey question answers
     *
     * @throws SystemLoginException in case anything is missing, data does not get added or unexpected errors occur within the method.
     *
     */
    ResponseEntity<LoggedUserModel> registerBySystem(SystemUserModel systemData, PreferencesModel preferencesModel, HttpServletResponse response);

    /**
     * Logs a user in using Google Authentication. It takes the ID token sent from the frontend and firstly verifies
     * it against Googles API. If valid, the method attempts to retrieve the user from the database using {@link UserDetailsService}
     * where the user credentials are loaded. Upon success, an HTTP-only JWT token is generated and set to the session.
     * This makes the user considered "logged" in.
     *
     * @param payload The string of the Google ID token
     * @param servletResponse The servlet of the users frontend client, which the cookie will be attached to.
     *
     * @throws GoogleLoginException if anything goes wrong during method execution.
     */
    ResponseEntity<LoggedUserModel> loginGoogle(Map<String, String> payload, HttpServletResponse servletResponse) throws GeneralSecurityException, IOException;

    /**
     * Logs a user in using System Authentication. It takes the {@link SystemLoginModel} sent from the frontend and firstly verifies
     * it. If valid, the method attempts to retrieve the user from the database using {@link UserDetailsService}
     * where the user credentials are loaded. Upon success, an HTTP-only JWT token is generated and set to the session.
     * This makes the user considered "logged" in.
     *
     * @param request The model containing the email and password entered by the user
     * @param servletResponse The servlet of the users frontend client, which the cookie will be attached to.
     *
     * @throws SystemLoginException if anything goes wrong during method execution.
     */
    ResponseEntity<LoggedUserModel> loginSystem(SystemLoginModel request, HttpServletResponse servletResponse);

    /**
     * Logs a user out of the application. It essentially removes any cookies from the servlet response and
     * this is automatically considered as a logout function.
     *
     * @param response The HTTP servlet send along with any request from the user.
     */
    ResponseEntity<Map<String, Object>> logout(HttpServletResponse response);

    /**
     * Checks to see if a session is present for the user that made the request. It extracts
     * the cookie (if present) from the {@link HttpServletResponse}. In case the cookie is present,
     * simply return the logged users data to the frontend. In case it is not, it is treated as a
     * protected route and returns 401.
     *
     * @param request The request object from the frontend
     * @param response The response object from the frontend.
     */
    ResponseEntity<LoggedUserModel> me(HttpServletRequest request, HttpServletResponse response);
}
