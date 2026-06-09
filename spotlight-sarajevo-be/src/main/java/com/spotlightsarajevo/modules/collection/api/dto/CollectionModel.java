package com.spotlightsarajevo.modules.collection.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "Default, all property, table model for the Collection Entity")
public class CollectionModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the collection")
    private Integer id;

    @Schema(description = "Determines if the collection is a system created one, or a custom created one")
    private Boolean isSystem;

    @Schema(description = "Name of the collection")
    private String collectionName;

    @Schema(description = "Description of the collection")
    private String collectionDescription;

    @Schema(description = "Type of the collection (SPOT or EVENT)")
    private String collectionType;

    @Schema(description = "Timestamp when the collection was created")
    private LocalDateTime created;

    @Schema(description = "Identifier or name of the user who created the collection")
    private String createdBy;

    @Schema(description = "Identifier of the user owning the collection")
    private Integer userId;
}
