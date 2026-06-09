package com.spotlightsarajevo.modules.spot.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Schema(description = "Aggregated review statistics for a Spot (read-only, from database view)")
public class SpotReviewStatsModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Identifier of the spot")
    private Integer spotId;

    @Schema(description = "Combined overall rating of the spot")
    private BigDecimal combinedRating;

    @Schema(description = "Combined cleanliness rating of the spot")
    private BigDecimal combinedCleanliness;

    @Schema(description = "Combined affordability rating of the spot")
    private BigDecimal combinedAffordability;

    @Schema(description = "Combined accessibility rating of the spot")
    private BigDecimal combinedAccessibility;

    @Schema(description = "Combined staff kindness rating of the spot")
    private BigDecimal combinedStaffKindness;

    @Schema(description = "Combined locale quality rating of the spot")
    private BigDecimal combinedQuality;

    @Schema(description = "Combined atmosphere rating of the spot")
    private BigDecimal combinedAtmosphere;
}

