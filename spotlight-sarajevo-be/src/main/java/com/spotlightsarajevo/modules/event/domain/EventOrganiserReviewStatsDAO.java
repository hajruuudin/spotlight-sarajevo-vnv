package com.spotlightsarajevo.modules.event.domain;

import com.spotlightsarajevo.modules.event.domain.entity.EventOrganiserReviewStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventOrganiserReviewStatsDAO extends JpaRepository<EventOrganiserReviewStatsEntity, Integer> {
    Optional<EventOrganiserReviewStatsEntity> findByOrganiserId(Integer organiserId);
}
