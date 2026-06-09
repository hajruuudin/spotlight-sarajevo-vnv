package com.spotlightsarajevo.modules.tag.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Default, all property, table model for the Event Tags Entity")
public class EventTagsModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the event-tag relation")
    private Integer id;

    @Schema(description = "Identifier of the event")
    private Integer eventId;

    @Schema(description = "Identifier of the tag")
    private Integer tagId;
}
