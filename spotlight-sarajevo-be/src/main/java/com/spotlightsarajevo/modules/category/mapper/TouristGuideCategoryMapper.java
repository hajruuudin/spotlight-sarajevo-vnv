package com.spotlightsarajevo.modules.category.mapper;

import com.spotlightsarajevo.modules.category.api.dto.TouristGuideCategoryModel;
import com.spotlightsarajevo.modules.category.domain.entity.TouristGuideCategoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TouristGuideCategoryMapper {
    List<TouristGuideCategoryModel> entitiesToDtos(List<TouristGuideCategoryEntity> entity);
}
