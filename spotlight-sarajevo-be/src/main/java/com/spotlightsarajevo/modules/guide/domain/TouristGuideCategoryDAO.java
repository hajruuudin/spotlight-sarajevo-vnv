package com.spotlightsarajevo.modules.guide.domain;

import com.spotlightsarajevo.modules.category.domain.entity.TouristGuideCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TouristGuideCategoryDAO extends JpaRepository<TouristGuideCategoryEntity, Integer> {
    Optional<TouristGuideCategoryEntity> findById(Integer id);
}
