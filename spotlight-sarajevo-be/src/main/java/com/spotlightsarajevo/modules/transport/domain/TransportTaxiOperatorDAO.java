package com.spotlightsarajevo.modules.transport.domain;

import com.spotlightsarajevo.modules.transport.domain.entity.TransportTaxiOperatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportTaxiOperatorDAO extends JpaRepository<TransportTaxiOperatorEntity, Integer> {
}
