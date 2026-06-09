package com.spotlightsarajevo.modules.review.api.dto;

import com.google.api.client.util.DateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Schema(description = "Properties of an event organiser review response")
public class EventOrganiserReviewUpdateModel implements Serializable {
    @Schema(description = "Unique identifier of the record in the database")
    private Integer id;

    @Schema(description = "Identifier of the user who created the record")
    private Integer userId;

    @Schema(description = "Identifier of the organiser associated with the record")
    private Integer organiserId;

    @Schema(description = "A short header or title for the record")
    private String header;

    @Schema(description = "The main content or body of the record")
    private String body;

    @Schema(description = "The organiser rating the user provided")
    private BigDecimal userOverallRating;

    @Schema(description = "Quality rating given by the user for the organiser")
    private BigDecimal userOrganiserQuality;

    @Schema(description = "Atmosphere rating given by the user for the organiser")
    private BigDecimal userOrganiserAtmosphere;

    @Schema(description = "Enjoyability rating given by the user for the organiser")
    private BigDecimal userOrganiserEnjoyability;
}
