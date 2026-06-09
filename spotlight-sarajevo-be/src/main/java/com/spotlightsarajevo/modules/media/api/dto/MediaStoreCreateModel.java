package com.spotlightsarajevo.modules.media.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "Default, all property, table model for the Media Store Entity")
@AllArgsConstructor
@NoArgsConstructor
public class MediaStoreCreateModel implements Serializable {
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
