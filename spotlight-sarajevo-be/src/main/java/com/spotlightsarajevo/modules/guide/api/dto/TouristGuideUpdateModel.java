package com.spotlightsarajevo.modules.guide.api.dto;

import com.spotlightsarajevo.modules.media.api.dto.MediaStoreCreateModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "Properties for updating a Tourist Guide")
public class TouristGuideUpdateModel implements Serializable {
    @Schema(description = "The unique identifier of the guide")
    private Integer id;

    @Schema(description = "The slug identifier for the guide")
    private String slug;

    @Schema(description = "The guide title in Bosnian")
    private String guideTitleBs;

    @Schema(description = "The guide title in English")
    private String guideTitleEn;

    @Schema(description = "The small description of the guide in Bosnian")
    private String guideSmallDescriptionBs;

    @Schema(description = "The small description of the guide in English")
    private String guideSmallDescriptionEn;

    @Schema(description = "The full description of the guide in Bosnian")
    private String guideFullDescriptionBs;

    @Schema(description = "The full description of the guide in English")
    private String guideFullDescriptionEn;

    @Schema(description = "The category ID associated with the guide")
    private Integer categoryId;

    @Schema(description = "The current thumbnail URL of the guide")
    private String thumbnailUrl;

    @Schema(description = "New thumbnail image to replace the current one (optional)")
    private MediaStoreCreateModel newThumbnailImage;

    @Schema(description = "List of sections to update")
    private List<TouristGuideSectionUpdateModel> toUpdateSections;

    @Schema(description = "List of sections to add")
    private List<TouristGuideSectionCreateModel> toAddSections;

    @Schema(description = "List of section IDs to delete")
    private List<Integer> toDeleteSections;
}
