package com.spotlightsarajevo.modules.community.service;

import com.spotlightsarajevo.common.enums.FilterOptions;
import com.spotlightsarajevo.modules.community.api.dto.*;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

/**
 * This interface defines the contract for the CommunityRequestService, which is responsible for handling all business logic related to
 * community requests. It includes methods for retrieving user-specific community requests, creating new requests, retrieving all requests for administrative purposes,
 * getting details of a specific request, updating request status, and deleting requests.
 */
public interface CommunityRequestService {
    /* USER ROUTES */
    /**
     * Retrieves all community requests made by the authenticated user from the database view.
     *
     * @param principal The security principal representing the authenticated user.
     * @return A ResponseEntity containing a list of UserCommunityRequestModel objects representing the user's community requests.
     */
    ResponseEntity<List<UserCommunityRequestModel>> getUserCommunityRequests(Principal principal);

    /**
     * Allows an authenticated user to create a new community request.
     *
     * @param request   The CommunityRequestCreateModel object containing the details of the community request to be created.
     * @param principal The security principal representing the authenticated user.
     * @return A ResponseEntity containing the created CommunityRequestModel object representing the newly created community request.
     */
    ResponseEntity<CommunityRequestModel> createCommunityRequest(CommunityRequestCreateModel request, Principal principal);

    /* ADMIN ROUTES */
    /**
     * Retrieves all community requests from the database view for administrative purposes, filtered by the specified option.
     *
     * @param filterOption The FilterOptions enum value to filter requests by status (ALL, PENDING, REJECTED, APPROVED).
     * @param principal The security principal representing the authenticated user (admin).
     * @return A ResponseEntity containing a list of CommunityRequestModel objects representing the filtered community requests.
     */
    ResponseEntity<List<CommunityRequestModel>> getAllCommunityRequests(FilterOptions filterOption, Principal principal);

    /**
     * Retrieves the details of a specific community request by its ID for administrative purposes.
     *
     * @param requestId The unique identifier of the community request to be retrieved.
     * @param principal The security principal representing the authenticated user (admin).
     * @return A ResponseEntity containing a CommunityRequestDataModel object representing the details of the specified community request.
     */
    ResponseEntity<CommunityRequestDataModel> getCommunityRequestById(Integer requestId, Principal principal);

    /**
     * Retrieves an overview of a specific community request by its ID for administrative purposes.
     * The overview includes the request details along with pending information and request description.
     *
     * @param requestId The unique identifier of the community request to be retrieved.
     * @param principal The security principal representing the authenticated user (admin).
     * @return A ResponseEntity containing a CommunityRequestOverviewModel object representing the overview of the specified community request.
     */
    ResponseEntity<CommunityRequestOverviewModel> getCommunityRequestOverview(Integer requestId, Principal principal);

    /**
     * Updates the status of the specified community request.
     *
     * @param request   The CommunityRequestStatusUpdate object containing the request ID and the new status to be updated.
     * @param principal The security principal representing the authenticated user (admin).
     * @return A ResponseEntity containing the updated CommunityRequestModel object representing the community request with its new status.
     */
    ResponseEntity<CommunityRequestModel> updateCommunityRequestStatus(CommunityRequestStatusUpdate request, Principal principal);

    /**
     * Deletes the specified community request.
     *
     * @param requestId The unique identifier of the community request to be deleted.
     * @param principal The security principal representing the authenticated user (admin).
     * @return A ResponseEntity containing a CommunityRequestModel object representing the deleted community request, or an appropriate response indicating the result of the deletion operation.
     */
    ResponseEntity<CommunityRequestModel> deleteCommunityRequest(Integer requestId, Principal principal);

    /**
     * Fetch recently added community requests for admin dashboard.
     * In case the request is successfully processed, the backend will return an array of CommunityRequestModel objects.
     *
     * @param limit The maximum number of recent requests to retrieve
     * @param principal The security principal representing the authenticated user (admin)
     * @return A ResponseEntity containing an array of recently added CommunityRequestModel objects
     */
    ResponseEntity<List<CommunityRequestModel>> getRecentlyAddedCommunityRequests(Integer limit, Principal principal);
}
