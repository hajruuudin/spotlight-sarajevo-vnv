package com.spotlightsarajevo.modules.review.domain;

import com.spotlightsarajevo.modules.review.domain.entity.SpotReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpotReviewDAO extends JpaRepository<SpotReviewEntity, Integer>, JpaSpecificationExecutor<SpotReviewEntity> {
    Page<SpotReviewEntity> findAll(Pageable request);
    Optional<SpotReviewEntity> findBySpotIdAndUserId(Integer spotId, Integer userId);
}
