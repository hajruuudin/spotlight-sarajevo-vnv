package com.spotlightsarajevo.modules.spot.domain;

import com.spotlightsarajevo.modules.spot.domain.entity.SpotWorkHoursEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotWorkHoursDAO extends JpaRepository<SpotWorkHoursEntity, Integer> {
    List<SpotWorkHoursEntity> findAllBySpotId(Integer spotId);

    @Modifying
    @Query("DELETE FROM SpotWorkHoursEntity wh WHERE wh.spotId = :spotId")
    void deleteAllBySpotId(@Param("spotId") Integer spotId);
}
