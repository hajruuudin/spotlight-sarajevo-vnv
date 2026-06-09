package com.spotlightsarajevo.modules.community.api.dto;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Data model for community request additional data")
public class CommunityRequestDataModel implements Serializable {
    @Schema(description = "Unique identifier of the community request data")
    private Integer id;

    @Schema(description = "Identifier of the associated community request")
    private Integer communityRequestId;

    @Schema(description = "Additional pending information related to the community request in JSON format")
    private JsonNode pendingInfo;
}
