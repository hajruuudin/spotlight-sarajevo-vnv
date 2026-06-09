package com.spotlightsarajevo.modules.community.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Model for updating the status of a Community Request")
public class CommunityRequestStatusUpdate implements Serializable {
    @Schema(description = "Unique identifier of the community request to be updated")
    private Integer requestId;

    @Schema(description = "New status to set for the community request")
    private String status;

    @Schema(description = "Optional reason for the status update")
    private String statusInfo;
}
