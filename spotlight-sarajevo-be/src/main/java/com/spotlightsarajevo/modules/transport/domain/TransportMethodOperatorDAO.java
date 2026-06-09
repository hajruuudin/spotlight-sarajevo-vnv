package com.spotlightsarajevo.modules.transport.domain;

import com.spotlightsarajevo.modules.transport.domain.entity.TransportMethodOperatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportMethodOperatorDAO extends JpaRepository<TransportMethodOperatorEntity, Integer> {
}
