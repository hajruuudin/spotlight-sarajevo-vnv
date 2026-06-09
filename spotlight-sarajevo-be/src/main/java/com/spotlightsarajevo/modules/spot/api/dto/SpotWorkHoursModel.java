package com.spotlightsarajevo.modules.spot.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;

@Data
@Schema(description = "Working hours for a specific Spot")
public class SpotWorkHoursModel implements Serializable {
    private static final Long versionUUID = 1L;

    @Schema(description = "Unique identifier of the work hours entry")
    private Integer id;

    @Schema(description = "Index of the day in the regular Week")
    private Integer dayIndex;

    @Schema(description = "Day of the week")
    private String day;

    @Schema(description = "Start time of the work hours")
    private LocalTime startTime;

    @Schema(description = "End time of the work hours")
    private LocalTime endTime;

    @Schema(description = "Identifier of the associated spot")
    private Integer spotId;
}
