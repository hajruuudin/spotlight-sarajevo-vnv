package com.spotlightsarajevo.modules.auth.service;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.exceptions.AuthExceptions;
import com.spotlightsarajevo.modules.auth.api.dto.UserAttendedEventsModel;
import com.spotlightsarajevo.modules.auth.api.dto.UserInfoModel;
import com.spotlightsarajevo.modules.auth.api.dto.UserVisitedSpotsModel;
import com.spotlightsarajevo.modules.auth.domain.*;
import com.spotlightsarajevo.modules.auth.domain.entity.*;
import com.spotlightsarajevo.modules.auth.mapper.UserMapper;
import com.spotlightsarajevo.modules.auth.utils.UserInfoUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    UserDAO userDAO;
    UserAttendedEventsDAO userAttendedEventsDAO;
    UserVisitedSpotsDAO userVisitedSpotsDAO;
    UserFavouriteEventsDAO userFavouriteEventsDAO;
    UserFavouriteSpotsDAO userFavouriteSpotsDAO;
    UserMapper userMapper;
    UserInfoUtils utils;

    @Override
    public ResponseEntity<UserInfoModel> findUserInfo(Principal principal) {
        if(principal == null){
            return ResponseEntity.status(200).body(null);
        }

        Optional<UserEntity> user = userDAO.findBySysEmailOrGoogleEmail(principal.getName());

        if(user.isPresent()){
            List<UserFavouriteSpotsEntity> favouriteSpots = userFavouriteSpotsDAO.findAllByUserId(user.get().getId());
            List<UserFavouriteEventsEntity> favouriteEvents = userFavouriteEventsDAO.findAllByUserId(user.get().getId());

            List<Integer> favouriteSpotCategoryIds = favouriteSpots.stream()
                    .map(UserFavouriteSpotsEntity::getSpotId)
                    .toList();

            List<Integer> favouriteEventCategoryIds = favouriteEvents.stream()
                    .map(UserFavouriteEventsEntity::getEventId)
                    .toList();

            utils.setFavoriteCategories(user.get(), favouriteSpotCategoryIds, favouriteEventCategoryIds);

            user.get().setVisitedSpotsCount(
                    userVisitedSpotsDAO.countAllByUserId(user.get().getId())
            );

            user.get().setAttendedEventsCount(
                    userAttendedEventsDAO.countAllByUserId(user.get().getId())
            );

            if(user.get().getSysEmail() == null){
                return ResponseEntity.status(200).body(
                        userMapper.infoEntityToGoogleDto(user.get())
                );
            } else {
                return ResponseEntity.status(200).body(
                        userMapper.infoEntityToSystemDto(user.get())
                );
            }
        } else {
            throw new AuthExceptions.AuthenticationUserNotFoundException(ExceptionCodes.AuthExceptionCodes.AUTH_USER_NOT_FOUND.toString());
        }
    }

    @Override
    public ResponseEntity<UserVisitedSpotsModel> addVisitedSpot(Principal principal, Integer spotId) {
        if(spotId == null) throw new AuthExceptions.AuthenticationEndpointException(ExceptionCodes.AuthExceptionCodes.AUTH_SPOT_ID_MISSING.toString());
        if(principal == null) throw new AuthExceptions.AuthenticationEndpointException(ExceptionCodes.AuthExceptionCodes.AUTH_UNAUTHORIZED_ACCESS.toString());
        if(!utils.validateSpotExists(spotId)) throw new AuthExceptions.AuthenticationEndpointException(ExceptionCodes.SpotExceptionCodes.SPOT_NOT_FOUND.toString());

        Optional<UserEntity> user = userDAO.findBySysEmailOrGoogleEmail(principal.getName());

        UserVisitedSpotsEntity entity = new UserVisitedSpotsEntity();
        entity.setUserId(user.get().getId());
        entity.setSpotId(spotId);

        UserVisitedSpotsEntity savedEntity = userVisitedSpotsDAO.save(entity);

        return ResponseEntity.status(200).body(userMapper.visitedSpotsEntityToDto(savedEntity));
    }

    @Override
    public ResponseEntity<UserAttendedEventsModel> addAttendedEvent(Principal principal, Integer eventId) {
        if(eventId == null) throw new AuthExceptions.AuthenticationEndpointException(ExceptionCodes.AuthExceptionCodes.AUTH_EVENT_ID_MISSING.toString());
        if(principal == null) throw new AuthExceptions.AuthenticationEndpointException(ExceptionCodes.AuthExceptionCodes.AUTH_UNAUTHORIZED_ACCESS.toString());
        if(!utils.validateEventExists(eventId)) throw new AuthExceptions.AuthenticationEndpointException(ExceptionCodes.EventExceptionCodes.EVENT_NOT_FOUND.toString());

        Optional<UserEntity> user = userDAO.findBySysEmailOrGoogleEmail(principal.getName());

        UserAttendedEventsEntity entity = new UserAttendedEventsEntity();
        entity.setUserId(user.get().getId());
        entity.setEventId(eventId);

        System.out.println("To be added event ID: " + eventId + " for user ID: " + user.get().getId());
        System.out.println("Entity to be saved: " + entity);

        UserAttendedEventsEntity savedEntity = userAttendedEventsDAO.save(entity);

        return ResponseEntity.status(200).body(userMapper.attendedEventsEntityToDto(savedEntity));
    }

    @Override
    @Transactional
    public ResponseEntity<Boolean> removeVisitedSpot(Principal principal, Integer spotId) {
        if (spotId == null) throw new AuthExceptions.AuthenticationEndpointException(ExceptionCodes.AuthExceptionCodes.AUTH_SPOT_ID_MISSING.toString());
        if (principal == null) throw new AuthExceptions.AuthenticationEndpointException(ExceptionCodes.AuthExceptionCodes.AUTH_UNAUTHORIZED_ACCESS.toString());
        if (!utils.validateSpotExists(spotId))
            throw new AuthExceptions.AuthenticationEndpointException(ExceptionCodes.SpotExceptionCodes.SPOT_NOT_FOUND.toString());

        Optional<UserEntity> user = userDAO.findBySysEmailOrGoogleEmail(principal.getName());
        userVisitedSpotsDAO.deleteByUserIdAndSpotId(user.get().getId(), spotId);

        return ResponseEntity.status(200).body(true);
    }

    @Override
    @Transactional
    public ResponseEntity<Boolean> removeAttendedEvent(Principal principal, Integer eventId) {
        if (eventId == null) throw new AuthExceptions.AuthenticationEndpointException(ExceptionCodes.AuthExceptionCodes.AUTH_EVENT_ID_MISSING.toString());
        if (principal == null) throw new AuthExceptions.AuthenticationEndpointException(ExceptionCodes.AuthExceptionCodes.AUTH_UNAUTHORIZED_ACCESS.toString());
        if (!utils.validateEventExists(eventId))
            throw new AuthExceptions.AuthenticationEndpointException(ExceptionCodes.EventExceptionCodes.EVENT_NOT_FOUND.toString());

        Optional<UserEntity> user = userDAO.findBySysEmailOrGoogleEmail(principal.getName());
        userAttendedEventsDAO.deleteByUserIdAndEventId(user.get().getId(), eventId);

        return ResponseEntity.status(200).body(true);
    }
}
