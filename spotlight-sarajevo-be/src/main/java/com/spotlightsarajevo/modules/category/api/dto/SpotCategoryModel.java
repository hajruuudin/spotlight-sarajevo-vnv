package com.spotlightsarajevo.modules.category.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Default, all property, table model for the Spot Category Entity")
public class SpotCategoryModel implements Serializable{
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the spot category")
    private Integer id;

    @Schema(description = "Name of the category in Bosnian")
    private String spotCategoryNameBs;

    @Schema(description = "Name of the category in English")
    private String spotCategoryNameEn;

    @Schema(description = "Short description of the category in Bosnian")
    private String spotCategoryDescriptionBs;

    @Schema(description = "Short description of the category in English")
    private String spotCategoryDescriptionEn;

    @Schema(description = "Color code used in showcase of the spot category")
    private String spotCategoryColorCode;
}
