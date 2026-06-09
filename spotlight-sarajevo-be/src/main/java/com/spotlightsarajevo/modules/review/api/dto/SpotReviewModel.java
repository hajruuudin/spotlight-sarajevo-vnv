package com.spotlightsarajevo.modules.review.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Default, all property, table model for the Spot Review Entity")
public class SpotReviewModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the spot review")
    private Integer id;

    @Schema(description = "Identifier of the user who submitted the review")
    private Integer userId;

    @Schema(description = "Username of the user who submitted the review")
    private String username;

    @Schema(description = "Header or title of the review")
    private String header;

    @Schema(description = "Full text of the review")
    private String body;

    @Schema(description = "Overall rating given by the user")
    private Integer userOverallRating;

    @Schema(description = "Rating of cleanliness given by the user")
    private BigDecimal userCleanliness;

    @Schema(description = "Rating of affordability given by the user")
    private BigDecimal userAffordability;

    @Schema(description = "Rating of accessibility given by the user")
    private BigDecimal userAccessibility;

    @Schema(description = "Rating of staff kindness given by the user")
    private BigDecimal userStaffKindness;

    @Schema(description = "Rating of locale quality given by the user")
    private BigDecimal userLocaleQuality;

    @Schema(description = "Rating of atmosphere given by the user")
    private BigDecimal userAtmosphere;

    @Schema(description = "Identifier of the spot being reviewed")
    private Integer spotId;

    @Schema(description = "Date the spot review was create")
    private LocalDate created;

    @Schema(description = "Date the spot review was last edited")
    private LocalDate modified;
}
