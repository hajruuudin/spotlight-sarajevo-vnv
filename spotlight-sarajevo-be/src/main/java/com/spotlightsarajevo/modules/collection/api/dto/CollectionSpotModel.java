package com.spotlightsarajevo.modules.collection.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Default, all property, table model for the Collection Spot Entity")
public class CollectionSpotModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the collection-spot relation")
    private Integer id;

    @Schema(description = "Identifier of the collection")
    private Integer collectionId;

    @Schema(description = "Identifier of the spot")
    private Integer spotId;
}

