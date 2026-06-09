package com.spotlightsarajevo.modules.review.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ss_event_organiser_reviews")
public class EventOrganiserReviewEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username", columnDefinition = "VARCHAR", length = 255)
    private String username;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "header", columnDefinition = "VARCHAR", length = 255)
    private String header;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @Column(name = "user_organiser_quality", columnDefinition = "NUMERIC")
    private BigDecimal userOrganiserQuality;

    @Column(name = "user_organiser_enjoyability", columnDefinition = "NUMERIC")
    private BigDecimal userOrganiserEnjoyability;

    @Column(name = "user_organiser_atmosphere", columnDefinition = "NUMERIC")
    private BigDecimal userOrganiserAtmosphere;

    @Column(name = "user_overall_rating", columnDefinition = "NUMERIC")
    private BigDecimal userOverallRating;

    @Column(name = "organiser_id")
    private Integer organiserId;

    @Column(name = "created")
    private LocalDate created;

    @Column(name = "modified")
    private LocalDate modified;
}
