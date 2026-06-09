package com.spotlightsarajevo.common.utils;

import com.spotlightsarajevo.common.exceptions.AuthExceptions;
import com.spotlightsarajevo.modules.auth.domain.UserDAO;
import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CommonFunctions {
    UserDAO userDAO;

    public UserEntity getUserFromPrincipal(Principal principal){
        Optional<UserEntity> user = userDAO.findBySysEmailOrGoogleEmail(principal.getName());

        if(user.isPresent()){
            return user.get();
        } else {
            throw new AuthExceptions.AuthenticationUserNotFoundException("Principal User Not Found");
        }
    }

    public String generateSlug(String title) {
        if (title == null || title.isEmpty()) {
            return "";
        }

        return title
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }
}
