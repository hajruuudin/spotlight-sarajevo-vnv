package com.spotlightsarajevo.modules.guide.api.dto;

import com.spotlightsarajevo.common.enums.GuideType;
import com.spotlightsarajevo.modules.media.api.dto.MediaStoreModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@Schema(description = "Properties for a simple tourist guide object returned to the create and update models")
public class TouristGuideModel implements Serializable {
    @Schema(description = "The unique identifier of the guide object")
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

    @Schema(description = "The type of the guide")
    private GuideType guideType;

    @Schema(description = "Contact information for the guide")
    private Map<String, String> contactInfo;

    @Schema(description = "The category ID associated with the guide")
    private Integer categoryId;

    @Schema(description = " Thumbnail Image")
    private MediaStoreModel thumbnailImage;
}
