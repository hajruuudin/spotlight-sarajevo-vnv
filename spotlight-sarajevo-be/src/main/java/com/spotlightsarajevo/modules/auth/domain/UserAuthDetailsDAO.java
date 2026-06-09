package com.spotlightsarajevo.modules.auth.domain;

import com.spotlightsarajevo.modules.auth.domain.entity.UserAuthDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthDetailsDAO extends JpaRepository<UserAuthDetailsEntity, Integer> {
}
