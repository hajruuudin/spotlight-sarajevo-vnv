package com.spotlightsarajevo.modules.community.domain;

import com.spotlightsarajevo.modules.community.domain.entity.CommunityRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRequestDAO extends JpaRepository<CommunityRequestEntity, Integer>, JpaSpecificationExecutor<CommunityRequestEntity> {
    List<CommunityRequestEntity> findByUserId(Integer userId);
}
