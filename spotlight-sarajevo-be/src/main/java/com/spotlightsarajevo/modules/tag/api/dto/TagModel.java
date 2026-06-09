package com.spotlightsarajevo.modules.tag.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Default, all property, table model for the Tag Entity")
public class TagModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the tag")
    private Integer id;

    @Schema(description = "Name of the tag in Bosnian")
    private String tagNameBs;

    @Schema(description = "Name of the tag in English")
    private String tagNameEn;

    @Schema(description = "Description of the tag in Bosnian")
    private String tagDescriptionBs;

    @Schema(description = "Description of the tag in English")
    private String tagDescriptionEn;
}

