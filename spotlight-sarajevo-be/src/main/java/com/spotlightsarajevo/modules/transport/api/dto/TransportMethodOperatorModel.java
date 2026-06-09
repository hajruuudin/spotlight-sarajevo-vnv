package com.spotlightsarajevo.modules.transport.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Default, all property, table model for the Transport Method Operator Entity")
public class TransportMethodOperatorModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the transport method operator")
    private Integer id;

    @Schema(description = "ID of the transport method type this operator belongs to")
    private Integer transportTypeId;

    @Schema(description = "Name of the transport operator")
    private String transportOperatorName;

    @Schema(description = "Webpage URL of the transport operator")
    private String transportOperatorWebpage;

    @Schema(description = "Phone number of the transport operator")
    private String transportOperatorPhone;

    @Schema(description = "Email address of the transport operator")
    private String transportOperatorEmail;
}
