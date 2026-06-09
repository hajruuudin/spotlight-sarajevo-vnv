package com.spotlightsarajevo.modules.event.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Schema(description = "Aggregated review statistics for an Event Organiser (read-only, from database view)")
public class EventOrganiserReviewStatsModel implements Serializable {
    @Schema(description = "Identifier of the event organiser")
    private Integer organiserId;

    @Schema(description = "Combined overall rating of the organiser")
    private BigDecimal combinedRating;

    @Schema(description = "Combined enjoyability rating of the organiser's events")
    private BigDecimal combinedEnjoyability;

    @Schema(description = "Combined quality rating of the organiser's events")
    private BigDecimal combinedQuality;

    @Schema(description = "Combined atmosphere rating of the organiser's events")
    private BigDecimal combinedAtmosphere;
}


