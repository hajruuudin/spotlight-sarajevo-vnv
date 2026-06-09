package com.spotlightsarajevo.modules.guide.api.dto;

import com.spotlightsarajevo.common.enums.GuideType;
import com.spotlightsarajevo.modules.media.api.dto.MediaStoreModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Schema(description = "Full overview model for a Tourist Guide, including all localized content and detailed sections.")
public class TouristGuideOverviewModel implements Serializable {
    @Schema(description = "Unique identifier of the tourist guide", example = "1")
    private Long id;

    @Schema(description = "Slug of the tourist guide", example = "historija-sarajeva")
    private String slug;

    @Schema(description = "Full title of the guide in Bosnian", example = "Vodič kroz sarajevske zanate")
    private String guideTitleBs;

    @Schema(description = "Full title of the guide in English", example = "Guide to Sarajevo Crafts")
    private String guideTitleEn;

    @Schema(description = "Small introductory text in Bosnian")
    private String guideSmallDescriptionBs;

    @Schema(description = "Small introductory text in English")
    private String guideSmallDescriptionEn;

    @Schema(description = "Comprehensive introductory text in Bosnian (supports HTML content)")
    private String guideFullDescriptionBs;

    @Schema(description = "Comprehensive introductory text in English (supports HTML content)")
    private String guideFullDescriptionEn;

    @Schema(description = "Category ID of the guide")
    private Integer categoryId;

    @Schema(description = "Name of the category in Bosnian", example = "Tradicija")
    private String categoryNameBs;

    @Schema(description = "Name of the category in English", example = "Tradition")
    private String categoryNameEn;

    @Schema(description = "List of detailed sections that make up the body of the guide, ordered by order_idx")
    private List<TouristGuideSectionModel> sections;

    @Schema(description = "Type of the guide, can be SYSTEM or EXTERNAL if it is organised by someone else")
    private GuideType guideType;

    @Schema(description = "Contact info of the guide organiser, if it is not a default system guide")
    private Map<String, String> contactInfo;

    @Schema(description = "Thumbnail image of the overview model")
    private MediaStoreModel thumbnailImage;
}
