package com.spotlightsarajevo.modules.auth.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Entity representing a user visited spot creation")
public class UserVisitedSpotCreate {
    @Schema(description = "ID of the spot visited by the user")
    private Integer spotId;
}
