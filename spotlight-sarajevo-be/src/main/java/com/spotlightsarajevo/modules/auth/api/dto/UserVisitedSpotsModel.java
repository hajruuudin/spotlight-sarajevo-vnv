package com.spotlightsarajevo.modules.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Default, all property, table model for the User Visited Spots Entity")
public class UserVisitedSpotsModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the record")
    private Integer id;

    @Schema(description = "ID of the user who visited the spot")
    private Integer userId;

    @Schema(description = "ID of the visited spot")
    private Integer spotId;
}
