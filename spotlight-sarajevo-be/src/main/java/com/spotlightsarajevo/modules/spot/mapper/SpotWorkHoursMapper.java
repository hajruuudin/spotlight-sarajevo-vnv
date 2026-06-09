package com.spotlightsarajevo.modules.spot.mapper;

import com.spotlightsarajevo.modules.spot.api.dto.SpotWorkHoursModel;
import com.spotlightsarajevo.modules.spot.domain.entity.SpotWorkHoursEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpotWorkHoursMapper {
    List<SpotWorkHoursModel> entitiesToDtos(List<SpotWorkHoursEntity> entities);

    @Mapping(target = "id", ignore = true)
    SpotWorkHoursEntity dtoToEntity(SpotWorkHoursModel dto);

    List<SpotWorkHoursEntity> dtosToEntities(List<SpotWorkHoursModel> dtos);
}
