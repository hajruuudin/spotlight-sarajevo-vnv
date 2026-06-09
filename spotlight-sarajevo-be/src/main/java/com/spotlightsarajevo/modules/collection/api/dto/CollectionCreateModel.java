package com.spotlightsarajevo.modules.collection.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Properties for a Collection create model")
public class CollectionCreateModel implements Serializable {
    @Schema(description = "Name of the collection")
    private String collectionName;

    @Schema(description = "Description of the collection")
    private String collectionDescription;

    @Schema(description = "Type of the collection (SPOT or EVENT)")
    private String collectionType;
}
