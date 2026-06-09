package com.spotlightsarajevo.modules.event.mapper;

import com.spotlightsarajevo.modules.event.api.dto.*;
import com.spotlightsarajevo.modules.event.domain.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {
    EventEntity dtoToEntity(EventCreateModel eventCreateModel);
    EventModel entityToDto(EventEntity entity);
    List<EventShorthandModel> entitiesToShorthandDtos(List<EventEntity> entities);
    EventOverviewModel entityToOverviewDto(EventEntity eventEntity);
    @Mapping(target = "thumbnailImage", ignore = true)
    void updateEntityFromDto(EventUpdateModel eventUpdateModel, @MappingTarget EventEntity eventEntity);
    EventShorthandModel entityToShorthandDto(EventEntity eventEntity);
}
