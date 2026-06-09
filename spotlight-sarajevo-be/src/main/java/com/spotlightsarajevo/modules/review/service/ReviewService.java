package com.spotlightsarajevo.modules.review.service;

import com.spotlightsarajevo.modules.review.api.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

/**
 * ReviewService interface defines methods for managing reviews related to spots and event organisers.
 * It includes methods to create, read, update, and delete reviews, as well as retrieve reviews for specific spots or organisers.
 */
public interface ReviewService {
    /* ============================================================= */
    /* ==================== SPOT REVIEWS CRUD ====================== */
    /* ============================================================= */

    /**
     * Retrieves a paginated list of reviews for a specific spot.
     *
     * @param pageRequest the pagination information
     * @param spotId the unique identifier of the spot
     * @param sortOption the option to sort the reviews
     * @return ResponseEntity containing a Page of SpotReviewModel
     */
    ResponseEntity<Page<SpotReviewModel>> findReviewsForSpot(PageRequest pageRequest, Integer spotId, String sortOption);

    /**
     * Retrieves the review submitted by the current user for a specific spot.
     *
     * @param spotId the unique identifier of the spot
     * @param principal the security principal representing the current user
     * @return ResponseEntity containing the SpotReviewModel
     */
    ResponseEntity<SpotReviewModel> findUserReview(Integer spotId, Principal principal);

    /**
     * Creates a new review for a specific spot.
     *
     * @param createRequest the model containing information to create a new spot review
     * @param principal the security principal representing the current user
     * @return ResponseEntity containing the created SpotReviewModel
     */
    ResponseEntity<SpotReviewModel> createSpotReview(SpotReviewCreateModel createRequest, Principal principal);

    /**
     * Updates an existing review for a specific spot.
     *
     * @param updateRequest the model containing updated information for the spot review
     * @param principal the security principal representing the current user
     * @return ResponseEntity containing the updated SpotReviewModel
     */
    ResponseEntity<SpotReviewModel> updateSpotReview(SpotReviewUpdateModel updateRequest, Principal principal);

    /**
     * Deletes a review for a specific spot.
     *
     * @param spotId the unique identifier of the spot
     * @param reviewId the unique identifier of the review to be deleted
     * @param principal the security principal representing the current user
     * @return ResponseEntity containing the deleted SpotReviewModel
     */
    ResponseEntity<SpotReviewModel> deleteSpotReview(Integer spotId, Integer reviewId, Principal principal);

    /* ======================================================================== */
    /* ==================== EVENT ORGANISER REVIEWS CRUD ====================== */
    /* ======================================================================== */

    /**
     * Retrieves a paginated list of reviews for a specific event organiser.
     *
     * @param pageRequest the pagination information
     * @param organiserId the unique identifier of the event organiser
     * @param sortOption the option to sort the reviews
     * @return ResponseEntity containing a Page of EventOrganiserReviewModel
     */
    ResponseEntity<Page<EventOrganiserReviewModel>> findReviewsForOrganiser(PageRequest pageRequest, Integer organiserId, String sortOption);

    /**
     * Retrieves the review submitted by the current user for a specific event organiser.
     *
     * @param organiserId the unique identifier of the event organiser
     * @param principal the security principal representing the current user
     * @return ResponseEntity containing the EventOrganiserReviewModel
     */
    ResponseEntity<EventOrganiserReviewModel> findUserOrganiserReview(Integer organiserId, Principal principal);

    /**
     * Creates a new review for a specific event organiser.
     *
     * @param request the model containing information to create a new event organiser review
     * @param principal the security principal representing the current user
     * @return ResponseEntity containing the created EventOrganiserReviewModel
     */
    ResponseEntity<EventOrganiserReviewModel> createOrganiserReview(EventOrganiserReviewCreateModel request, Principal principal);

    /**
     * Updates an existing review for a specific event organiser.
     *
     * @param request the model containing updated information for the event organiser review
     * @param principal the security principal representing the current user
     * @return ResponseEntity containing the updated EventOrganiserReviewModel
     */
    ResponseEntity<EventOrganiserReviewModel> updateOrganiserReview(EventOrganiserReviewUpdateModel request, Principal principal );

    /**
     * Deletes a review for a specific event organiser.
     *
     * @param organiserId the unique identifier of the event organiser
     * @param reviewId the unique identifier of the review to be deleted
     * @param principal the security principal representing the current user
     * @return ResponseEntity containing the deleted EventOrganiserReviewModel
     */
    ResponseEntity<EventOrganiserReviewModel> deleteOrganiserReview(Integer organiserId, Integer reviewId, Principal principal);
}
