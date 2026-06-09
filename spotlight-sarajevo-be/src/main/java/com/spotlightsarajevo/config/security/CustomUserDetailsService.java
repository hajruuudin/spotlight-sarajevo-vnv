package com.spotlightsarajevo.config.security;

import com.spotlightsarajevo.modules.auth.domain.UserDAO;
import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Custom implementation of UserDetailsService to load user-specific data.
 * For the authentication process in this application, email and password are used. Due
 * to the convention of the UserDetailsService interface, the email is treated as the username.
 * <p>
 * Depending on the login type, credentials will be generated accordingly. If a user logs in via Google,
 * there will be no password set in the UserDetails object, as authentication is handled by Google. Else, there
 * will be a hashed password set for system-registered users. The email is also given to the user, depending on which Auth
 * method the usr chooses.<b> AN ACCOUNT CANNOT HAVE BOTH EMAIL TYPES FILLED; IT IS EITHER GOOGLE OR SYSTEM.</b>
 * <p>
 * Admin status is also determined here, assigning the appropriate role to the UserDetails object. Admins cannot be
 * registered by themselves to the system; admin credentials are set directly in the database by the project owners
 */
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try{
            Optional<UserEntity> optionalUser = userDAO.findBySysEmailOrGoogleEmail(email);
            String userUsername;
            String userRole;
            String userPassword;

            UserEntity user = new UserEntity();

            if(optionalUser.isPresent()){
                user = optionalUser.get();
            } else {
                throw new RuntimeException("User not found");
            }

            if(user.getRegistrationType().equals("SYSTEM")){
                userUsername = user.getSysEmail();
            } else if (user.getRegistrationType().equals("GOOGLE")){
                userUsername = user.getGoogleEmail();
            } else {
                throw new RuntimeException("Email not identified: SPRING SECURITY");
            }

            if(user.getIsAdmin()){
                userRole = "ADMIN";
            } else {
                userRole = "USER";
            }

            if(user.getRegistrationType().equals("SYSTEM")){
                userPassword = user.getSysPassword();
            } else {
                userPassword = "GOOGLE_AUTH_NO_PASS";
            }

            return User.builder()
                    .username(userUsername)
                    .password(userPassword)
                    .roles(userRole)
                    .build();
        } catch (UsernameNotFoundException e){
            e.printStackTrace();
            throw new RuntimeException("USERNAME NOT FOUND - AUTH ERROR");
        }

    }
}
