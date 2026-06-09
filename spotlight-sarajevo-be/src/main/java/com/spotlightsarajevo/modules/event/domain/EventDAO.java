package com.spotlightsarajevo.modules.event.domain;

import com.spotlightsarajevo.modules.event.domain.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventDAO extends JpaRepository<EventEntity, Integer>, JpaSpecificationExecutor<EventEntity> {
    Page<EventEntity> findAll(Pageable query);
    Optional<EventEntity> findBySlug(String eventSlug);

    @Query("SELECT COUNT(e) FROM EventEntity e")
    Long countAllEvents();

    @Query(value = "SELECT * FROM ss_event ORDER BY created DESC LIMIT :limit", nativeQuery = true)
    List<EventEntity> findRecentlyAdded(@Param("limit") Integer limit);
}
