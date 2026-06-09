package com.spotlightsarajevo.modules.guide.api.dto;

import com.spotlightsarajevo.modules.media.api.dto.MediaStoreModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Shorthand model for Tourist Guides, used for displaying preview cards in lists.")
public class TouristGuideShorthandModel implements Serializable {
    @Schema(description = "Unique identifier of the tourist guide", example = "1")
    private Long id;

    @Schema(description = "Slug of the tourist guide", example = "historija-sarajeva")
    private String slug;

    @Schema(description = "Main title of the guide in Bosnian", example = "Historija Sarajeva")
    private String guideTitleBs;

    @Schema(description = "Main title of the guide in English", example = "History of Sarajevo")
    private String guideTitleEn;

    @Schema(description = "Short summary/teaser of the guide in Bosnian", example = "Kratak pregled ključnih historijskih događaja...")
    private String guideSmallDescriptionBs;

    @Schema(description = "Short summary/teaser of the guide in English", example = "A brief overview of key historical events...")
    private String guideSmallDescriptionEn;

    @Schema(description = "Thumbnail image object representing the guide")
    private MediaStoreModel thumbnailImage;

    @Schema(description = "The name of the assigned category in Bosnian", example = "Kultura")
    private String categoryNameBs;

    @Schema(description = "The name of the assigned category in English", example = "Culture")
    private String categoryNameEn;
}
