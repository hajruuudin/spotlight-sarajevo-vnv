package com.spotlightsarajevo.modules.spot.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Default, all property, table model for the Spot Contact Entity")
public class SpotContactModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the spot contact")
    private Integer id;

    @Schema(description = "Phone number of the spot")
    private String spotContactPhone;

    @Schema(description = "Email address of the spot")
    private String spotContactEmail;

    @Schema(description = "Webpage URL of the spot")
    private String spotContactWebpage;
}

