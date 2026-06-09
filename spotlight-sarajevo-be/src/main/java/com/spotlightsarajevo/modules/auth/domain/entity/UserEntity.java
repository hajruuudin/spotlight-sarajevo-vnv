package com.spotlightsarajevo.modules.auth.domain.entity;

import com.spotlightsarajevo.modules.category.api.dto.EventCategoryModel;
import com.spotlightsarajevo.modules.category.api.dto.SpotCategoryModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ss_user")
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "locale", columnDefinition = "VARCHAR", length = 10)
    private String locale;

    @Column(name = "first_name", columnDefinition = "VARCHAR", length = 255, nullable = false)
    private String firstName;

    @Column(name = "last_name", columnDefinition = "VARCHAR", length = 255, nullable = false)
    private String lastName;

    @Column(name = "username", columnDefinition = "VARCHAR", length = 255, nullable = false, unique = true)
    private String username;

    @Column(name = "registration_type", columnDefinition = "VARCHAR", nullable = false)
    private String registrationType;

    @Column(name = "sys_email", columnDefinition = "VARCHAR", length = 255)
    private String sysEmail;

    @Column(name = "sys_password", columnDefinition = "VARCHAR", length = 255)
    private String sysPassword;

    @Column(name = "google_id", columnDefinition = "VARCHAR", length = 255)
    private String googleId;

    @Column(name = "google_email", columnDefinition = "VARCHAR", length = 255)
    private String googleEmail;

    @Column(name = "is_premium")
    private Boolean isPremium;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "question_one")
    private Boolean questionOne;

    @Column(name = "question_two")
    private Boolean questionTwo;

    @Column(name = "question_three")
    private Boolean questionThree;

    @Column(name = "question_four")
    private Boolean questionFour;

    @Transient
    private List<SpotCategoryModel> favoriteSpotCategories;

    @Transient
    private List<EventCategoryModel> favoriteEventCategories;

    @Transient
    private Integer visitedSpotsCount;

    @Transient
    private Integer attendedEventsCount;
}