package com.spotlightsarajevo.modules.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Properties for sending the favourite categories and survey questions from the authentication on the frontend")
public class PreferencesModel {
    private static final Long versionUUId = 1L;

    @Schema(description = "List of IDs of the users favourite spot categories")
    private List<Integer> favouriteSpotCategories;

    @Schema(description = "List of IDs of the users favourite event categories")
    private List<Integer> favouriteEventCategories;

    @Schema(description = "Answer to the first question of the register survey")
    private Boolean questionOne;

    @Schema(description = "Answer to the second question of the register survey")
    private Boolean questionTwo;

    @Schema(description = "Answer to the third question of the register survey")
    private Boolean questionThree;

    @Schema(description = "Answer to the fourth question of the register survey")
    private Boolean questionFour;
}
