package com.spotlightsarajevo.modules.auth.service;

import com.spotlightsarajevo.modules.auth.api.dto.UserAttendedEventsModel;
import com.spotlightsarajevo.modules.auth.api.dto.UserInfoModel;
import com.spotlightsarajevo.modules.auth.api.dto.UserVisitedSpotsModel;
import com.spotlightsarajevo.modules.auth.domain.UserAttendedEventsDAO;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

/**
 * Service interface for user-related operations. Does not include authentication methods.
 */
public interface UserService {
    /**
     * Retrieves detailed information about the logged-in user.
     *
     * @param principal the security principal representing the logged-in user
     * @return ResponseEntity containing UserInfoModel with user details
     */
    ResponseEntity<UserInfoModel> findUserInfo(Principal principal);

    /**
     * Adds a visited spot for the logged-in user.
     *
     * @param principal the security principal representing the logged-in user
     * @param spotId    the ID of the spot to be added as visited
     * @return ResponseEntity containing UserVisitedSpotsModel with updated visited spots
     */
    ResponseEntity<UserVisitedSpotsModel> addVisitedSpot(Principal principal, Integer spotId);

    /**
     * Adds an attended event for the logged-in user.
     *
     * @param principal the security principal representing the logged-in user
     * @param eventId   the ID of the event to be added as attended
     * @return ResponseEntity containing UserAttendedEventsModel with updated attended events
     */
    ResponseEntity<UserAttendedEventsModel> addAttendedEvent(Principal principal, Integer eventId);

    /**
     * Removes a visited spot for the logged-in user.
     *
     * @param principal the security principal representing the logged-in user
     * @param spotId    the ID of the spot to be removed from visited
     * @return ResponseEntity containing Boolean indicating success or failure
     */
    ResponseEntity<Boolean> removeVisitedSpot(Principal principal, Integer spotId);

    /**
     * Removes an attended event for the logged-in user.
     *
     * @param principal the security principal representing the logged-in user
     * @param eventId   the ID of the event to be removed from attended
     * @return ResponseEntity containing Boolean indicating success or failure
     */
    ResponseEntity<Boolean> removeAttendedEvent(Principal principal, Integer eventId);
}
