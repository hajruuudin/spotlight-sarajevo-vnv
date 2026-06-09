package com.spotlightsarajevo.modules.collection.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Properties for a model which contains a record to add a new Event to a collection")
public class CollectionAddItemModel implements Serializable {
    @Schema(description = "Id of the object to be added")
    private Integer objectId;

    @Schema(description = "Type of the object to be added")
    private String objectType;

    @Schema(description = "Id of the collection to add the event to")
    private Integer collectionId;

    @Schema(description = "Determines if the collection to be added is a system collection or user collection")
    private Boolean isSystem;
}
