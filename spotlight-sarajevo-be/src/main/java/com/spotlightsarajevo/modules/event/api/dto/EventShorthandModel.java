package com.spotlightsarajevo.modules.event.api.dto;

import com.spotlightsarajevo.common.utils.CollectionItem;
import com.spotlightsarajevo.modules.media.api.dto.MediaStoreModel;
import com.spotlightsarajevo.modules.tag.api.dto.TagModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Properties for an event shorthand model")
public class EventShorthandModel implements Serializable, CollectionItem {
    private static final long versionUUId = 1L;

    @Schema(description = "Unique identifier of the event")
    private Integer id;

    @Schema(description = "Unique slug identifier of the event")
    private String slug;

    @Schema(description = "Official name of the event in Bosnian")
    private String officialNameBs;

    @Schema(description = "Official name of the event in English")
    private String officialNameEn;

    @Schema(description = "Small description/headline of the event in Bosnian")
    private String smallDescriptionBs;

    @Schema(description = "Small description/headline of the event in English")
    private String smallDescriptionEn;

    @Schema(description = "Category name for the event in bosnian")
    private String categoryNameBs;

    @Schema(description = "Category name for the event in english")
    private String categoryNameEn;

    @Schema(description = "The start date of the event")
    private LocalDateTime startDate;

    @Schema(description = "The end date of the event")
    private LocalDateTime endDate;

    @Schema(description = "The thumbnail image of the event")
    private MediaStoreModel thumbnailImage;

    @Schema(description = "Tags related to the event")
    private List<TagModel> eventTags;
}
