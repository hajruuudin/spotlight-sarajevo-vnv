package com.spotlightsarajevo.modules.transport.mapper;

import com.spotlightsarajevo.modules.transport.api.dto.TransportMethodLineModel;
import com.spotlightsarajevo.modules.transport.api.dto.TransportMethodModel;
import com.spotlightsarajevo.modules.transport.api.dto.TransportMethodShorthandModel;
import com.spotlightsarajevo.modules.transport.api.dto.TransportTaxiOperatorModel;
import com.spotlightsarajevo.modules.transport.domain.entity.TransportMethodEntity;
import com.spotlightsarajevo.modules.transport.domain.entity.TransportMethodLineEntity;
import com.spotlightsarajevo.modules.transport.domain.entity.TransportTaxiOperatorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransportMapper {
    TransportMethodShorthandModel entityToShorthandDto(TransportMethodEntity entity);
    List<TransportMethodShorthandModel> entitiesToShorthandDtos(List<TransportMethodEntity> entities);

    @Mapping(target = "geometryGeoJson", ignore = true)
    TransportMethodModel entityToDto(TransportMethodEntity entity);

    @Mapping(source = "operator.id", target = "operatorId")
    @Mapping(source = "operator.transportOperatorName", target = "transportOperatorName")
    @Mapping(target = "geometryGeoJson", ignore = true)
    TransportMethodLineModel lineEntityToDto(TransportMethodLineEntity entity);

    List<TransportTaxiOperatorModel> taxiEntitiesToDtos(List<TransportTaxiOperatorEntity> entities);
}
