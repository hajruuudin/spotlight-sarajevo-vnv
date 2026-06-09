package com.spotlightsarajevo.modules.auth.domain;

import com.spotlightsarajevo.modules.auth.domain.entity.UserFavouriteEventsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavouriteEventsDAO extends JpaRepository<UserFavouriteEventsEntity, Integer> {
    List<UserFavouriteEventsEntity> findAllByUserId(Integer userId);
}
