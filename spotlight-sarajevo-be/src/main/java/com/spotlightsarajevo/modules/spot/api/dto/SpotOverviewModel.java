package com.spotlightsarajevo.modules.spot.api.dto;

import com.spotlightsarajevo.modules.media.api.dto.MediaStoreModel;
import com.spotlightsarajevo.modules.review.api.dto.SpotReviewModel;
import com.spotlightsarajevo.modules.tag.api.dto.TagModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "Properties of a whole spot overview model")
public class SpotOverviewModel implements Serializable {
    private static final Long versionUUId = 1L;

    @Schema(description = "Unique identifier of the Spot")
    private Integer id;

    @Schema(description = "Unique slug identifier of the spot")
    private String slug;

    @Schema(description = "Official name of the spot in Bosnian")
    private String officialNameBs;

    @Schema(description = "Official name of the spot in English")
    private String officialNameEn;

    @Schema(description = "Small description/headline of the spot in Bosnian")
    private String smallDescriptionBs;

    @Schema(description = "Small description/headline of the spot in English")
    private String smallDescriptionEn;

    @Schema(description = "Full description/headline of the spot in Bosnian")
    private String fullDescriptionBs;

    @Schema(description = "Full description/headline of the spot in English")
    private String fullDescriptionEn;

    @Schema(description = "Legal address of the Spot location")
    private String address;

    @Schema(description = "Category ID of the spot overview")
    private Integer categoryId;

    @Schema(description = "Category name for the spot in English")
    private String categoryNameEn;

    @Schema(description = "Category name for the spot in Bosnian")
    private String categoryNameBs;

    @Schema(description = "Tags related to the spot")
    private List<TagModel> spotTags;

    @Schema(description = "Latitude of the Spot location")
    private BigDecimal latitude;

    @Schema(description = "Longitude of the Spot location")
    private BigDecimal longitude;

    @Schema(description = "The initial spot overall rating given by the admin who added the spot")
    private BigDecimal combinedRating;

    @Schema(description = "The initial spot cleanliness given by the admin who added the spot")
    private BigDecimal combinedCleanliness;

    @Schema(description = "The initial spot affordability given by the admin who added the spot")
    private BigDecimal combinedAffordability;

    @Schema(description = "The initial spot accessibility given by the admin who added the spot")
    private BigDecimal combinedAccessibility;

    @Schema(description = "The initial spot staff kindness by the admin who added the spot")
    private BigDecimal combinedStaffKindness;

    @Schema(description = "The initial spot locale quality given by the admin who added the spot")
    private BigDecimal combinedLocaleQuality;

    @Schema(description = "The initial spot atmosphere given by the admin who added the spot")
    private BigDecimal combinedAtmosphere;

    @Schema(description = "The reviews related to the spot")
    private List<SpotReviewModel> reviews;

    @Schema(description = "The work hours of th spot, if specified (null if it is a landmark spot)")
    private List<SpotWorkHoursModel> workHours;

    @Schema(description = "Thumbnail image of the spot")
    private MediaStoreModel thumbnailImage;

    @Schema(description = "List of all other images of the spot")
    private List<MediaStoreModel> images;
}
