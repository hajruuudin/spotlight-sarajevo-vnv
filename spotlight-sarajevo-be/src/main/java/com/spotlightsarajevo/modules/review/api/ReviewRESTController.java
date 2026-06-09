package com.spotlightsarajevo.modules.review.api;

import com.spotlightsarajevo.modules.review.api.dto.*;
import com.spotlightsarajevo.modules.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Tag(name = "review", description = "Review API Routes")
@AllArgsConstructor
@RequestMapping(value = "/review")
public class ReviewRESTController {
    private final ReviewService reviewService;

    /* ============================================================= */
    /* ==================== SPOT REVIEWS CRUD ====================== */
    /* ============================================================= */

    @GetMapping(value = "/spot/find-spot-reviews")
    @Operation(description = "Get a page of spot reviews for a spot")
    public ResponseEntity<Page<SpotReviewModel>> findSpotReviews(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(required = true) Integer spotId,
            @RequestParam(defaultValue = "ALPHABETICAL", required = false) String sortOption
    ){
        return reviewService.findReviewsForSpot(PageRequest.of(pageNumber, pageSize), spotId, sortOption);
    }

    @GetMapping(value = "/spot/find-user-review")
    @Operation(description = "Get the users review of a specific spot")
    public ResponseEntity<SpotReviewModel> findUserReview(
            @RequestParam(required = true) Integer spotId,
            Principal principal
    ){
        return reviewService.findUserReview(spotId, principal);
    }

    @PostMapping(value = "/spot/add-review")
    @Operation(description = "Add a spot review to the database")
    public ResponseEntity<SpotReviewModel> addSpotReview(
            @RequestBody SpotReviewCreateModel createRequest, Principal principal
    ){
        return reviewService.createSpotReview(createRequest, principal);
    }

    @PutMapping(value = "/spot/update-review")
    @Operation(description = "Update a spot review from the database")
    public ResponseEntity<SpotReviewModel> updateSpotReview(
            @RequestBody SpotReviewUpdateModel updateRequest, Principal principal
    ){
        return reviewService.updateSpotReview(updateRequest, principal);
    }

    @DeleteMapping(value = "/spot/remove-review")
    @Operation(description = "Remove a spot review from the database")
    public ResponseEntity<SpotReviewModel> deleteSpotReview(
            @RequestParam(required = true) Integer spotId,
            @RequestParam(required = true) Integer reviewId,
            Principal principal
    ){
        return reviewService.deleteSpotReview(spotId, reviewId, principal);
    }

    /* ======================================================================== */
    /* ==================== EVENT ORGANISER REVIEWS CRUD ====================== */
    /* ======================================================================== */

    @GetMapping(value = "/organiser/find-organiser-reviews")
    @Operation(description = "Find all organiser reviews with pagination included")
    public ResponseEntity<Page<EventOrganiserReviewModel>> findOrganiserReviews(
            @RequestParam(required = true, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = true, defaultValue = "10") Integer pageSize,
            @RequestParam(required = true) Integer organiserId,
            @RequestParam(required = false, defaultValue = "ALPHABETICAL") String sortOption
    ) {
        return reviewService.findReviewsForOrganiser(PageRequest.of(pageNumber, pageSize), organiserId, sortOption);
    }

    @GetMapping(value = "/organiser/find-user-review")
    @Operation(description = "Find a users review for the organiser")
    public ResponseEntity<EventOrganiserReviewModel> findUserOrganiserReview(
            @RequestParam(required = true) Integer organiserId,
            Principal principal
    ){
        return reviewService.findUserOrganiserReview(organiserId, principal);
    }

    @PostMapping(value = "/organiser/add-review")
    @Operation(description = "Add a review to an event organiser")
    public ResponseEntity<EventOrganiserReviewModel> addOrganiserReview(
            @RequestBody EventOrganiserReviewCreateModel request, Principal principal
    ){
        System.out.println(request);
        return reviewService.createOrganiserReview(request, principal);
    }

    @PutMapping(value = "/organiser/update-review")
    @Operation(description = "Update an existing organiser review in the database")
    public ResponseEntity<EventOrganiserReviewModel> updateOrganiserReview(
            @RequestBody EventOrganiserReviewUpdateModel request, Principal principal
    ){
        return reviewService.updateOrganiserReview(request, principal);
    }

    @DeleteMapping(value = "/organiser/remove-review")
    @Operation(description = "Remove an existing organiser review in the database")
    public ResponseEntity<EventOrganiserReviewModel> deleteOrganiserReview(
            @RequestParam(required = true) Integer organiserId,
            @RequestParam(required = true) Integer reviewId,
            Principal principal
    ){
        return reviewService.deleteOrganiserReview(organiserId, reviewId, principal);
    }
}
