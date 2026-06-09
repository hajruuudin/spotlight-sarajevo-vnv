package com.spotlightsarajevo.modules.collection.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Update an existing collections name and description")
public class CollectionUpdateModel implements Serializable {
    @Schema(description = "Unique identifier of the collection")
    private Integer id;

    @Schema(description = "Name of the collection")
    private String collectionName;

    @Schema(description = "Description of the collection")
    private String collectionDescription;
}
