package com.spotlightsarajevo.modules.spot.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Schema(description = "Default, all property, table model for the Spot Entity")
public class SpotModel implements Serializable {
    private static final long versionUUId = 1L;

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

    @Schema(description = "Latitude of the Spot location")
    private BigDecimal latitude;

    @Schema(description = "Longitude of the Spot location")
    private BigDecimal longitude;

    @Schema(description = "Legal address of the Spot location")
    private String address;

    @Schema(description = "Category Identifier for the spot")
    private Integer categoryId;

    @Schema(description = "The initial spot overall rating given by the admin who added the spot")
    private BigDecimal initialOverallRating;

    @Schema(description = "The initial spot cleanliness given by the admin who added the spot")
    private BigDecimal initialCleanliness;

    @Schema(description = "The initial spot affordability given by the admin who added the spot")
    private BigDecimal initialAffordability;

    @Schema(description = "The initial spot accessibility given by the admin who added the spot")
    private BigDecimal initialAccessibility;

    @Schema(description = "The initial spot staff kindness by the admin who added the spot")
    private BigDecimal initialStaffKindness;

    @Schema(description = "The initial spot locale quality given by the admin who added the spot")
    private BigDecimal initialLocaleQuality;

    @Schema(description = "The initial spot atmosphere given by the admin who added the spot")
    private BigDecimal initialAtmosphere;
}
