package com.spotlightsarajevo.modules.tag.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Mapping between a Spot and its Tags")
public class SpotTagsModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the spot-tag mapping")
    private Integer id;

    @Schema(description = "Identifier of the associated spot")
    private Integer spotId;

    @Schema(description = "Identifier of the associated tag")
    private Integer tagId;
}

