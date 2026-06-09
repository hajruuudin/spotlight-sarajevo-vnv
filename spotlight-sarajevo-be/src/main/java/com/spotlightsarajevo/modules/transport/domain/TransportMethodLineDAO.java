package com.spotlightsarajevo.modules.transport.domain;

import com.spotlightsarajevo.modules.transport.domain.entity.TransportMethodLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportMethodLineDAO extends JpaRepository<TransportMethodLineEntity, Integer> {
    List<TransportMethodLineEntity> findByOperatorId(Integer operatorId);
    List<TransportMethodLineEntity> findByTransportTypeId(Integer transportTypeId);
    List<TransportMethodLineEntity> findByOperatorIdAndTransportTypeId(Integer operatorId, Integer transportTypeId);
}
