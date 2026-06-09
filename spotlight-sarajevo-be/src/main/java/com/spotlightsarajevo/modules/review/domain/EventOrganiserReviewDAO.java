package com.spotlightsarajevo.modules.review.domain;

import com.spotlightsarajevo.modules.review.domain.entity.EventOrganiserReviewEntity;
import com.spotlightsarajevo.modules.review.domain.entity.SpotReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventOrganiserReviewDAO extends JpaRepository<EventOrganiserReviewEntity, Integer>, JpaSpecificationExecutor<EventOrganiserReviewEntity> {
    Page<EventOrganiserReviewEntity> findAll(Pageable request);
    Optional<EventOrganiserReviewEntity> findByOrganiserIdAndUserId(Integer spotId, Integer userId);
}
