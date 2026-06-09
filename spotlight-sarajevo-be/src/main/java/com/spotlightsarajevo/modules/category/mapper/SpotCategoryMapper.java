package com.spotlightsarajevo.modules.category.mapper;

import com.spotlightsarajevo.modules.category.api.dto.SpotCategoryModel;
import com.spotlightsarajevo.modules.category.domain.entity.SpotCategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpotCategoryMapper {
    SpotCategoryModel entityToDto(SpotCategoryEntity entity);
    List<SpotCategoryModel> entitiesToDtos(List<SpotCategoryEntity> entities);
}
