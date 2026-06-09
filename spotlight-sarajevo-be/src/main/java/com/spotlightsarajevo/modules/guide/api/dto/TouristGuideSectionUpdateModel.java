package com.spotlightsarajevo.modules.guide.api.dto;

import com.spotlightsarajevo.modules.media.api.dto.MediaStoreCreateModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Properties for updating a Tourist Guide Section")
public class TouristGuideSectionUpdateModel implements Serializable {
    @Schema(description = "The unique identifier of the section")
    private Integer id;

    @Schema(description = "The section title in Bosnian")
    private String sectionTitleBs;

    @Schema(description = "The section title in English")
    private String sectionTitleEn;

    @Schema(description = "The section body content in Bosnian")
    private String sectionBodyBs;

    @Schema(description = "The section body content in English")
    private String sectionBodyEn;

    @Schema(description = "The current thumbnail URL of the section")
    private String thumbnailUrl;

    @Schema(description = "New thumbnail image to replace the current one (optional)")
    private MediaStoreCreateModel newThumbnailImage;

    @Schema(description = "The order index for the section")
    private Integer orderIdx;
}
