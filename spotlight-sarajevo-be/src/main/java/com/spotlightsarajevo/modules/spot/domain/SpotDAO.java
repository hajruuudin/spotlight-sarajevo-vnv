package com.spotlightsarajevo.modules.spot.domain;

import com.spotlightsarajevo.modules.spot.domain.entity.SpotEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpotDAO extends JpaRepository<SpotEntity, Integer>, JpaSpecificationExecutor<SpotEntity> {
    Page<SpotEntity> findAll(Pageable query);
    Optional<SpotEntity> findBySlug(String spotSlug);

    @Query("SELECT COUNT(s) FROM SpotEntity s")
    Long countAllSpots();

    @Query(value = "SELECT * FROM ss_spot ORDER BY created DESC LIMIT :limit", nativeQuery = true)
    List<SpotEntity> findRecentlyAdded(@Param("limit") Integer limit);
}
