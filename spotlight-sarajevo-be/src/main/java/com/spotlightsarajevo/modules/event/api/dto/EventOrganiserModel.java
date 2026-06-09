package com.spotlightsarajevo.modules.event.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Default, all property, table model for the Event Organiser Entity")
public class EventOrganiserModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the event organiser")
    private Integer id;

    @Schema(description = "Name of the organiser")
    private String organiserName;

    @Schema(description = "Date when the organiser entity was created")
    private LocalDateTime organiserCreationDate;

    @Schema(description = "Category ID of the event organiser (identical to the Event Category)")
    private Integer organiserCategoryId;

    @Schema(description = "Category name of the organiser in English")
    private String organiserCategoryNameEn;

    @Schema(description = "Category name of the organiser in Bosnian")
    private String organiserCategoryNameBs;

    @Schema(description = "Phone number of the organiser")
    private String organiserPhone;

    @Schema(description = "Email address of the organiser")
    private String organiserEmail;

    @Schema(description = "Website URL of the organiser")
    private String organiserWebsite;

    @Schema(description = "Thumbnail image of the organiser (logo or a default image)")
    private String thumbnailImage;

    @Schema(description = "Overall aggregate rating of the organiser form the Users")
    private BigDecimal overallRating;

    @Schema(description = "Overall aggregate atmosphere rating of the organiser form the Users")
    private BigDecimal overallAtmosphere;

    @Schema(description = "Overall aggregate enjoyability rating of the organiser form the Users")
    private BigDecimal overallEnjoyability;

    @Schema(description = "Overall aggregate quality rating of the organiser form the Users")
    private BigDecimal overallQuality;
}
