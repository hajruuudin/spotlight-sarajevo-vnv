package com.spotlightsarajevo.modules.auth.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ss_user_auth_details")
public class UserAuthDetailsEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @Column(name = "otp_tfa_setup")
    private Boolean otpTfaSetup;

    @Column(name = "otp_tfa_code")
    private Integer otpTfaCode;

    @Column(name = "email_tfa_setup")
    private Boolean emailTfaSetup;

    @Column(name = "email_tfa_code")
    private Integer emailTfaCode;
}
