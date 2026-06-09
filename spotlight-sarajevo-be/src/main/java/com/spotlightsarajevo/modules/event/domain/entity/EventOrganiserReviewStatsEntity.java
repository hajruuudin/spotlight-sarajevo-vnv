package com.spotlightsarajevo.modules.event.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.jcip.annotations.Immutable;

import java.math.BigDecimal;

@Getter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Immutable
@Table(name = "ss_organiser_review_stats")
public class EventOrganiserReviewStatsEntity {
    @Id
    @Column(name = "organiser_id")
    private Integer organiserId;

    @Column(name = "combined_rating", columnDefinition = "NUMERIC")
    private BigDecimal combinedRating;

    @Column(name = "combined_enjoyability", columnDefinition = "NUMERIC")
    private BigDecimal combinedEnjoyability;

    @Column(name = "combined_quality", columnDefinition = "NUMERIC")
    private BigDecimal combinedQuality;

    @Column(name = "combined_atmosphere", columnDefinition = "NUMERIC")
    private BigDecimal combinedAtmosphere;
}
