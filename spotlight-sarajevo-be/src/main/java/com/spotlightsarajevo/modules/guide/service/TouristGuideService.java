package com.spotlightsarajevo.modules.guide.service;

import com.spotlightsarajevo.modules.guide.api.dto.*;
import org.apache.catalina.connector.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

/**
 * TouristGuideService interface defines methods for managing tourist guides.
 * It includes methods to find all guides, find guides by category,
 */
public interface TouristGuideService {
    /**
     * Retrieves a list of all tourist guides in shorthand format.
     *
     * @return ResponseEntity containing a list of TouristGuideShorthandModel
     */
    ResponseEntity<List<TouristGuideShorthandModel>> findAllGuides();

    /**
     * Retrieves a list of paginated tourist guides in shorthand format
     *
     * @return A list of paginated response, sorted and based on the search term
     */
    ResponseEntity<Page<TouristGuideShorthandModel>> findGuidesPaginated(PageRequest request, String searchTerm, String sortOption);

    /**
     * Retrieves a list of tourist guides by category ID in shorthand format.
     *
     * @param categoryId the unique identifier of the category
     * @return ResponseEntity containing a list of TouristGuideShorthandModel
     */
    ResponseEntity<List<TouristGuideShorthandModel>> findByCategory(Integer categoryId);

    /**
     * Retrieves an overview of a specific tourist guide by its slug.
     *
     * @param slug the unique slug identifier of the tourist guide
     * @return ResponseEntity containing the TouristGuideOverviewModel
     */
    ResponseEntity<TouristGuideOverviewModel> findGuideOverview(String slug);

    /**
     * Creates a new tourist guide based on the provided TouristGuideCreateModel.
     *
     * @param request the model containing information to create a new tourist guide
     * @param principal the security principal representing the current user
     * @return ResponseEntity containing the created TouristGuideOverviewModel
     */
    ResponseEntity<TouristGuideOverviewModel> create(TouristGuideCreateModel request, Principal principal);

    /**
     * Updates an existing tourist guide, alongside its individual sections.
     *
     * @param request The TouristGuideUpdateModel sent from the FE
     * @param principal The principal object that is sent alongside the authenticated user data
     * @return ResponseEntity containing the newly updated TouristGuideModel (The simple one with the updated status)
     */
    ResponseEntity<TouristGuideModel> update(TouristGuideUpdateModel request, Principal principal);

    /**
     * Deletes a tourist guide by its ID.
     *
     * @param id the unique identifier of the tourist guide to be deleted
     * @return ResponseEntity containing the deleted TouristGuideModel
     */
    ResponseEntity<TouristGuideModel> delete(Integer id);
}
