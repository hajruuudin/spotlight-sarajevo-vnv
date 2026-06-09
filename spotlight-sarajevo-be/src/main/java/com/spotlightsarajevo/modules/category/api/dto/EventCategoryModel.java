package com.spotlightsarajevo.modules.category.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Default, all property, table model for the Event Category Entity")
public class EventCategoryModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the event category")
    private Integer id;

    @Schema(description = "Name of the category in Bosnian")
    private String eventCategoryNameBs;

    @Schema(description = "Name of the category in English")
    private String eventCategoryNameEn;

    @Schema(description = "Short description of the category in Bosnian")
    private String eventCategoryDescriptionBs;

    @Schema(description = "Short description of the category in English")
    private String eventCategoryDescriptionEn;

    @Schema(description = "Color code used in showcase of the event category")
    private String eventCategoryColorCode;
}
