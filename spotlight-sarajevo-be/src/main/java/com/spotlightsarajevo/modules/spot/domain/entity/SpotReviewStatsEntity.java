package com.spotlightsarajevo.modules.spot.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import net.jcip.annotations.Immutable;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Immutable
@Table(name = "ss_spot_review_stats")
public class SpotReviewStatsEntity implements Serializable {
    @Id
    @Column(name = "spot_id")
    private Integer spotId;

    @Column(name = "combined_rating", columnDefinition = "NUMERIC")
    private BigDecimal combinedRating;

    @Column(name = "combined_cleanliness", columnDefinition = "NUMERIC")
    private BigDecimal combinedCleanliness;

    @Column(name = "combined_affordability", columnDefinition = "NUMERIC")
    private BigDecimal combinedAffordability;

    @Column(name = "combined_accessibility", columnDefinition = "NUMERIC")
    private BigDecimal combinedAccessibility;

    @Column(name = "combined_staff_kindness", columnDefinition = "NUMERIC")
    private BigDecimal combinedStaffKindness;

    @Column(name = "combined_quality", columnDefinition = "NUMERIC")
    private BigDecimal combinedQuality;

    @Column(name = "combined_atmosphere", columnDefinition = "NUMERIC")
    private BigDecimal combinedAtmosphere;
}
