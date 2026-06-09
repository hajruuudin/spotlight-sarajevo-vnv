package com.spotlightsarajevo.modules.spot.api.dto;

import com.spotlightsarajevo.modules.media.api.dto.MediaStoreModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Schema(description = "Properties for a spot map marker model")
public class SpotMapModel implements Serializable {
    private static final long versionUUId = 1L;

    @Schema(description = "Unique identifier of the Spot")
    private Integer id;

    @Schema(description = "Unique slug identifier of the spot")
    private String slug;

    @Schema(description = "Official name of the spot in Bosnian")
    private String officialNameBs;

    @Schema(description = "Official name of the spot in English")
    private String officialNameEn;

    @Schema(description = "Latitude of the Spot location")
    private BigDecimal latitude;

    @Schema(description = "Longitude of the Spot location")
    private BigDecimal longitude;

    @Schema(description = "Category name for the spot in bosnian")
    private String categoryNameBs;

    @Schema(description = "Category name for the spot in english")
    private String categoryNameEn;

    @Schema(description = "The thumbnail image of the spot")
    private MediaStoreModel thumbnailImage;
}

