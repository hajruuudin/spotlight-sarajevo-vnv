package com.spotlightsarajevo.modules.transport.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Model for Transport Method Line representing a public transport route")
public class TransportMethodLineModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the transport line")
    private Integer id;

    @Schema(description = "ID of the operator running this line")
    private Integer operatorId;

    @Schema(description = "Name of the operator running this line")
    private String transportOperatorName;

    @Schema(description = "Starting point/station of the line")
    private String lineStart;

    @Schema(description = "Ending point/station of the line")
    private String lineEnd;

    @Schema(description = "Line number or identifier")
    private String lineNumber;

    @Schema(description = "GeoJSON representation of the line geometry")
    private String geometryGeoJson;
}
