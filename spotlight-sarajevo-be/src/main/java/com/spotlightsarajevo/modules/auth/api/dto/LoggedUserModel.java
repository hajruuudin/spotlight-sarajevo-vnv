package com.spotlightsarajevo.modules.auth.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "Properties of a logged in user, returned to the fronted to keep in the session")
@AllArgsConstructor
public class LoggedUserModel implements Serializable {
    @Serial
    private static final Long versionUUId = 1L;

    @Schema(description = "Id of the user")
    private Integer id;

    @Schema(description = "First name of the user")
    private String firstName;

    @Schema(description = "Last name of the user")
    private String lastName;

    @Schema(description = "Email of the user (Google or System)")
    private String email;

    @Schema(description = "Username of the user")
    private String username;

    @Schema(description = "Admin status of the user")
    private Boolean isAdmin;

    @Schema(description = "Premium status of the user")
    private Boolean isPremium;
}
