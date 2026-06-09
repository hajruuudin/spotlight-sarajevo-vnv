package com.spotlightsarajevo.modules.community.api;

import com.spotlightsarajevo.common.enums.FilterOptions;
import com.spotlightsarajevo.modules.community.api.dto.*;
import com.spotlightsarajevo.modules.community.service.CommunityRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Tag(name = "Community Request API", description = "API for managing community requests")
@AllArgsConstructor
@RequestMapping(value = "/community-request")
public class CommunityRequestRESTController {
    CommunityRequestService communityRequestService;

    @GetMapping(value = "/get-user-requests")
    @Operation(description = "This API endpoint retrieves all the community requests made by the authenticated user from the database view")
    public ResponseEntity<List<UserCommunityRequestModel>> getUserCommunityRequests(
            Principal principal
    ) {
        return communityRequestService.getUserCommunityRequests(principal);
    }

    @PostMapping(value = "/create-request")
    @Operation(description = "This API endpoint allows an authenticated user to create a new community request")
    public ResponseEntity<CommunityRequestModel> createCommunityRequest(
            @RequestBody CommunityRequestCreateModel request,
            Principal principal
    ){
        return communityRequestService.createCommunityRequest(request, principal);
    }

    /* ADMIN ROUTES */
    @GetMapping(value = "/admin/get-all-requests")
    @Operation(description = "This API endpoint retrieves all community requests from the database view for administrative purposes, filtered by the specified filter option")
    public ResponseEntity<List<CommunityRequestModel>> getAllCommunityRequests(
            @RequestParam(defaultValue = "ALL") FilterOptions filterOption,
            Principal principal
    ){
        return communityRequestService.getAllCommunityRequests(filterOption, principal);
    }

    @GetMapping(value = "/admin/get-request/{requestId}")
    @Operation(description = "This API endpoint retrieves an overview of a specific community request by its ID for administrative purposes, including pending information and request description")
    public ResponseEntity<CommunityRequestOverviewModel> getCommunityRequestOverview(
            @PathVariable Integer requestId,
            Principal principal
    ) {
        return communityRequestService.getCommunityRequestOverview(requestId, principal);
    }

    @PutMapping(value = "/admin/update-request-status")
    @Operation(description = "This API endpoint updates the status of the specified community request")
    public ResponseEntity<CommunityRequestModel> updateCommunityRequestStatus(
            @RequestBody CommunityRequestStatusUpdate request,
            Principal principal
    ) {
        System.out.println(request);
        return communityRequestService.updateCommunityRequestStatus(request, principal);
    }

    @DeleteMapping(value = "/admin/delete-request/{requestId}")
    @Operation(description = "This API endpoint deletes the specified community request from the database")
    public ResponseEntity<CommunityRequestModel> deleteCommunityRequest(
            @PathVariable Integer requestId,
            Principal principal
    ) {
        return communityRequestService.deleteCommunityRequest(requestId, principal);
    }

    @GetMapping(value = "/admin/get-recently-added")
    @Operation(description = "Fetch recently added community requests for admin dashboard")
    public ResponseEntity<List<CommunityRequestModel>> getRecentlyAddedCommunityRequests(
            @RequestParam(defaultValue = "10", required = false) Integer limit,
            Principal principal
    ) {
        return communityRequestService.getRecentlyAddedCommunityRequests(limit, principal);
    }
}
