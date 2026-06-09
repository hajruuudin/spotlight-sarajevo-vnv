package com.spotlightsarajevo.modules.spot.api.dto;

import com.spotlightsarajevo.modules.media.api.dto.MediaStoreCreateModel;
import io.swagger.v3.oas.annotations.media.Schema;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "Default, all property, entity update model for the Spot Entity")
public class SpotUpdateModel implements Serializable {
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

    @Schema(description = "Array of Tag IDs to be added to the spot")
    private List<Integer> tagIds;

    @Schema(description = "An object representing the new Work Hours of the spot")
    private List<SpotWorkHoursModel> workHoursModel;

    @Schema(description = "Current thumbnail image URL of the Spot")
    private String thumbnailImage;

    @Schema(description = "New Thumbnail image Object of the Spot")
    private MediaStoreCreateModel newThumbnailImage;

    @Schema(description = "List of new Objects to be added for images of the Spot")
    private List<MediaStoreCreateModel> toAddImages;

    @Schema(description = "List of IDs of the images to be removed")
    private List<Integer> toRemoveImages;
}

