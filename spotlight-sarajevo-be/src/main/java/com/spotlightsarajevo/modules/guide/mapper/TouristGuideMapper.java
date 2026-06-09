package com.spotlightsarajevo.modules.guide.mapper;

import com.spotlightsarajevo.modules.guide.api.dto.*;
import com.spotlightsarajevo.modules.guide.domain.entity.TouristGuideEntity;
import com.spotlightsarajevo.modules.guide.domain.entity.TouristGuideSectionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TouristGuideMapper {
    TouristGuideShorthandModel entityToShorthandDto(TouristGuideEntity entity);

    TouristGuideModel entityToDto(TouristGuideEntity entity);

    List<TouristGuideShorthandModel> entitiesToShorthandDtos(List<TouristGuideEntity> entities);

    TouristGuideOverviewModel entityToOverviewDto(TouristGuideEntity entity);

    TouristGuideEntity dtoToEntity(TouristGuideCreateModel dto);

    TouristGuideSectionModel sectionEntityToDto(TouristGuideSectionEntity entity);

    TouristGuideSectionEntity dtoToSectionEntity(TouristGuideSectionCreateModel dto);

    void updateEntityFromDto(TouristGuideUpdateModel dto, @MappingTarget TouristGuideEntity entity);
}
