package com.spotlightsarajevo.modules.tag.domain;

import com.spotlightsarajevo.modules.tag.domain.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagDAO extends JpaRepository<TagEntity, Integer> {
}
