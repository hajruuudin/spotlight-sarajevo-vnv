package com.spotlightsarajevo.modules.transport.domain;

import com.spotlightsarajevo.modules.transport.domain.entity.TransportMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportMethodDAO extends JpaRepository<TransportMethodEntity, Integer> {
}
