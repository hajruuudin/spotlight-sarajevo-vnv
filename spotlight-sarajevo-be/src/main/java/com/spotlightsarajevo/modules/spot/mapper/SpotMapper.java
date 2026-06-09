package com.spotlightsarajevo.modules.spot.mapper;

import com.spotlightsarajevo.modules.spot.api.dto.*;
import com.spotlightsarajevo.modules.spot.domain.entity.SpotEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpotMapper {
    SpotEntity dtoToEntity(SpotCreateModel spotCreateModel);
    SpotModel entityToDto(SpotEntity spotEntity);
    List<SpotModel> entitiesToDtos(List<SpotEntity> spotEntityList);
    @Mapping(target = "thumbnailImage", ignore = true)
    void updateEntityFromDto(SpotUpdateModel spotUpdateModel, @MappingTarget SpotEntity spotEntity);
    List<SpotShorthandModel> entitiesToShorthandDtos(List<SpotEntity> entities);
    @Mappings({
            @Mapping(source = "reviewStats.combinedRating", target = "combinedRating"),
            @Mapping(source = "reviewStats.combinedQuality", target = "combinedLocaleQuality"),
            @Mapping(source = "reviewStats.combinedAccessibility", target = "combinedAccessibility"),
            @Mapping(source = "reviewStats.combinedStaffKindness", target = "combinedStaffKindness"),
            @Mapping(source = "reviewStats.combinedAffordability", target = "combinedAffordability"),
            @Mapping(source = "reviewStats.combinedAtmosphere", target = "combinedAtmosphere"),
            @Mapping(source = "reviewStats.combinedCleanliness", target = "combinedCleanliness")
    })
    SpotOverviewModel entityOverviewToDto(SpotEntity entity);
    SpotShorthandModel entityToShorthandDto(SpotEntity entity);
    SpotMapModel entityToMapDto(SpotEntity entity);
    List<SpotMapModel> entitiesToMapDtos(List<SpotEntity> entities);
}
