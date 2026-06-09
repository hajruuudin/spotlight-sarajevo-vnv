package com.spotlightsarajevo.modules.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.spotlightsarajevo.modules.auth.domain.UserDAO;
import com.spotlightsarajevo.modules.auth.api.dto.*;
import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
import com.spotlightsarajevo.modules.auth.mapper.UserMapper;
import com.spotlightsarajevo.common.exceptions.AuthExceptions;
import com.spotlightsarajevo.common.utils.CommonConstants;
import com.spotlightsarajevo.config.security.JwtUtil;
import com.spotlightsarajevo.modules.auth.utils.AuthUserUtilities;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    UserDAO userDAO;
    UserMapper userMapper;
    UserDetailsService userDetailsService;
    JwtUtil jwtUtil;
    AuthUserUtilities authUserUtilities;

    @Override
    public ResponseEntity<Map<String, Object>> storeGoogleCredentials(Map<String, String> payload, HttpSession session) throws GeneralSecurityException, IOException {
        String idTokenString = payload.get("idToken");

        if(idTokenString == null){
            throw new AuthExceptions.GoogleRegisterException("Id token not found");
        } else {
            Random rand = new Random();

            GoogleIdToken finalIdToken = authUserUtilities.verifyGoogleToken(idTokenString);

            if(finalIdToken != null){
                GoogleIdToken.Payload tokenPayload = finalIdToken.getPayload();

                // Extract user information
                String googleId = tokenPayload.getSubject();
                String email = tokenPayload.getEmail();
                String local = (String) tokenPayload.get("locale");
                String firstName = (String) tokenPayload.get("given_name");
                String lastName = (String) tokenPayload.get("family_name");

                // In case there is a Google account already registered to the same Google ID:
                Optional<UserEntity> checkUserEntity = userDAO.findByGoogleId(googleId);
                if(checkUserEntity.isPresent()){
                    return ResponseEntity.status(400).body(Map.of("message", "A google account is already registered to that email. Please log in!"));
                }

                // In case there is another account that uses the same email but registered with system credentials:
                Optional<UserEntity> checkUserEmail = userDAO.findBySysEmail(email);
                if(checkUserEmail.isPresent()){
                    return ResponseEntity.status(400).body(Map.of("message", "An account is already registered to that email. Please log in!"));
                }

                // Store this information in the session temporarily
                GoogleUserModel googleUserInfo = new GoogleUserModel();
                googleUserInfo.setGoogleId(googleId);
                googleUserInfo.setEmail(email);
                googleUserInfo.setLocale(local);
                googleUserInfo.setFirstName(firstName);
                googleUserInfo.setLastName(lastName);
                googleUserInfo.setUsername(firstName.toLowerCase(Locale.ROOT) + lastName.toLowerCase(Locale.ROOT) + (rand.nextInt(100, 500)));

                session.setAttribute(CommonConstants.GOOGLE_USER_INFO_SESSION_KEY, googleUserInfo);

                return ResponseEntity.status(200).body(Map.of("message", "Successfully stored Google data to session", "firstName", googleUserInfo.getFirstName()));
            } else {
                throw new AuthExceptions.GoogleRegisterException("Google could not verify Your ID token!");
            }
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> storeSystemCredentials(SystemUserModel payload, HttpSession session) {
        if(payload == null){
            throw new AuthExceptions.SystemRegisterException("System credentials cannot be empty!");
        } else {
            Random rand = new Random();
            // In case there is an account already registered on the system email:
            Optional<UserEntity> checkSystemEmail = userDAO.findBySysEmail(payload.getEmail());
            if(checkSystemEmail.isPresent()){
                return ResponseEntity.status(400).body(Map.of("message", "An account is already registered to that email. Please log in!"));
            }

            // In case there is an account already registered on the Google email:
            Optional<UserEntity> checkGoogleEmail = userDAO.findBySysEmail(payload.getEmail());
            if(checkGoogleEmail.isPresent()){
                return ResponseEntity.status(400).body(Map.of("message", "A google account is already registered to that email. Please log in!"));
            }

            String hashedPassword = BCrypt.hashpw(payload.getPassword(), BCrypt.gensalt());
            payload.setPassword(hashedPassword);
            payload.setUsername(payload.getFirstName() + payload.getLastName() + (rand.nextInt(100, 500)));

            session.setAttribute(CommonConstants.SYSTEM_USER_INFO_SESSION_KEY, payload);

            return ResponseEntity.status(200).body(Map.of("message", "Successfully stored System data to session", "firstName", payload.getFirstName()));
        }
    }

    @Override
    @Transactional
    public ResponseEntity<LoggedUserModel> registerByGoogle(GoogleUserModel googleData, PreferencesModel preferencesModel, HttpServletResponse response) {
        try {
            // Generate a new, blank, user entity
            UserEntity userEntity = new UserEntity();

            // Set the entities authentication details and main data (Google register)
            authUserUtilities.setUserEntityIdentity(userEntity, googleData, null, preferencesModel);

            // Save it to the database and return the entity
            UserEntity savedUserEntity = userDAO.save(userEntity);

            // Using the ID of the new entity, save the users preferences
            authUserUtilities.setUserFavouriteObjects(savedUserEntity, preferencesModel);

            // Creating two system collections for Spots and for Events
            authUserUtilities.setUserDefaultCollections(savedUserEntity);

            // Immediately log the user in
            UserDetails userDetails = userDetailsService.loadUserByUsername(googleData.getEmail());
            String jwt = jwtUtil.generateToken(userDetails);
            AuthUserUtilities.setJwtCookie(response, jwt);

            // Return the saved element
            return ResponseEntity.status(200).body(userMapper.entityToGoogleLoggedDto(savedUserEntity));
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthExceptions.GoogleRegisterException("Unexpected error during Google-register process");
        }
    }

    @Override
    public ResponseEntity<LoggedUserModel> registerBySystem(SystemUserModel systemData, PreferencesModel preferencesModel, HttpServletResponse response) {
        try {
            // Generate a new, blank, user entity
            UserEntity userEntity = new UserEntity();

            // Set the entities authentication details and main data (Google register)
            authUserUtilities.setUserEntityIdentity(userEntity, null, systemData, preferencesModel);

            // Save it to the database and return the entity
            UserEntity savedUserEntity = userDAO.save(userEntity);

            // Using the ID of the new entity, save the users preferences
            authUserUtilities.setUserFavouriteObjects(savedUserEntity, preferencesModel);

            // Creating two system collections for Spots and for Events
            authUserUtilities.setUserDefaultCollections(savedUserEntity);

            // Immediately log the user in to the system
            UserDetails userDetails = userDetailsService.loadUserByUsername(systemData.getEmail());
            String jwt = jwtUtil.generateToken(userDetails);
            AuthUserUtilities.setJwtCookie(response, jwt);

            // Return the saved element
            return ResponseEntity.status(200).body(userMapper.entityToGoogleSystemDto(savedUserEntity));
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthExceptions.SystemRegisterException("Unexpected error during System-register process");
        }
    }

    @Override
    public ResponseEntity<LoggedUserModel> loginGoogle(Map<String, String> payload, HttpServletResponse servletResponse) throws GeneralSecurityException, IOException {
        String idTokenString = payload.get("idToken");

        if (idTokenString == null) {
            throw new AuthExceptions.GoogleRegisterException("Id token not found");
        } else {
            GoogleIdToken token = authUserUtilities.verifyGoogleToken(idTokenString);

            if (token != null) {
                GoogleIdToken.Payload tokenPayload = token.getPayload();
                String googleId = tokenPayload.getSubject();
                String email = tokenPayload.getEmail();

                Optional<UserEntity> checkEmailEntity = userDAO.findBySysEmail(email);

                if(checkEmailEntity.isPresent()){
                    throw new AuthExceptions.GoogleLoginException("This email is registered with system credentials. Please log in with that instead.");
                }

                Optional<UserEntity> entity = userDAO.findByGoogleId(googleId);

                if (entity.isPresent()) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    String jwt = jwtUtil.generateToken(userDetails);
                    AuthUserUtilities.setJwtCookie(servletResponse, jwt);
                    return ResponseEntity.status(200).body(userMapper.entityToGoogleLoggedDto(entity.get()));
                } else {
                    throw new AuthExceptions.GoogleLoginException("No account is registered to this email. Please register.");
                }
            } else {
                throw new AuthExceptions.GoogleLoginException("There was an error while logging in...");
            }
        }
    }

    @Override
    public ResponseEntity<LoggedUserModel> loginSystem(SystemLoginModel request, HttpServletResponse servletResponse) {
        if(request == null){
            throw new AuthExceptions.SystemRegisterException("Credentials cannot be null");
        }
        Optional<UserEntity> entity = userDAO.findBySysEmail(request.getEmail());

        if(entity.isPresent()){
            String storedPassword = entity.get().getSysPassword();
            String providedPassword = request.getPassword();

            if (BCrypt.checkpw(providedPassword, storedPassword)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
                String jwt = jwtUtil.generateToken(userDetails);
                AuthUserUtilities.setJwtCookie(servletResponse, jwt);

                return ResponseEntity.status(200).body(userMapper.entityToGoogleSystemDto(entity.get()));
            } else {
                throw new AuthExceptions.SystemRegisterException("Invalid credentials");
            }
        } else {
            throw new AuthExceptions.SystemRegisterException("Invalid credentials");
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> logout(HttpServletResponse response) {
        Cookie deleteCookie = new Cookie(CommonConstants.JWT_COOKIE_NAME, null);
        deleteCookie.setPath("/");
        deleteCookie.setHttpOnly(true);
        deleteCookie.setMaxAge(0);

        String appEnv = System.getenv("APP_ENVIRONMENT");
        StringBuilder cookieHeader = new StringBuilder(deleteCookie.getName() + "=; Path=" + deleteCookie.getPath() + "; HttpOnly; Max-Age=0");

        if ("production".equals(appEnv)) {
            cookieHeader.append("; Secure");
            cookieHeader.append("; SameSite=None");
        }

        response.setHeader("Set-Cookie", cookieHeader.toString());

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    public ResponseEntity<LoggedUserModel> me(HttpServletRequest request, HttpServletResponse response){
        try {
            String jwt = authUserUtilities.parseJwt(request);

            if (jwt != null && jwtUtil.validateToken(jwt)) {
                String email = jwtUtil.extractUsername(jwt);

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
                Optional<UserEntity> entity = userDAO.findBySysEmailOrGoogleEmail(userDetails.getUsername());

                if(entity.isPresent()){
                    return ResponseEntity.ok(
                            new LoggedUserModel(
                                entity.get().getId(),
                                entity.get().getFirstName(),
                                entity.get().getLastName(),
                                entity.get().getSysEmail(),
                                entity.get().getUsername(),
                                entity.get().getIsAdmin(),
                                entity.get().getIsPremium()
                            )
                    );
                }
            } else {
                return ResponseEntity.status(401).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }

        return ResponseEntity.status(401).body(null);
    };
}
