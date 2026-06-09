package com.spotlightsarajevo.modules.spot.domain;

import com.spotlightsarajevo.modules.spot.domain.entity.SpotTagsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotTagsDAO extends JpaRepository<SpotTagsEntity, Integer> {
    List<SpotTagsEntity> findBySpotId(Integer id);

    @Modifying
    @Query("DELETE FROM SpotTagsEntity st WHERE st.spotId = :spotId")
    void deleteAllBySpotId(@Param("spotId") Integer spotId);
}
