package com.spotlightsarajevo.modules.event.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Schema(description = "Default, all property, table model for the Event Review Entity")
public class EventReviewModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the event review")
    private Integer id;

    @Schema(description = "Identifier of the user who submitted the review")
    private Integer userId;

    @Schema(description = "Header or title of the review")
    private String header;

    @Schema(description = "Full text of the review")
    private String body;

    @Schema(description = "Rating of the event quality given by the user")
    private BigDecimal userEventQuality;

    @Schema(description = "Rating of the event enjoyability given by the user")
    private BigDecimal userEventEnjoyability;

    @Schema(description = "Rating of the event atmosphere given by the user")
    private BigDecimal userEventAtmosphere;

    @Schema(description = "Identifier of the event being reviewed")
    private Integer eventId;
}
