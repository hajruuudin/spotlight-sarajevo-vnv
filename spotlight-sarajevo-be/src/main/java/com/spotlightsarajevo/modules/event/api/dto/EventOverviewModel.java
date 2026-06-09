package com.spotlightsarajevo.modules.event.api.dto;

import com.spotlightsarajevo.modules.media.api.dto.MediaStoreModel;
import com.spotlightsarajevo.modules.tag.api.dto.TagModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Properties of a whole event overview model")
public class EventOverviewModel implements Serializable {

    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the event")
    private Integer id;

    @Schema(description = "Unique slug identifier of the event")
    private String slug;

    @Schema(description = "Official name of the event in Bosnian")
    private String officialNameBs;

    @Schema(description = "Official name of the event in English")
    private String officialNameEn;

    @Schema(description = "Small description/headline of the event in Bosnian")
    private String smallDescriptionBs;

    @Schema(description = "Small description/headline of the event in English")
    private String smallDescriptionEn;

    @Schema(description = "Full description of the event in Bosnian")
    private String fullDescriptionBs;

    @Schema(description = "Full description of the event in English")
    private String fullDescriptionEn;

    @Schema(description = "Location name of the event")
    private String location;

    @Schema(description = "Slug link of the location (points to a spot or venue)")
    private String locationLinkSlug;

    @Schema(description = "Category ID of the event")
    private Integer categoryId;

    @Schema(description = "Category name of the event in English")
    private String categoryNameEn;

    @Schema(description = "Category name of the event in Bosnian")
    private String categoryNameBs;

    @Schema(description = "Tags related to the event")
    private List<TagModel> eventTags;

    @Schema(description = "Latitude of the event location")
    private BigDecimal eventLat;

    @Schema(description = "Longitude of the event location")
    private BigDecimal eventLon;

    @Schema(description = "Start date and time of the event")
    private LocalDateTime startDate;

    @Schema(description = "End date and time of the event")
    private LocalDateTime endDate;

    @Schema(description = "Entry price of the event (formatted), or 'Free'")
    private String entryPrice;

    @Schema(description = "Cancellation and refund policy")
    private Boolean cancelRefund;

    @Schema(description = "Language of the event")
    private String eventLanguage;

    @Schema(description = "Age limit for the event (0 if none)")
    private Integer ageLimit;

    @Schema(description = "Whether the event requires a reservation")
    private Boolean reservation;

    @Schema(description = "Organisers of the event")
    private EventOrganiserModel organiser;

    @Schema(description = "Thumbnail image of the event")
    private MediaStoreModel thumbnailImage;

    @Schema(description = "List of additional event images")
    private List<MediaStoreModel> images;
}
