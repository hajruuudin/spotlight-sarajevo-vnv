package com.spotlightsarajevo.modules.category.domain;

import com.spotlightsarajevo.modules.category.domain.entity.EventCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCategoryDAO extends JpaRepository<EventCategoryEntity, Integer> {
}
