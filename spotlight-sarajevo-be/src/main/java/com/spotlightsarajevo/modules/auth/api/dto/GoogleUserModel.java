package com.spotlightsarajevo.modules.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Model for Google user information")
public class GoogleUserModel implements Serializable {
    private static final long versionUUId = 1L;

    @Schema(description = "Google's unique identifier for the user")
    private String googleId;

    @Schema(description = "User's first name from Google")
    private String firstName;

    @Schema(description = "User's last name from Google")
    private String lastName;

    @Schema(description = "Username of the user. Generated initially by the system")
    private String username;

    @Schema(description = "User's locale from Google (may be null)")
    private String locale;

    @Schema(description = "User's email address from Google")
    private String email;
}