package com.spotlightsarajevo.modules.category.mapper;

import com.spotlightsarajevo.modules.category.api.dto.EventCategoryModel;
import com.spotlightsarajevo.modules.category.domain.entity.EventCategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventCategoryMapper {
    EventCategoryModel entityToDto(EventCategoryEntity entity);
    List<EventCategoryModel> entitiesToDtos(List<EventCategoryEntity> entity);
}
