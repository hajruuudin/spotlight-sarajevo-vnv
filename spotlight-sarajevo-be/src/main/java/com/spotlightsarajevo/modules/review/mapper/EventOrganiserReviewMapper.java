package com.spotlightsarajevo.modules.review.mapper;

import com.spotlightsarajevo.modules.review.api.dto.EventOrganiserReviewCreateModel;
import com.spotlightsarajevo.modules.review.api.dto.EventOrganiserReviewModel;
import com.spotlightsarajevo.modules.review.api.dto.EventOrganiserReviewUpdateModel;
import com.spotlightsarajevo.modules.review.domain.entity.EventOrganiserReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventOrganiserReviewMapper {
    List<EventOrganiserReviewModel> entitiesToDto(List<EventOrganiserReviewEntity> entity);
    EventOrganiserReviewEntity dtoToEntity(EventOrganiserReviewCreateModel dto);
    EventOrganiserReviewModel entityToDto(EventOrganiserReviewEntity entity);
    void updateEntityFromDto(EventOrganiserReviewUpdateModel dto, @MappingTarget EventOrganiserReviewEntity entity);
}
