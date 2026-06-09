package com.spotlightsarajevo.modules.spot.domain;

import com.spotlightsarajevo.modules.spot.domain.entity.SpotReviewStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotReviewStatsDAO extends JpaRepository<SpotReviewStatsEntity, Integer> {
    SpotReviewStatsEntity findBySpotId(Integer spotId);
}
