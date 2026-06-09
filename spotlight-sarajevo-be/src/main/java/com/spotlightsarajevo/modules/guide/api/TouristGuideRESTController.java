package com.spotlightsarajevo.modules.guide.api;

import com.spotlightsarajevo.modules.guide.api.dto.*;
import com.spotlightsarajevo.modules.guide.service.TouristGuideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Tag(name = "guide", description = "Tourist Guide API")
@AllArgsConstructor
@RequestMapping(value = "/guide")
public class TouristGuideRESTController {

    TouristGuideService guideService;

    @GetMapping(value = "/all")
    @Operation(description = "Get all tourist guides from the system")
    public ResponseEntity<List<TouristGuideShorthandModel>> findAllGuides(){
        return guideService.findAllGuides();
    }

    @GetMapping()
    @Operation(description = "Get all tourist guides from the system via pagination and sorting options")
    public ResponseEntity<Page<TouristGuideShorthandModel>> findGuidesPaginated(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String sortOption
    ){
        return guideService.findGuidesPaginated(PageRequest.of(pageNumber, pageSize), searchTerm, sortOption);
    }

    @GetMapping(value = "/category/{categoryId}")
    @Operation(description = "Get all tourist guides for a specific category id")
    ResponseEntity<List<TouristGuideShorthandModel>> findByCategory(
            @PathVariable Integer categoryId
    ){
        return guideService.findByCategory(categoryId);
    }

    @GetMapping(value = "/{slug}")
    @Operation(description = "Get a tourist guide by its Slug")
    public ResponseEntity<TouristGuideOverviewModel> findGuideOverview(
            @PathVariable String slug)
    {
        return guideService.findGuideOverview(slug);
    }

    @PostMapping(value = "/create")
    @Operation(description = "Create a new tourist guide inside the database")
    public ResponseEntity<TouristGuideOverviewModel> create(
            @RequestBody TouristGuideCreateModel request,
            Principal principal
    ){
        return guideService.create(request, principal);
    }

    @PutMapping
    @Operation(description = "Update an existing Tourist guide, including its individual sections (either creating new ones or modifying eisting ones)")
    public ResponseEntity<TouristGuideModel> update(
            @RequestBody TouristGuideUpdateModel request,
            Principal principal
    ){
        return guideService.update(request, principal);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(description = "Delete a tourist guide from the database by its ID")
    public ResponseEntity<TouristGuideModel> delete(
            @PathVariable Integer id
    ){
        return guideService.delete(id);
    }
}