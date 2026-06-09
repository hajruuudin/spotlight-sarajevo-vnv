package com.spotlightsarajevo.modules.guide.api.dto;

import com.spotlightsarajevo.modules.media.api.dto.MediaStoreModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Default, all property, table model for the Tourist Guide Section Entity")
public class TouristGuideSectionModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the tourist guide section")
    private Integer id;

    @Schema(description = "ID of the associated tourist guide")
    private Integer guideId;

    @Schema(description = "Title of the section in Bosnian")
    private String sectionTitleBs;

    @Schema(description = "Title of the section in English")
    private String sectionTitleEn;

    @Schema(description = "Body/content of the section in Bosnian")
    private String sectionBodyBs;

    @Schema(description = "Body/content of the section in English")
    private String sectionBodyEn;

    @Schema(description = "Image of the section, labeled as thumbnail image")
    private MediaStoreModel thumbnailImage;
}
