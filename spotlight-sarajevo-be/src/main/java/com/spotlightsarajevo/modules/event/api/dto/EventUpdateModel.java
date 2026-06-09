package com.spotlightsarajevo.modules.event.api.dto;

import com.spotlightsarajevo.modules.media.api.dto.MediaStoreCreateModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Default, all property, entity update model for the Event Entity")
public class EventUpdateModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the Event object")
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

    @Schema(description = "Tag IDs of the event model")
    private List<Integer> tagIds;

    @Schema(description = "Current thumbnail image URL of the Spot")
    private String thumbnailImage;

    @Schema(description = "New Thumbnail image Object of the Spot")
    private MediaStoreCreateModel newThumbnailImage;

    @Schema(description = "List of new Objects to be added for images of the Spot")
    private List<MediaStoreCreateModel> toAddImages;

    @Schema(description = "List of IDs of the images to be removed")
    private List<Integer> toRemoveImages;
}


