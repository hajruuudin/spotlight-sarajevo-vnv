package com.spotlightsarajevo.modules.category.api;

import com.spotlightsarajevo.modules.category.api.dto.EventCategoryModel;
import com.spotlightsarajevo.modules.category.api.dto.SpotCategoryModel;
import com.spotlightsarajevo.modules.category.api.dto.TouristGuideCategoryModel;
import com.spotlightsarajevo.modules.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "category", description = "Category API")
@AllArgsConstructor
@RequestMapping(value = "/category")
public class CategoryRESTController {
    CategoryService categoryService;

    @GetMapping(value = "/allSpot")
    @Operation(description = "Get all spot categories from the system")
    public ResponseEntity<List<SpotCategoryModel>> findAllSpot(){
        return this.categoryService.findAllSpotCategories();
    }

    @GetMapping(value = "/allEvent")
    @Operation(description = "Get all event categories from the system")
    public ResponseEntity<List<EventCategoryModel>> findAllEvent(){
        return this.categoryService.findAllEventCategories();
    }


    @GetMapping(value = "/allGuide")
    @Operation(description = "Get all tourist guide categories from the system")
    public ResponseEntity<List<TouristGuideCategoryModel>> findAllGuide(){
        return this.categoryService.findAllGuideCategories();
    }
}
