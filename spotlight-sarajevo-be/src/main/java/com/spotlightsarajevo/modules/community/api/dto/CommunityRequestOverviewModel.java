package com.spotlightsarajevo.modules.community.api.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.spotlightsarajevo.common.enums.ObjectType;
import com.spotlightsarajevo.common.enums.RequestStatus;
import com.spotlightsarajevo.common.enums.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "Overview model for the Community Request Entity with pending info and request description")
public class CommunityRequestOverviewModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the community request")
    private Integer id;

    @Schema(description = "Identifier of the user who made the request")
    private Integer userId;

    @Schema(description = "Username of the user who made the request")
    private String username;

    @Schema(description = "Type of request submitted")
    private RequestType requestType;

    @Schema(description = "Type of object the request concerns")
    private ObjectType objectType;

    @Schema(description = "Short header or title of the request")
    private String requestHeader;

    @Schema(description = "Detailed description of the request")
    private String requestDescription;

    @Schema(description = "Status of the request as of being added")
    private RequestStatus status;

    @Schema(description = "Timestamp when the request was created")
    private LocalDateTime createdAt;

    @Schema(description = "Additional pending information related to the community request in JSON format")
    private JsonNode pendingInfo;
}

