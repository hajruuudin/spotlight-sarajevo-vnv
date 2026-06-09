package com.spotlightsarajevo.modules.community.api.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.spotlightsarajevo.common.enums.ObjectType;
import com.spotlightsarajevo.common.enums.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommunityRequestCreateModel implements Serializable {
    @Schema(description = "Type of request submitted")
    private RequestType requestType;

    @Schema(description = "Type of object the request concerns")
    private ObjectType objectType;

    @Schema(description = "Short header or title of the request")
    private String requestHeader;

    @Schema(description = "Detailed description of the request")
    private String requestDescription;

    @Schema(description = "Optional body of the request. Can be contained if the request type is ADD")
    private JsonNode requestBody;
}
