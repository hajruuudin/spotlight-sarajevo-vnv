package com.spotlightsarajevo.modules.community.domain;

import com.spotlightsarajevo.modules.community.domain.entity.CommunityRequestDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunityRequestDataDAO extends JpaRepository<CommunityRequestDataEntity, Integer> {
    Optional<CommunityRequestDataEntity> findByCommunityRequestId(Integer communityRequestId);
}
