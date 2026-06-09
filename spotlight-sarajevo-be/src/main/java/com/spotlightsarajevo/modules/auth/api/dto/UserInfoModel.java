package com.spotlightsarajevo.modules.auth.api.dto;

import com.spotlightsarajevo.modules.category.api.dto.EventCategoryModel;
import com.spotlightsarajevo.modules.category.api.dto.SpotCategoryModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "User information model with all properties to be shown on the profile page")
public class UserInfoModel implements Serializable {
    @Schema(description = "Id of the user")
    private Integer id;

    @Schema(description = "First name of the user")
    private String firstName;

    @Schema(description = "Last name of the user")
    private String lastName;

    @Schema(description = "Email of the user (Google or System)")
    private String email;

    @Schema(description = "Username of the user")
    private String username;

    @Schema(description = "Admin status of the user")
    private Boolean isAdmin;

    @Schema(description = "Premium status of the user")
    private Boolean isPremium;

    @Schema(description = "Spot categories the user selected as favourite")
    private List<SpotCategoryModel> favoriteSpotCategories;

    @Schema(description = "Event categories the user selected as favourite")
    private List<EventCategoryModel> favoriteEventCategories;

    @Schema(description = "Number of spots the user has visited")
    private Integer visitedSpotsCount;

    @Schema(description = "Number of events the user has attended")
    private Integer attendedEventsCount;
}
