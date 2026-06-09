package com.spotlightsarajevo.modules.guide.domain;

import com.spotlightsarajevo.common.specifications.TouristGuideSpecification;
import com.spotlightsarajevo.modules.guide.domain.entity.TouristGuideEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface TouristGuideDAO extends JpaRepository<TouristGuideEntity, Integer>, JpaSpecificationExecutor<TouristGuideEntity> {
    Optional<TouristGuideEntity> findBySlug(String slug);
    List<TouristGuideEntity> findByCategoryId(Integer id);
}
