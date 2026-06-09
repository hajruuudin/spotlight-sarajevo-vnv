package com.spotlightsarajevo.modules.auth.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.spotlightsarajevo.common.enums.ObjectType;
import com.spotlightsarajevo.common.enums.ProtectedKeywords;
import com.spotlightsarajevo.common.exceptions.AuthExceptions;
import com.spotlightsarajevo.common.utils.CommonConstants;
import com.spotlightsarajevo.modules.collection.domain.CollectionDAO;
import com.spotlightsarajevo.modules.collection.domain.entity.CollectionEntity;
import com.spotlightsarajevo.modules.collection.domain.entity.CollectionEventEntity;
import com.spotlightsarajevo.modules.collection.domain.entity.CollectionSpotEntity;
import com.spotlightsarajevo.modules.auth.api.dto.GoogleUserModel;
import com.spotlightsarajevo.modules.auth.api.dto.PreferencesModel;
import com.spotlightsarajevo.modules.auth.api.dto.SystemUserModel;
import com.spotlightsarajevo.modules.auth.domain.UserDAO;
import com.spotlightsarajevo.modules.auth.domain.UserFavouriteEventsDAO;
import com.spotlightsarajevo.modules.auth.domain.UserFavouriteSpotsDAO;
import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
import com.spotlightsarajevo.modules.auth.domain.entity.UserFavouriteEventsEntity;
import com.spotlightsarajevo.modules.auth.domain.entity.UserFavouriteSpotsEntity;
import com.spotlightsarajevo.modules.auth.service.AuthServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

/**
 * Utility class for user authentication and registration processes.
 * Contains methods for parsing JWT tokens, verifying Google ID tokens,
 * setting user entity data during registration, and managing user favourite categories and collections.
 *
 * Used in {@link AuthServiceImpl}
 */
@Component
@AllArgsConstructor
public class AuthUserUtilities {
    private static final GsonFactory jsonFactory = new GsonFactory();
    private static final NetHttpTransport transport = new NetHttpTransport();
    UserFavouriteSpotsDAO userFavouriteSpotsDAO;
    UserFavouriteEventsDAO userFavouriteEventsDAO;
    CollectionDAO collectionDAO;
    UserDAO userDAO;

    public String parseJwt(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> "spo-sar".equals(cookie.getName()))
                    .findFirst();
            if (jwtCookie.isPresent()) {
                return jwtCookie.get().getValue();
            }
        }
        return null;
    }

    /**
     * Method to verify google id token received from the frontend upon Google authentication.<br/>
     * Works with both login and registration. Used in {@link AuthServiceImpl}
     *
     * @param idTokenString The string of the token received from the frontend.
     */
    public GoogleIdToken verifyGoogleToken(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(System.getenv("GOOGLE_CLIENT_ID")))
                .build();

        return verifier.verify(idTokenString);
    }

    /**
     * Method to set the {@link UserEntity} after the registration process. This method sets the basic user data
     * and sets the question answers from the survey. The method sets the appropriate user type ({@link GoogleUserModel} or {@link SystemUserModel}),
     * depending on which model is sent to the method as full and which is null (the null one is ignored).
     *
     * @param userEntity The created user entity object ready to be modified.
     * @param googleUserModel The model of a Google registration (can be null in case of a System register)
     * @param systemUserModel The model of a System registration (can be null in case of a Google registration)
     * @param preferencesModel The model containing the user spot and event categories, along with the survey question answers
     */
    public void setUserEntityIdentity(UserEntity userEntity, GoogleUserModel googleUserModel, SystemUserModel systemUserModel, PreferencesModel preferencesModel){
        if (googleUserModel == null && systemUserModel == null) {
            throw new AuthExceptions.UserRegisterException("No user data provided for registration");
        }
        if (preferencesModel == null) {
            throw new AuthExceptions.UserRegisterException("Preferences data cannot be null");
        }

        if (systemUserModel == null) { // REGISTER A GOOGLE USER
            userEntity.setLocale(googleUserModel.getLocale());
            userEntity.setFirstName(googleUserModel.getFirstName());
            userEntity.setLastName(googleUserModel.getLastName());
            userEntity.setIsPremium(false);
            userEntity.setIsAdmin(false);
            userEntity.setCreatedAt(LocalDateTime.now());
            userEntity.setRegistrationType("GOOGLE");
            userEntity.setGoogleId(googleUserModel.getGoogleId());
            userEntity.setGoogleEmail(googleUserModel.getEmail());
            userEntity.setUsername(googleUserModel.getUsername());
        } else if (googleUserModel == null) { // REGISTER A SYSTEM USER
            userEntity.setFirstName(systemUserModel.getFirstName());
            userEntity.setLastName(systemUserModel.getLastName());
            userEntity.setIsPremium(false);
            userEntity.setIsAdmin(false);
            userEntity.setCreatedAt(LocalDateTime.now());
            userEntity.setRegistrationType("SYSTEM");
            userEntity.setSysEmail(systemUserModel.getEmail());
            userEntity.setSysPassword(systemUserModel.getPassword());
            userEntity.setUsername(systemUserModel.getUsername());
        }

        // SETTING THE SURVEY ANSWERS FROM THE MODEL
        userEntity.setQuestionOne(preferencesModel.getQuestionOne());
        userEntity.setQuestionTwo(preferencesModel.getQuestionTwo());
        userEntity.setQuestionThree(preferencesModel.getQuestionThree());
        userEntity.setQuestionFour(preferencesModel.getQuestionFour());
    }

    /**
     * Method to set the users favourite spot and event categories taken from the preferences model.
     * Used in {@link AuthServiceImpl} during Google and System registration
     *
     * @param userEntity The user entity linked from the database
     * @param preferencesModel The object preferences model containing the list of category numbers
     */
    public void setUserFavouriteObjects(UserEntity userEntity, PreferencesModel preferencesModel){
        if(userEntity == null){
            throw new AuthExceptions.UserRegisterException("User Entity cannot be null");
        }

        if(preferencesModel == null){
            throw new AuthExceptions.UserRegisterException("Preferences cannot be empty");
        }

        for(int spotId : preferencesModel.getFavouriteSpotCategories()){
            UserFavouriteSpotsEntity userFavouriteSpotsEntity = new UserFavouriteSpotsEntity();
            userFavouriteSpotsEntity.setUserId(userEntity.getId());
            userFavouriteSpotsEntity.setSpotId(spotId);
            userFavouriteSpotsDAO.save(userFavouriteSpotsEntity);
        }

        for(int eventId : preferencesModel.getFavouriteEventCategories()){
            UserFavouriteEventsEntity userFavouriteEventsEntity = new UserFavouriteEventsEntity();
            userFavouriteEventsEntity.setUserId(userEntity.getId());
            userFavouriteEventsEntity.setEventId(eventId);
            userFavouriteEventsDAO.save(userFavouriteEventsEntity);
        }
    }

    /**
     * Method to create two default user collections, one for {@link CollectionSpotEntity} and
     * one for {@link CollectionEventEntity}. Used in {@link AuthServiceImpl} during Google and System registration
     *
     * @param userEntity The user who the collections are linked to
     * @apiNote The name of the collections are by default "All Spots" and "All Events" and <b> cannot be changed</b>
     * within the application UI.
     */
    public void setUserDefaultCollections(UserEntity userEntity){
        if(userEntity == null){
            throw new AuthExceptions.UserRegisterException("User Entity cannot be null");
        }

        CollectionEntity collectionSpotEntity = new CollectionEntity();
        collectionSpotEntity.setCollectionName(ProtectedKeywords.ALL_SPOTS.getValue());
        collectionSpotEntity.setCollectionType(String.valueOf(ObjectType.SPOT));
        collectionSpotEntity.setIsSystem(true);
        collectionSpotEntity.setUserId(userEntity.getId());
        collectionSpotEntity.setCreated(LocalDateTime.now());
        collectionSpotEntity.setCreatedBy(ProtectedKeywords.SYSTEM.getValue());
        collectionDAO.save(collectionSpotEntity);

        CollectionEntity collectionEventEntity = new CollectionEntity();
        collectionEventEntity.setCollectionName(ProtectedKeywords.ALL_EVENTS.getValue());
        collectionEventEntity.setCollectionType(String.valueOf(ObjectType.EVENT));
        collectionEventEntity.setIsSystem(true);
        collectionEventEntity.setUserId(userEntity.getId());
        collectionEventEntity.setCreated(LocalDateTime.now());
        collectionEventEntity.setCreatedBy(ProtectedKeywords.SYSTEM.getValue());
        collectionDAO.save(collectionEventEntity);
    }

    /**
     * Method to create a new JWT cookie upon logging in. Used in {@link AuthServiceImpl} during
     * logging in as Google or System user.
     *
     * @param response The HTTP servlet used to which the JWT cookie is attached
     * @param token The generated JWT token
     */
    public static void setJwtCookie(HttpServletResponse response, String token) {
        Cookie jwtCookie = new Cookie(CommonConstants.JWT_COOKIE_NAME, token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(CommonConstants.JWT_COOKIE_MAX_AGE_SECONDS);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
    }

    /**
     * Common method to check if the user that is accessing the API is the same user that made the request to
     * the database from the frontend UI. Uses Springs principal object to validate the identity of the user.
     *
     * @param principal The principal of the request including authorisation data
     *
     * @return A boolean value representing if the request is authorised or not
     */
    public boolean isAuthorisedUser(Integer expectedUserId, Principal principal){
        Optional<UserEntity> userEntity = userDAO.findBySysEmailOrGoogleEmail(principal.getName());
        return expectedUserId.equals(userEntity.get().getId());
    }
}
