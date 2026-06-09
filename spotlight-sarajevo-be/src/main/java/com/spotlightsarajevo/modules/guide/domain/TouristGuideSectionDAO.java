package com.spotlightsarajevo.modules.guide.domain;

import com.spotlightsarajevo.modules.guide.domain.entity.TouristGuideSectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
public interface TouristGuideSectionDAO extends JpaRepository<TouristGuideSectionEntity, Integer> {
    List<TouristGuideSectionEntity> findAllByGuideId(Integer guideId);
}
