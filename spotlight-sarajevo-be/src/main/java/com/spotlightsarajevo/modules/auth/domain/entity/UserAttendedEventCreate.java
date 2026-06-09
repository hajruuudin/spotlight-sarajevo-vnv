package com.spotlightsarajevo.modules.auth.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Entity representing a user attended event creation")
public class UserAttendedEventCreate implements Serializable {
    @Schema(description = "ID of the event attended by the user")
    private Integer eventId;
}
