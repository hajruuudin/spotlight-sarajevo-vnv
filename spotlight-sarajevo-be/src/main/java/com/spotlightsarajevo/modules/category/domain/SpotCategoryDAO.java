package com.spotlightsarajevo.modules.category.domain;

import com.spotlightsarajevo.modules.category.domain.entity.SpotCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotCategoryDAO extends JpaRepository<SpotCategoryEntity, Integer> {
}
