package com.spotlightsarajevo.modules.event.api.dto;

import com.spotlightsarajevo.modules.media.api.dto.MediaStoreCreateModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Properties for an event organiser update model")
public class EventOrganiserUpdateModel implements Serializable {
    @Schema(description = "Unique identifier for the organiser")
    private Integer id;

    @Schema(description = "ID of the associated category")
    private Integer organiserCategoryId;

    @Schema(description = "The date the organiser was created", example = "2026-03-06T12:00:00Z")
    private LocalDate organiserCreationDate;

    @Schema(description = "Contact email of the organiser", example = "contact@organiser.com")
    private String organiserEmail;

    @Schema(description = "Full name of the organiser")
    private String organiserName;

    @Schema(description = "Contact phone number")
    private String organiserPhone;

    @Schema(description = "Official website URL")
    private String organiserWebsite;

    @Schema(description = "Current thumbnail image URL of the Spot")
    private String thumbnailImage;

    @Schema(description = "New Thumbnail image Object of the Spot")
    private MediaStoreCreateModel newThumbnailImage;

    @Schema(description = "List of new Objects to be added for images of the Spot")
    private List<MediaStoreCreateModel> toAddImages;

    @Schema(description = "List of IDs of the images to be removed")
    private List<Integer> toRemoveImages;
}
