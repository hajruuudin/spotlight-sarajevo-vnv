package com.spotlightsarajevo.modules.auth.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Model for System register user information")
public class SystemUserModel implements Serializable {
    private static final long versionUUId = 1L;

    @Schema(description = "First name")
    private String firstName;

    @Schema(description = "Last name")
    private String lastName;

    @Schema(description = "Username of the user. Generated initially by the system")
    private String username;

    @Schema(description = "Email of the user")
    private String email;

    @Schema(description = "Hashed password")
    private String password;
}
