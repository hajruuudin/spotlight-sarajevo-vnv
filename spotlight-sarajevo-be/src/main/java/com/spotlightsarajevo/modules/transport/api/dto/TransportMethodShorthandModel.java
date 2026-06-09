package com.spotlightsarajevo.modules.transport.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Shorthand model for Transport Method containing only ID and names")
public class TransportMethodShorthandModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the transport method")
    private Integer id;

    @Schema(description = "Name of the transport method in Bosnian")
    private String methodNameBs;

    @Schema(description = "Name of the transport method in English")
    private String methodNameEn;
}
