package com.spotlightsarajevo.modules.category.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

@Data
@Schema(description = "Default, all property, table model for the Tourist Guide Category Entity")
public class TouristGuideCategoryModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Unique identifier of the tourist guide category")
    private Long id;

    @Schema(description = "Name of the guide category in Bosnian")
    private String categoryNameBs;

    @Schema(description = "Name of the guide category in English")
    private String categoryNameEn;

    @Schema(description = "Detailed description of the guide category in Bosnian")
    private String categoryDescriptionBs;

    @Schema(description = "Detailed description of the guide category in English")
    private String categoryDescriptionEn;

    @Schema(description = "Hex color code used for the category UI elements (Cyan-Green range)")
    private String colorCode;
}