package com.spotlightsarajevo.modules.event.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Default, all property, table model for the Event Entity")
public class EventModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the event")
    private Integer id;

    @Schema(description = "Slug used in URLs for the event")
    private String slug;

    @Schema(description = "Official name of the event in Bosnian")
    private String officialNameBs;

    @Schema(description = "Official name of the event in English")
    private String officialNameEn;

    @Schema(description = "Short description of the event in Bosnian")
    private String smallDescriptionBs;

    @Schema(description = "Short description of the event in English")
    private String smallDescriptionEn;

    @Schema(description = "Full description of the event in Bosnian")
    private String fullDescriptionBs;

    @Schema(description = "Full description of the event in English")
    private String fullDescriptionEn;

    @Schema(description = "Identifier of the event category")
    private Integer categoryId;

    @Schema(description = "Start date and time of the event")
    private LocalDateTime startDate;

    @Schema(description = "End date and time of the event")
    private LocalDateTime endDate;

    @Schema(description = "Entry price for the event")
    private BigDecimal entryPrice;

    @Schema(description = "Minimum age limit for the event")
    private BigDecimal ageLimit;

    @Schema(description = "Indicates if reservation is required for the event")
    private Boolean reservation;

    @Schema(description = "Cancellation and refund policy of the event")
    private String cancelRefund;

    @Schema(description = "Language in which the event will be conducted")
    private String eventLanguage;

    @Schema(description = "Latitude coordinate of the event location")
    private BigDecimal eventLat;

    @Schema(description = "Longitude coordinate of the event location")
    private BigDecimal eventLong;

    @Schema(description = "Physical location of the event")
    private String location;

    @Schema(description = "Identifier of the event organiser")
    private Integer organiserId;
}
