package com.spotlightsarajevo.modules.auth.domain;

import com.spotlightsarajevo.modules.auth.domain.entity.UserFavouriteSpotsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
public interface UserFavouriteSpotsDAO extends JpaRepository<UserFavouriteSpotsEntity, Integer> {
    List<UserFavouriteSpotsEntity> findAllByUserId(Integer userId);
}
