package com.spotlightsarajevo.modules.transport.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Description for a taxi operator information")
public class TransportTaxiOperatorModel implements Serializable {
    @Schema(description = "Unique identifier of the taxi object")
    private Integer id;

    @Schema(description = "Name of the company operating the Taxi service")
    private String company;

    @Schema(description = "Taxi company phone number. Should be received in the +387- format")
    private String phone;

    @Schema(description = "Website of the taxi operator, if it exists")
    private String website;
}
