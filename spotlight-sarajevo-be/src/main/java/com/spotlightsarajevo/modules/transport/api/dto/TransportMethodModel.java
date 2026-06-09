package com.spotlightsarajevo.modules.transport.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Default, all property, table model for the Transport Method Entity")
public class TransportMethodModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the transport method")
    private Integer id;

    @Schema(description = "Name of the transport method in Bosnian")
    private String methodNameBs;

    @Schema(description = "Name of the transport method in English")
    private String methodNameEn;

    @Schema(description = "Description of the transport method in Bosnian")
    private String methodDescriptionBs;

    @Schema(description = "Description of the transport method in English")
    private String methodDescriptionEn;

    @Schema(description = "GeoJSON representation of the transport method geometry")
    private String geometryGeoJson;
}

