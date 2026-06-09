package com.spotlightsarajevo.modules.collection.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Default, all property, table model for the Collection Event Entity")
public class CollectionEventModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the collection-event relation")
    private Integer id;

    @Schema(description = "Identifier of the collection")
    private Integer collectionId;

    @Schema(description = "Identifier of the event")
    private Integer eventId;
}
