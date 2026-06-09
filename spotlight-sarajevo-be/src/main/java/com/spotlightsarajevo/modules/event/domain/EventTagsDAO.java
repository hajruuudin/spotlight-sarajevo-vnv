package com.spotlightsarajevo.modules.event.domain;

import com.spotlightsarajevo.modules.event.domain.entity.EventTagsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventTagsDAO extends JpaRepository<EventTagsEntity, Integer> {
    List<EventTagsEntity> findByEventId(Integer id);
    void deleteAllByEventId(Integer id);
}
