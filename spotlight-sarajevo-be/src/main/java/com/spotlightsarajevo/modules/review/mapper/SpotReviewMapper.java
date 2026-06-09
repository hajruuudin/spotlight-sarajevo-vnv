package com.spotlightsarajevo.modules.review.mapper;

import com.spotlightsarajevo.modules.review.api.dto.SpotReviewCreateModel;
import com.spotlightsarajevo.modules.review.api.dto.SpotReviewModel;
import com.spotlightsarajevo.modules.review.api.dto.SpotReviewUpdateModel;
import com.spotlightsarajevo.modules.review.domain.entity.SpotReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpotReviewMapper {
    List<SpotReviewModel> entitiesToDto(List<SpotReviewEntity> entity);
    SpotReviewEntity dtoToEntity(SpotReviewCreateModel dto);
    SpotReviewModel entityToDto(SpotReviewEntity entity);
    void updateEntityFromDto(SpotReviewUpdateModel dto, @MappingTarget SpotReviewEntity entity);
}
