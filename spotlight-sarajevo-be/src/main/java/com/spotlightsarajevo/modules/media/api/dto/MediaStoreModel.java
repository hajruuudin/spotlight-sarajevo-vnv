package com.spotlightsarajevo.modules.media.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "Default, all property, table model for the Media Store Entity")
public class MediaStoreModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the media item")
    private Integer id;

    @Schema(description = "Identifier of the related item")
    private Integer itemId;

    @Schema(description = "Category of the item (e.g., SPOT, EVENT, SECTION_GUIDE, TRANSPORT)")
    private String itemCategory;

    @Schema(description = "URL of the media item")
    private String imageUrl;

    @Schema(description = "URL to delete the media item")
    private String imageDeleteUrl;

    @Schema(description = "Indicates if this media item is a thumbnail")
    private Boolean isThumbnail;

    @Schema(description = "Date and time when the media item was created")
    private LocalDateTime created;

    @Schema(description = "Date and time when the media item was created by user")
    private String createdBy;
}

