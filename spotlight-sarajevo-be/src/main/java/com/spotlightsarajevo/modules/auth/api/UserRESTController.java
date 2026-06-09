package com.spotlightsarajevo.modules.auth.api;

import com.spotlightsarajevo.modules.auth.api.dto.UserAttendedEventsModel;
import com.spotlightsarajevo.modules.auth.api.dto.UserInfoModel;
import com.spotlightsarajevo.modules.auth.api.dto.UserVisitedSpotsModel;
import com.spotlightsarajevo.modules.auth.domain.entity.UserAttendedEventCreate;
import com.spotlightsarajevo.modules.auth.domain.entity.UserVisitedSpotCreate;
import com.spotlightsarajevo.modules.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Tag(name = "user", description = "User API Endpoints")
@AllArgsConstructor
@RequestMapping(value = "/user")
public class UserRESTController {
    UserService userService;

    @Operation(description = "Get detailed information about the logged in user")
    @GetMapping(value = "/info")
    public ResponseEntity<UserInfoModel> findUserInfo(Principal principal){
        return userService.findUserInfo(principal);
    }

    @Operation(description = "Add a visited spot for the logged in user")
    @PostMapping(value = "/visited-spot/add")
    public ResponseEntity<UserVisitedSpotsModel> addVisitedSpot(
            Principal principal,
            @RequestBody UserVisitedSpotCreate request) {
        return userService.addVisitedSpot(principal, request.getSpotId());
    }

    @Operation(description = "Add an attended event for the logged in user")
    @PostMapping(value = "/attended-event/add")
    public ResponseEntity<UserAttendedEventsModel> addAttendedEvent(
            Principal principal,
            @RequestBody UserAttendedEventCreate request) {
        return userService.addAttendedEvent(principal, request.getEventId());
    }

    @Operation(description = "Remove a visited spot for the logged in user")
    @DeleteMapping(value = "/visited-spot/remove")
    public ResponseEntity<Boolean> removeVisitedSpot(
            Principal principal,
            @RequestParam Integer spotId) {
        return userService.removeVisitedSpot(principal, spotId);
    }

    @Operation(description = "Remove an attended event for the logged in user")
    @DeleteMapping(value = "/attended-event/remove")
    public ResponseEntity<Boolean> removeAttendedEvent(
            Principal principal,
            @RequestParam Integer eventId) {
        return userService.removeAttendedEvent(principal, eventId);
    }
}
