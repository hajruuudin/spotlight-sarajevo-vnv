package com.spotlightsarajevo.modules.event.mapper;

import com.spotlightsarajevo.modules.event.api.dto.EventOrganiserCreateModel;
import com.spotlightsarajevo.modules.event.api.dto.EventOrganiserModel;
import com.spotlightsarajevo.modules.event.api.dto.EventOrganiserUpdateModel;
import com.spotlightsarajevo.modules.event.domain.entity.EventOrganiserEntity;
import com.spotlightsarajevo.modules.event.domain.entity.EventOrganiserReviewStatsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventOrganiserMapper {
    EventOrganiserModel entityToDto(EventOrganiserEntity entity);
    List<EventOrganiserModel> entitiesToDtos(List<EventOrganiserEntity> entities);
    EventOrganiserEntity dtoToEntity(EventOrganiserCreateModel model);

    @Mapping(source = "combinedRating", target = "overallRating")
    @Mapping(source = "combinedQuality", target = "overallQuality")
    @Mapping(source = "combinedAtmosphere", target = "overallAtmosphere")
    @Mapping(source = "combinedEnjoyability", target = "overallEnjoyability")
    void updateOrganiserFromStats(EventOrganiserReviewStatsEntity stats, @MappingTarget EventOrganiserModel organiserModel);

    void updateEntityFromDto(EventOrganiserUpdateModel updateModel, @MappingTarget EventOrganiserEntity entity);
}
